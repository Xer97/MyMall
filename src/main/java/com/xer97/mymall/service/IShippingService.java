package com.xer97.mymall.service;

import com.github.pagehelper.PageInfo;
import com.xer97.mymall.common.ServerResponse;
import com.xer97.mymall.pojo.Shipping;

import java.util.Map;

/**
 * @author xer97
 * @date 2019/4/20 16:19
 */
public interface IShippingService {

    /**
     * 新增用户收货地址
     *
     * @param userId   用户id
     * @param shipping 地址信息
     * @return 服务响应 附带新增地址的id
     */
    ServerResponse<Map> add(Integer userId, Shipping shipping);

    /**
     * 删除用户收货地址
     *
     * @param userId     用户id
     * @param shippingId 地址id
     * @return 服务响应
     */
    ServerResponse<String> delete(Integer userId, Integer shippingId);

    /**
     * 更新用户收货地址
     *
     * @param userId   用户id
     * @param shipping 新的地址信息
     * @return 服务响应
     */
    ServerResponse<String> update(Integer userId, Shipping shipping);

    /**
     * 查询用户某个收货地址
     *
     * @param userId     用户id
     * @param shippingId 地址id
     * @return 服务响应 附带地址信息
     */
    ServerResponse<Shipping> select(Integer userId, Integer shippingId);

    /**
     * 查询出用户所有收货地址
     *
     * @param userId   用户id
     * @param pageNum  页码
     * @param pageSize 页容量
     * @return 服务响应 附带收货地址的分页信息
     */
    ServerResponse<PageInfo<Shipping>> list(Integer userId, int pageNum, int pageSize);
}
