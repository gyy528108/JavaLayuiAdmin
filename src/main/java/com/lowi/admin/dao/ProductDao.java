package com.lowi.admin.dao;

import com.lowi.admin.entity.Product;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Lowi
 * @since 2020-07-22
 */
@Repository
public interface ProductDao extends BaseMapper<Product> {

    List<Product> getProductList(Integer page, Integer count, Integer parentId, Integer oneCategoryId, String productInfo);

    int getProductTotal(Integer parentId, Integer oneCategoryId, String productInfo);
}
