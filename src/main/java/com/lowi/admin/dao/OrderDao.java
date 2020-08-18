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

    /**
     * 支付订单
     * @auther gengyy
     * @param id 订单的id
     * @param version 订单的版本号
     * @return int 数据是否成功
     * @date 2020-08-14 15:44
     * */
    int payOrder(Integer id, Integer version);

}
