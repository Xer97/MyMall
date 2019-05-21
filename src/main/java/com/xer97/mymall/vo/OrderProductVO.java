package com.xer97.mymall.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author xer97
 * @date 2019/5/7 19:30
 */
public class OrderProductVO {
    private List<OrderItemVO> orderItemVOList;
    private BigDecimal productTotalPrice;
    private String imageHost;

    public List<OrderItemVO> getOrderItemVOList() {
        return orderItemVOList;
    }

    public void setOrderItemVOList(List<OrderItemVO> orderItemVOList) {
        this.orderItemVOList = orderItemVOList;
    }

    public BigDecimal getProductTotalPrice() {
        return productTotalPrice;
    }

    public void setProductTotalPrice(BigDecimal productTotalPrice) {
        this.productTotalPrice = productTotalPrice;
    }

    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }
}
