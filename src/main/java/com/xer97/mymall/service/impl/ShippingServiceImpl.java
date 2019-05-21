package com.xer97.mymall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.xer97.mymall.common.ServerResponse;
import com.xer97.mymall.dao.ShippingMapper;
import com.xer97.mymall.pojo.Shipping;
import com.xer97.mymall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author xer97
 * @date 2019/4/20 16:20
 */
@Service
public class ShippingServiceImpl implements IShippingService {

    @Autowired
    private ShippingMapper shippingMapper;

    @Override
    public ServerResponse<Map> add(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);
        int rowCount = shippingMapper.insert(shipping);
        if (rowCount > 0) {
            Map<String, Integer> map = Maps.newHashMap();
            map.put("shippingId", shipping.getId());
            return ServerResponse.createBySuccess("新增地址成功", map);
        }
        return ServerResponse.createByErrorMessage("新增地址失败");
    }

    @Override
    public ServerResponse<String> delete(Integer userId, Integer shippingId) {
        // 防止横向越权
        int rowCount = shippingMapper.deleteByUserIdAndShippingId(userId, shippingId);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("删除收货地址成功");
        }
        return ServerResponse.createByErrorMessage("删除收货地址失败");
    }

    @Override
    public ServerResponse<String> update(Integer userId, Shipping shipping) {
        // 防止横向越权
        shipping.setUserId(userId);
        int rowCount = shippingMapper.updateByShipping(shipping);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("更新收货地址成功");
        }
        return ServerResponse.createByErrorMessage("更新收货地址失败");
    }

    @Override
    public ServerResponse<Shipping> select(Integer userId, Integer shippingId) {
        // 防止横向越权
        Shipping shipping = shippingMapper.selectByUserIdAndShippingId(userId, shippingId);
        if (shipping == null) {
            return ServerResponse.createByErrorMessage("无法查询到该收货地址");
        }
        return ServerResponse.createBySuccess(shipping);
    }

    @Override
    public ServerResponse<PageInfo<Shipping>> list(Integer userId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Shipping> shippingList = shippingMapper.selectByUserId(userId);
        PageInfo<Shipping> pageInfo = new PageInfo<>(shippingList);
        return ServerResponse.createBySuccess(pageInfo);
    }
}
