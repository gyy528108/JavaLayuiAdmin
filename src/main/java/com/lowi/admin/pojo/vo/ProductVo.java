package com.lowi.admin.pojo.vo;

import com.lowi.admin.dao.TwoCategoryDao;
import com.lowi.admin.entity.Product;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

/**
 * ProductVo.java
 * ==============================================
 * Copy right 2015-2017 by http://www.51lick.com
 * ----------------------------------------------
 * This is not a free software, without any authorization is not allowed to use and spread.
 * ==============================================
 *
 * @author : gengyy
 * @version : v2.0
 * @desc :
 * @since : 2020/7/23 14:09
 */
@Data
public class ProductVo extends Product {
    private String twoCategoryStr;
    private String oneCategoryStr;

    public static ProductVo toVo(Product product) {
        if (product == null) {
            return null;
        }
        ProductVo productVo=new ProductVo();
        BeanUtils.copyProperties(product,productVo);
        return productVo;
    }
}
