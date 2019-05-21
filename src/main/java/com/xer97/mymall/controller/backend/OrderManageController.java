package com.xer97.mymall.controller.backend;

import com.github.pagehelper.PageInfo;
import com.xer97.mymall.common.Const;
import com.xer97.mymall.common.ResponseCode;
import com.xer97.mymall.common.ServerResponse;
import com.xer97.mymall.pojo.User;
import com.xer97.mymall.service.IOrderService;
import com.xer97.mymall.service.IUserService;
import com.xer97.mymall.vo.OrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @author xer97
 * @date 2019/5/7 20:16
 */
@Controller
@RequestMapping("/manage/order/")
public class OrderManageController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private IOrderService iOrderService;

    @RequestMapping("list")
    @ResponseBody
    public ServerResponse<PageInfo> list(HttpSession session,
                                         @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
        }
        //校验是否为管理员
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iOrderService.getManageOrderList(pageNum, pageSize);
        } else {
            return ServerResponse.createByErrorMessage("无操作权限，需要管理员权限");
        }
    }

    @RequestMapping("detail")
    @ResponseBody
    public ServerResponse<OrderVO> detail(HttpSession session, Long orderNo) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
        }
        //校验是否为管理员
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iOrderService.getManageOrderDetail(orderNo);
        } else {
            return ServerResponse.createByErrorMessage("无操作权限，需要管理员权限");
        }
    }

    @RequestMapping("search")
    @ResponseBody
    public ServerResponse<PageInfo> search(HttpSession session, Long orderNo,
                                           @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                           @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
        }
        //校验是否为管理员
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iOrderService.searchOrder(orderNo, pageNum, pageSize);
        } else {
            return ServerResponse.createByErrorMessage("无操作权限，需要管理员权限");
        }
    }

    @RequestMapping("send_goods")
    @ResponseBody
    public ServerResponse<String> orderSendGoods(HttpSession session, Long orderNo) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
        }
        //校验是否为管理员
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iOrderService.manageSendGoods(orderNo);
        } else {
            return ServerResponse.createByErrorMessage("无操作权限，需要管理员权限");
        }
    }

}
