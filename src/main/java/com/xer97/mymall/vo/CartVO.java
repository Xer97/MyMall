package com.xer97.mymall.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * 描述了整个购物车的视图信息
 *
 * @author xer97
 * @date 2019/4/18 23:11
 */
public class CartVO {

    private List<CartProductVO> cartProductVOList;
    private BigDecimal cartTotalPrice;
    private Boolean allChecked;
    private String imageHost;

    public List<CartProductVO> getCartProductVOList() {
        return cartProductVOList;
    }

    public void setCartProductVOList(List<CartProductVO> cartProductVOList) {
        this.cartProductVOList = cartProductVOList;
    }

    public BigDecimal getCartTotalPrice() {
        return cartTotalPrice;
    }

    public void setCartTotalPrice(BigDecimal cartTotalPrice) {
        this.cartTotalPrice = cartTotalPrice;
    }

    public Boolean getAllChecked() {
        return allChecked;
    }

    public void setAllChecked(Boolean allChecked) {
        this.allChecked = allChecked;
    }

    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }

    @Override
    public String toString() {
        return "CartVO{" +
                "cartProductVOList=" + cartProductVOList +
                ", cartTotalPrice=" + cartTotalPrice +
                ", allChecked=" + allChecked +
                ", imageHost='" + imageHost + '\'' +
                '}';
    }
}
