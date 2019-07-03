package com.xer97.mymall.controller.portal;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.xer97.mymall.common.Const;
import com.xer97.mymall.common.ResponseCode;
import com.xer97.mymall.common.ServerResponse;
import com.xer97.mymall.pojo.User;
import com.xer97.mymall.service.IOrderService;
import com.xer97.mymall.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author xer97
 * @date 2019/4/22 21:09
 */
@Controller
@RequestMapping("/order/")
@Slf4j
public class OrderController {

//    private static Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private IOrderService iOrderService;

    @RequestMapping("create")
    @ResponseBody
    public ServerResponse create(HttpSession session, Integer shippingId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return iOrderService.createOrder(user.getId(), shippingId);
    }

    @RequestMapping("cancel")
    @ResponseBody
    public ServerResponse cancel(HttpSession session, Long orderNo) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.cancelOrder(user.getId(), orderNo);
    }

    @RequestMapping("get_order_cart_product")
    @ResponseBody
    public ServerResponse getOrderCartProduct(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.getOrderCartProduct(user.getId());
    }

    @RequestMapping("detail")
    @ResponseBody
    public ServerResponse<OrderVO> detail(HttpSession session, Long orderNo) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.getOrderDetail(user.getId(), orderNo);
    }

    @RequestMapping("list")
    @ResponseBody
    public ServerResponse<PageInfo> list(HttpSession session,
                                         @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.getOrderList(user.getId(), pageNum, pageSize);
    }

    @RequestMapping("pay")
    @ResponseBody
    public ServerResponse pay(HttpSession session, Long orderNo, HttpServletRequest request) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        String path = request.getSession().getServletContext().getRealPath("upload");
        return iOrderService.pay(user.getId(), orderNo, path);
    }

    @RequestMapping("alipay_callback")
    @ResponseBody
    public Object alipayCallBack(HttpServletRequest request) {
        // 获取支付宝回调的请求信息
        Map<String, String[]> map = request.getParameterMap();
        Map<String, String> params = Maps.newHashMap();
        for (Map.Entry<String, String[]> entry : map.entrySet()) {
            String name = entry.getKey();
            String[] value = entry.getValue();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < value.length; i++) {
                sb.append(value[i]);
                if (i != value.length - 1) {
                    sb.append(",");
                }
            }
            params.put(name, sb.toString());
        }
        log.info("支付宝回调,sign:{},trade_status:{},参数:{}", params.get("sign"), params.get("trade_status"), params.toString());
        // 验证支付宝回调的正确性，并避免重复通知
        params.remove("sign_type");
        try {
            boolean alipayRSACheckedV2 = AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(), "utf-8", Configs.getSignType());
            if (!alipayRSACheckedV2) {
                return ServerResponse.createByErrorMessage("非法请求，验证不通过");
            }
        } catch (AlipayApiException e) {
            log.error("支付宝回调验证异常", e);
        }
        // TODO 验证各种数据

        ServerResponse serverResponse = iOrderService.aliCallback(params);
        if (serverResponse.isSuccess()) {
            return Const.AlipayCallback.RESPONSE_SUCCESS;
        } else {
            return Const.AlipayCallback.RESPONSE_FAILED;
        }
    }

    @RequestMapping("query_order_pay_status")
    @ResponseBody
    public ServerResponse<Boolean> queryOrderPayStatus(HttpSession session, Long orderNo) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        ServerResponse serverResponse = iOrderService.queryOrderPayStatus(user.getId(), orderNo);
        if (serverResponse.isSuccess()) {
            return ServerResponse.createBySuccess(Boolean.TRUE);
        } else {
            return ServerResponse.createBySuccess(Boolean.FALSE);
        }

    }
}
