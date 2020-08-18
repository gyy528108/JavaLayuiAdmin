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

    /**
     * 获取商品列表
     * @auther gengyy
     * @date 2020-08-14 14:23
     * @param  page 页数
     * @param  count 页码
     * @param  parentId 用户父id
     * @param  oneCategoryId 一级类别
     * @param  productInfo 详情
     * @return List<Product>
     * */
    List<Product> getProductList(Integer page, Integer count, Integer parentId, Integer oneCategoryId, String productInfo);
    /**
     * 获取商品列表总数
     * @auther gengyy
     * @date 2020-08-14 14:23
     * @param  parentId 用户父id
     * @param  oneCategoryId 一级类别
     * @param  productInfo 详情
     * @return int 返回总数
     * */
    int getProductTotal(Integer parentId, Integer oneCategoryId, String productInfo);
    /**
     * 更新产品数量
     * @auther gengyy
     * @date 2020-08-14 14:23
     * @param  productId 商品id
     * */
    void updateCount(Integer productId);
}
