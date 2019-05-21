package com.xer97.mymall.controller.portal;

import com.github.pagehelper.PageInfo;
import com.xer97.mymall.common.ServerResponse;
import com.xer97.mymall.service.IProductService;
import com.xer97.mymall.vo.ProductDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author xer97
 * @date 2019/4/15 21:29
 */
@Controller
@RequestMapping("/product/")
public class ProductController {

    @Autowired
    private IProductService iProductService;

    @RequestMapping("detail")
    @ResponseBody
    public ServerResponse<ProductDetailVO> detail(Integer productId) {
        return iProductService.getProductDetail(productId);
    }

    @RequestMapping("list")
    @ResponseBody
    public ServerResponse<PageInfo> list(@RequestParam(value = "keyword", required = false) String keyword,
                                         @RequestParam(value = "categoryId", required = false) Integer categoryId,
                                         @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                         @RequestParam(value = "orderBy", defaultValue = "") String orderBy) {
        return iProductService.listProductByKeywordOrCategory(keyword, categoryId, pageNum, pageSize, orderBy);

    }
}
