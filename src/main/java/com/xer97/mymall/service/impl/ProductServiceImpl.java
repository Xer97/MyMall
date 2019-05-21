package com.xer97.mymall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.xer97.mymall.common.Const;
import com.xer97.mymall.common.ResponseCode;
import com.xer97.mymall.common.ServerResponse;
import com.xer97.mymall.dao.CategoryMapper;
import com.xer97.mymall.dao.ProductMapper;
import com.xer97.mymall.pojo.Category;
import com.xer97.mymall.pojo.Product;
import com.xer97.mymall.service.ICategoryService;
import com.xer97.mymall.service.IProductService;
import com.xer97.mymall.util.DateTimeUtil;
import com.xer97.mymall.util.PropertiesUtil;
import com.xer97.mymall.vo.ProductDetailVO;
import com.xer97.mymall.vo.ProductListVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xer97
 * @date 2019/4/12 22:43
 */
@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ICategoryService iCategoryService;

    @Override
    public ServerResponse<String> saveOrUpdateProduct(Product product) {
        if (product != null) {
            if (StringUtils.isNotBlank(product.getSubImages())) {
                String[] subImageArray = product.getSubImages().split(",");
                if (subImageArray.length > 0) {
                    product.setMainImage(subImageArray[0]);
                }
            }
            if (product.getId() != null) {
                int rowCount = productMapper.updateByPrimaryKeySelective(product);
                if (rowCount > 0) {
                    return ServerResponse.createBySuccessMessage("更新产品成功");
                }
                return ServerResponse.createByErrorMessage("更新产品失败");
            } else {
                int rowCount = productMapper.insert(product);
                if (rowCount > 0) {
                    return ServerResponse.createBySuccessMessage("新增产品成功");
                }
                return ServerResponse.createByErrorMessage("新增产品失败");
            }
        } else {
            return ServerResponse.createByErrorMessage("更新或保存产品参数不正确");
        }
    }

    @Override
    public ServerResponse<String> setSaleStatus(Integer productId, Integer status) {
        if (productId == null || status == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int rowCount = productMapper.updateByPrimaryKeySelective(product);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("修改产品销售状态成功");
        }
        return ServerResponse.createByErrorMessage("修改产品销售状态失败");
    }

    @Override
    public ServerResponse<ProductDetailVO> getManageProductDetail(Integer productId) {
        if (productId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return ServerResponse.createByErrorMessage("产品已被删除");
        }
        ProductDetailVO productDetailVo = assembleProductDetailVo(product);
        return ServerResponse.createBySuccess(productDetailVo);
    }

    private ProductDetailVO assembleProductDetailVo(Product product) {
        ProductDetailVO productDetailVo = new ProductDetailVO();
//        productDetailVo.setId(product.getId());
//        productDetailVo.setCategoryId(product.getCategoryId());
//        productDetailVo.setName(product.getName());
//        productDetailVo.setSubtitle(product.getSubtitle());
//        productDetailVo.setDetail(product.getDetail());
//        productDetailVo.setMainImage(product.getMainImage());
//        productDetailVo.setSubImages(product.getSubImages());
//        productDetailVo.setPrice(product.getPrice());
//        productDetailVo.setStock(product.getStock());
//        productDetailVo.setStatus(product.getStatus());

        // 浅拷贝
        BeanUtils.copyProperties(product, productDetailVo);

        productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://image.chen.com/"));

        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if (category == null) {
            productDetailVo.setParentCategoryId(0);
        } else {
            productDetailVo.setParentCategoryId(category.getParentId());
        }
        productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
        productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));
        return productDetailVo;
    }

    @Override
    public ServerResponse<PageInfo> listProducts(int pageNum, int pageSize) {
        // 开始分页
        PageHelper.startPage(pageNum, pageSize);
        List<Product> productList = productMapper.selectList();
        // 先获得productList中的分页信息存放到pageInfo中
        PageInfo pageResult = new PageInfo(productList);

        List<ProductListVO> productListVoList = Lists.newArrayList();
        for (Product product : productList) {
            ProductListVO productListVo = assembleProductListVo(product);
            productListVoList.add(productListVo);
        }
        // 将pageInfo中的list替换为业务需要的list视图
        pageResult.setList(productListVoList);
        return ServerResponse.createBySuccess(pageResult);
    }

    private ProductListVO assembleProductListVo(Product product) {
        ProductListVO productListVo = new ProductListVO();
        productListVo.setId(product.getId());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setName(product.getName());
        productListVo.setSubtitle(product.getSubtitle());
        productListVo.setMainImage(product.getMainImage());
        productListVo.setPrice(product.getPrice());
        productListVo.setStatus(product.getStatus());

        productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://image.chen.com/"));
        return productListVo;
    }

    @Override
    public ServerResponse<PageInfo> searchProduct(String productName, Integer productId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        if (StringUtils.isNotBlank(productName)) {
            productName = new StringBuilder("%").append(productName).append("%").toString();
        }
        List<Product> productList = productMapper.selectByNameAndProductId(productName, productId);

        List<ProductListVO> productListVoList = Lists.newArrayList();
        for (Product product : productList) {
            ProductListVO productListVo = assembleProductListVo(product);
            productListVoList.add(productListVo);
        }

        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVoList);
        return ServerResponse.createBySuccess(pageInfo);
    }

    @Override
    public ServerResponse<ProductDetailVO> getProductDetail(Integer productId) {
        if (productId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null || product.getStatus() != Const.ProductStatusEnum.ON_SALE.getCode()) {
            return ServerResponse.createByErrorMessage("产品已下架或删除");
        }
        ProductDetailVO productDetailVo = assembleProductDetailVo(product);
        return ServerResponse.createBySuccess(productDetailVo);
    }

    @Override
    public ServerResponse<PageInfo> listProductByKeywordOrCategory(String keyword, Integer categoryId, int pageNum, int pageSize, String orderBy) {
        if (StringUtils.isBlank(keyword) && categoryId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        List<Integer> categoryIdList = Lists.newArrayList();
        if (categoryId != null) {
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            if (category == null && StringUtils.isBlank(keyword)) {
                // 品类不存在且关键字为空白，返回一个空集
                PageHelper.startPage(pageNum, pageSize);
                List<ProductListVO> productListVOList = Lists.newArrayList();
                PageInfo pageInfo = new PageInfo(productListVOList);
                return ServerResponse.createBySuccess(pageInfo);
            }
            // 品类存在，查询该品类及其子类id
            if (category != null) {
                categoryIdList = iCategoryService.selectCategoryAndChildrenById(category.getId()).getData();
            }
        }
        if (StringUtils.isNotBlank(keyword)) {
            keyword = "%" + keyword + "%";
        }
        // 开始分页
        PageHelper.startPage(pageNum, pageSize);
        if (StringUtils.isNotBlank(orderBy)) {
            if (Const.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)) {
                String[] orderByArray = orderBy.split("_");
                // 设置分页排序
                PageHelper.orderBy(orderByArray[0] + " " + orderByArray[1]);
            }
        }
        keyword = StringUtils.isBlank(keyword) ? null : keyword;
        categoryIdList = categoryIdList.size() == 0 ? null : categoryIdList;
        List<Product> productList = productMapper.selectByNameAndCategoryIds(keyword, categoryIdList);

        List<ProductListVO> productListVOList = Lists.newArrayList();
        PageInfo pageInfo = new PageInfo(productList);
        for (Product product : productList) {
            ProductListVO productListVO = assembleProductListVo(product);
            productListVOList.add(productListVO);
        }
        pageInfo.setList(productListVOList);
        return ServerResponse.createBySuccess(pageInfo);
    }
}
