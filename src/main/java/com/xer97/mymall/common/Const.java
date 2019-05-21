package com.xer97.mymall.common;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * @author xer97
 * @date 2019/3/31 13:24
 */
public class Const {
    public static final String CURRENT_USER = "currentUser";
    public static final String USERNAME = "username";
    public static final String EMAIL = "email";

    public interface Role {
        int ROLE_CUSTOMER = 0;
        int ROLE_ADMIN = 1;
    }

    public interface Cart {
        int CHECKED = 1;
        int UN_CHECKED = 0;

        String LIMIT_NUM_SUCCESS = "LIMIT_NUM_SUCCESS";
        String LIMIT_NUM_FAIL = "LIMIT_NUM_FAIL";
    }

    public interface ProductListOrderBy {
        /**
         * 使用Set而不用List是因为Set的contains的时间复杂度为O(1)
         */
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_asc", "price_desc");
    }

    public enum ProductStatusEnum {
        /**
         * 商品在线
         */
        ON_SALE(1, "在线");

        private int code;
        private String value;

        ProductStatusEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        public int getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }
    }

    public enum OrderStatusEnum {
        /**
         * 订单取消
         */
        CANCELED(0, "已取消"),
        /**
         * 订单未支付
         */
        NO_PAY(10, "未支付"),
        /**
         * 订单已支付
         */
        PAID(20, "已支付"),
        /**
         * 订单已发货
         */
        SHIPPED(40, "已发货"),
        /**
         * 订单完成
         */
        ORDER_SUCCESS(50, "订单完成"),
        /**
         * 订单关闭
         */
        ORDER_CLOSE(60, "订单关闭");

        OrderStatusEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        private int code;
        private String value;

        public static OrderStatusEnum codeOf(int code) {
            for (OrderStatusEnum orderStatusEnum : values()) {
                if (orderStatusEnum.getCode() == code) {
                    return orderStatusEnum;
                }
            }
            throw new RuntimeException("没有找到对应枚举");
        }

        public int getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }
    }

    public interface AlipayCallback {
        String TRADE_STATUS_WAIT_BUYER_PAY = "WAIT_BUYER_PAY";
        String TRADE_STATUS_TRADE_SUCCESS = "TRADE_SUCCESS";

        String RESPONSE_SUCCESS = "success";
        String RESPONSE_FAILED = "failed";
    }

    public enum PayPlatformEnum {
        /**
         * 支付宝平台
         */
        ALIPAY(1, "支付宝");

        PayPlatformEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        private int code;
        private String value;

        public int getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }
    }

    public enum PaymentTypeEnum {
        /**
         * 支付类型，在线支付
         */
        ONLINE_PAY(1, "在线支付");

        PaymentTypeEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        private int code;
        private String value;

        public static PaymentTypeEnum codeOf(int code) {
            for (PaymentTypeEnum paymentTypeEnum : values()) {
                if (paymentTypeEnum.getCode() == code) {
                    return paymentTypeEnum;
                }
            }
            throw new RuntimeException("没有找到对应枚举");
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
