package com.xer97.mymall.service;

import com.xer97.mymall.common.ServerResponse;
import com.xer97.mymall.pojo.Category;

import java.util.List;

/**
 * @author xer97
 * @date 2019/4/8 19:02
 */
public interface ICategoryService {

    /**
     * 新增品类
     *
     * @param categoryName 新增品类名称
     * @param parentId     新增品类的父品类id
     * @return 服务响应
     */
    ServerResponse<String> addCategory(String categoryName, Integer parentId);

    /**
     * 更新品类名称
     *
     * @param categoryId   品类id
     * @param categoryName 新品类名称
     * @return 服务响应
     */
    ServerResponse<String> updateCategoryName(Integer categoryId, String categoryName);

    /**
     * 获取指定品类id的直接子品类
     *
     * @param categoryId 品类id
     * @return 服务响应 附带品类对象集合
     */
    ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId);

    /**
     * 递归查询本节点id及孩子节点的id
     *
     * @param categoryId 品类id
     * @return 服务响应 附带品类id集合
     */
    ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId);
}
