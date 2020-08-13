package com.lowi.admin.dao;

import com.lowi.admin.entity.OrderRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Lowi
 * @since 2020-08-12
 */
@Repository
public interface OrderDao extends BaseMapper<OrderRecord> {

    int payOrder(Integer id, Integer version);

}
