package com.xer97.mymall.service.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.xer97.mymall.common.Const;
import com.xer97.mymall.common.ResponseCode;
import com.xer97.mymall.common.ServerResponse;
import com.xer97.mymall.dao.CartMapper;
import com.xer97.mymall.dao.ProductMapper;
import com.xer97.mymall.pojo.Cart;
import com.xer97.mymall.pojo.Product;
import com.xer97.mymall.service.ICartService;
import com.xer97.mymall.util.BigDecimalUtil;
import com.xer97.mymall.util.PropertiesUtil;
import com.xer97.mymall.vo.CartProductVO;
import com.xer97.mymall.vo.CartVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author xer97
 * @date 2019/4/18 22:52
 */
@Service
public class CartServiceImpl implements ICartService {

    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;

    @Override
    public ServerResponse<CartVO> list(Integer userId) {
        CartVO cartVO = this.getCartVOLimit(userId);
        return ServerResponse.createBySuccess(cartVO);
    }

    @Override
    public ServerResponse<CartVO> add(Integer userId, Integer productId, Integer count) {
        if (this.isIllegalArgument(productId, count)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        // 先判断购物车中是否已有该商品
        Cart cart = cartMapper.selectByUseIdAndProductId(userId, productId);
        if (cart == null) {
            // 购物车没有该商品，新增商品到购物车
            Cart cartItem = new Cart();
            cartItem.setQuantity(count);
            cartItem.setUserId(userId);
            cartItem.setProductId(productId);
            cartItem.setChecked(Const.Cart.CHECKED);
            int rowCount = cartMapper.insert(cartItem);
            if (rowCount < 1) {
                return ServerResponse.createByErrorMessage("添加购物车失败");
            }
        } else {
            // 购物车已有该商品，对数量进行累加
            count = cart.getQuantity() + count;
            cart.setQuantity(count);
            cartMapper.updateByPrimaryKeySelective(cart);
        }
        return this.list(userId);
    }

    @Override
    public ServerResponse<CartVO> update(Integer userId, Integer productId, Integer count) {
        if (this.isIllegalArgument(productId, count)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Cart cart = cartMapper.selectByUseIdAndProductId(userId, productId);
        if (cart != null) {
            cart.setQuantity(count);
            cartMapper.updateByPrimaryKeySelective(cart);
        }
        return this.list(userId);
    }

    @Override
    public ServerResponse<CartVO> deleteProduct(Integer userId, String productIds) {
        List<String> productIdList = Splitter.on(",").splitToList(productIds);
        if (CollectionUtils.isEmpty(productIdList)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        cartMapper.deleteByUserIdAndProductIds(userId, productIdList);
        return this.list(userId);
    }

    @Override
    public ServerResponse<CartVO> selectOrUnSelect(Integer userId, Integer productId, Integer checked) {
        cartMapper.checkedOrUncheckedProduct(userId, productId, checked);
        return this.list(userId);
    }

    @Override
    public ServerResponse<Integer> getCartProductCount(Integer userId) {
        if (userId == null) {
            return ServerResponse.createBySuccess(0);
        }
        int count = cartMapper.selectCartProductCount(userId);
        return ServerResponse.createBySuccess(count);
    }


    private boolean isIllegalArgument(Integer productId, Integer count) {
        return productId == null || count == null;
    }

    private CartVO getCartVOLimit(Integer userId) {
        CartVO cartVO = new CartVO();
        List<Cart> cartList = cartMapper.selectByUserId(userId);
        List<CartProductVO> cartProductVOList = Lists.newArrayList();

        BigDecimal cartTotalPrice = new BigDecimal("0");
        if (CollectionUtils.isNotEmpty(cartList)) {
            for (Cart cartItem : cartList) {
                CartProductVO cartProductVO = new CartProductVO();
                cartProductVO.setCartId(cartItem.getId());
                cartProductVO.setProductId(cartItem.getProductId());
                cartProductVO.setUserId(cartItem.getUserId());
                // 获取商品详细信息
                Product product = productMapper.selectByPrimaryKey(cartItem.getProductId());
                if (product != null) {
                    cartProductVO.setProductName(product.getName());
                    cartProductVO.setProductPrice(product.getPrice());
                    cartProductVO.setProductSubtitle(product.getSubtitle());
                    cartProductVO.setProductMainImage(product.getMainImage());
                    cartProductVO.setProductStatus(product.getStatus());
                    cartProductVO.setProductStock(product.getStock());
                    // 判断库存
                    int buyLimitCount = 0;
                    if (product.getStock() >= cartItem.getQuantity()) {
                        buyLimitCount = cartItem.getQuantity();
                        cartProductVO.setLimitQuantity(Const.Cart.LIMIT_NUM_SUCCESS);
                    } else {
                        buyLimitCount = product.getStock();
                        cartProductVO.setLimitQuantity(Const.Cart.LIMIT_NUM_FAIL);
                        // 更新购物车中的有效选择数量
                        Cart cart = new Cart();
                        cart.setUserId(cartItem.getUserId());
                        cart.setQuantity(buyLimitCount);
                        cartMapper.updateByPrimaryKeySelective(cart);
                    }
                    cartProductVO.setQuantity(buyLimitCount);
                    // 计算单件商品的总价
                    cartProductVO.setProductTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(), buyLimitCount));
                    cartProductVO.setProductChecked(cartItem.getChecked());
                }
                if (cartItem.getChecked() == Const.Cart.CHECKED) {
                    // 判断是否勾选，是则增加到购物车总价中
                    cartTotalPrice = BigDecimalUtil.add(cartTotalPrice.doubleValue(), cartProductVO.getProductTotalPrice().doubleValue());
                }
                cartProductVOList.add(cartProductVO);
            }
        }
        cartVO.setCartProductVOList(cartProductVOList);
        cartVO.setCartTotalPrice(cartTotalPrice);
        cartVO.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
        cartVO.setAllChecked(this.getAllCheckedStatus(userId));
        return cartVO;
    }

    private boolean getAllCheckedStatus(Integer userId) {
        if (userId == null) {
            return false;
        }
        return cartMapper.selectCartProductCheckStatusByUserId(userId) == 0;
    }
}
