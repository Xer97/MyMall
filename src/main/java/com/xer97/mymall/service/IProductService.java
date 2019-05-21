package com.xer97.mymall.service;

import com.github.pagehelper.PageInfo;
import com.xer97.mymall.common.ServerResponse;
import com.xer97.mymall.pojo.Product;
import com.xer97.mymall.vo.ProductDetailVO;

/**
 * @author xer97
 * @date 2019/4/12 22:43
 */
public interface IProductService {

    /**
     * 更新或保存商品信息
     *
     * @param product 商品对象
     * @return 服务响应
     */
    ServerResponse<String> saveOrUpdateProduct(Product product);

    /**
     * 设置商品的状态
     *
     * @param productId 商品id
     * @param status    状态值
     * @return 服务响应
     */
    ServerResponse<String> setSaleStatus(Integer productId, Integer status);

    /**
     * 后台管理查询商品详细信息
     *
     * @param productId 商品id
     * @return 服务响应 附带商品详细信息视图
     */
    ServerResponse<ProductDetailVO> getManageProductDetail(Integer productId);

    /**
     * 后台获取商品列表信息，分页显示
     *
     * @param pageNum  当前页码
     * @param pageSize 一页最大商品数
     * @return 服务响应 附带商品分页信息
     */
    ServerResponse<PageInfo> listProducts(int pageNum, int pageSize);

    /**
     * 根据商品名或商品id查询商品信息，分页显示
     *
     * @param productName 商品名
     * @param productId   商品id
     * @param pageNum     当前页面
     * @param pageSize    一页最大商品数
     * @return 服务响应 附带商品分页信息
     */
    ServerResponse<PageInfo> searchProduct(String productName, Integer productId, int pageNum, int pageSize);

    /**
     * 前台查询商品详情
     *
     * @param productId 商品id
     * @return 服务响应 附带商品详细信息视图
     */
    ServerResponse<ProductDetailVO> getProductDetail(Integer productId);

    /**
     * 前台获取商品列表信息，分页显示
     *
     * @param keyword    关键字
     * @param categoryId 品类id
     * @param pageNum    当前页码
     * @param pageSize   一页最大商品数
     * @param orderBy    排序规则
     * @return 服务响应 附带商品分页信息
     */
    ServerResponse<PageInfo> listProductByKeywordOrCategory(String keyword, Integer categoryId, int pageNum, int pageSize, String orderBy);
}
