package com.xer97.mymall.service;

import com.github.pagehelper.PageInfo;
import com.xer97.mymall.common.ServerResponse;
import com.xer97.mymall.vo.OrderVO;

import java.util.Map;

/**
 * @author xer97
 * @date 2019/4/22 21:12
 */
public interface IOrderService {
    /**
     * 支付功能
     *
     * @param userId  用户id
     * @param orderNo 订单号
     * @param path    二维码存放路径
     * @return 服务响应 附带二维码图片的url
     */
    ServerResponse pay(Integer userId, Long orderNo, String path);

    /**
     * 处理支付宝回调
     *
     * @param params 回调的请求信息
     * @return 服务响应 是否成功
     */
    ServerResponse aliCallback(Map<String, String> params);

    /**
     * 查询订单状态是否支付成功
     *
     * @param userId  用户id
     * @param orderNo 订单号
     * @return 服务响应
     */
    ServerResponse queryOrderPayStatus(Integer userId, Long orderNo);

    /**
     * 创建订单
     *
     * @param userId     用户id
     * @param shippingId 收货地址id
     * @return 服务响应 附带订单详情视图对象
     */
    ServerResponse createOrder(Integer userId, Integer shippingId);

    /**
     * 取消订单
     *
     * @param userId  用户id
     * @param orderNo 订单号
     * @return 服务响应
     */
    ServerResponse cancelOrder(Integer userId, Long orderNo);

    /**
     * 获取确认提交订单前显示的订单详情，即购物车中勾选的商品列表
     *
     * @param userId 用户id
     * @return 服务响应 附带订单商品视图对象
     */
    ServerResponse getOrderCartProduct(Integer userId);

    /**
     * 获取订单详情
     *
     * @param userId  用户id
     * @param orderNo 订单号
     * @return 服务响应 附带订单的视图对象
     */
    ServerResponse<OrderVO> getOrderDetail(Integer userId, Long orderNo);

    /**
     * 查询用户所有订单信息
     *
     * @param userId   用户id
     * @param pageNum  当前页码
     * @param pageSize 页大小
     * @return 服务响应 附带分页信息
     */
    ServerResponse<PageInfo> getOrderList(Integer userId, int pageNum, int pageSize);

    /**
     * 管理员获取所有订单信息
     *
     * @param pageNum  当前页码
     * @param pageSize 页大小
     * @return 服务响应 附带分页信息
     */
    ServerResponse<PageInfo> getManageOrderList(int pageNum, int pageSize);

    /**
     * 管理员获取订单详情
     *
     * @param orderNo 订单号
     * @return 服务响应 附带订单视图对象
     */
    ServerResponse<OrderVO> getManageOrderDetail(Long orderNo);

    /**
     * 管理员搜索订单
     *
     * @param orderNo  订单号
     * @param pageNum  当前页码
     * @param pageSize 页大小
     * @return 服务响应 附带分页信息
     */
    ServerResponse<PageInfo> searchOrder(Long orderNo, int pageNum, int pageSize);

    /**
     * 订单发货
     *
     * @param orderNo 订单号
     * @return 服务响应
     */
    ServerResponse<String> manageSendGoods(Long orderNo);
}
