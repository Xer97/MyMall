package com.xer97.mymall.service;

import com.xer97.mymall.common.ServerResponse;
import com.xer97.mymall.vo.CartVO;

/**
 * @author xer97
 * @date 2019/4/18 22:51
 */
public interface ICartService {
    /**
     * 列出购物车中所有商品
     *
     * @param userId 用户名
     * @return 服务响应 附带购物车视图信息
     */
    ServerResponse<CartVO> list(Integer userId);

    /**
     * 往购物车添加商品
     *
     * @param userId    用户id
     * @param productId 商品id
     * @param count     数量
     * @return 服务响应 附带购物车视图信息
     */
    ServerResponse<CartVO> add(Integer userId, Integer productId, Integer count);

    /**
     * 更新购物车中商品数量
     *
     * @param userId    用户id
     * @param productId 商品id
     * @param count     数量
     * @return 服务响应 附带购物车视图信息
     */
    ServerResponse<CartVO> update(Integer userId, Integer productId, Integer count);

    /**
     * 删除购物车中制定商品
     *
     * @param userId     用户id
     * @param productIds 一个或多个productId
     * @return 服务响应 附带购物车视图信息
     */
    ServerResponse<CartVO> deleteProduct(Integer userId, String productIds);

    /**
     * 对购物车商品进行选择或反选，商品id为null时，进行全选或全反选
     *
     * @param userId    用户id
     * @param productId 商品id
     * @param checked   反选还是正选
     * @return 服务响应 附带购物车视图信息
     */
    ServerResponse<CartVO> selectOrUnSelect(Integer userId, Integer productId, Integer checked);

    /**
     * 获取购物车商品数量
     *
     * @param userId 用户id
     * @return 数量
     */
    ServerResponse<Integer> getCartProductCount(Integer userId);

}
