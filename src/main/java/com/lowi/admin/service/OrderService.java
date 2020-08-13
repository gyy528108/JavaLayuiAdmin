package com.lowi.admin.service;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lowi.admin.dao.OrderDao;
import com.lowi.admin.entity.Order;
import com.lowi.admin.entity.User;
import com.lowi.admin.enums.MqTopicEnum;
import com.lowi.admin.enums.OrderStatusEnum;
import com.lowi.admin.mq.MqService;
import com.lowi.admin.pojo.dto.OrderDTO;
import com.lowi.admin.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

/**
 * OrderService.java
 * ==============================================
 * Copy right 2015-2017 by http://www.51lick.com
 * ----------------------------------------------
 * This is not a free software, without any authorization is not allowed to use and spread.
 * ==============================================
 *
 * @author : gengyy
 * @version : v2.0
 * @desc :
 * @since : 2020/8/12 17:45
 */
@Service
public class OrderService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private MqService mqService;

    private static String PRODUCT_KEY = "product_";


    public Result submitOrder(OrderDTO orderDTO) {
        String userInfo = stringRedisTemplate.opsForValue().get(orderDTO.getToken());
        User user = JSONUtil.toBean(userInfo, User.class);
        Result result = new Result();
        Long increment = stringRedisTemplate.opsForValue().increment(PRODUCT_KEY + orderDTO.getProductId(), -1);
        if (increment < 0) {
            stringRedisTemplate.opsForValue().increment(PRODUCT_KEY + orderDTO.getProductId(), 1);
            result.setCode(1);
            result.setMsg("已售完");
            return result;
        } else {
            String orderId = RandomUtil.randomString(32);
            Order order = new Order();
            order.setOrderId(orderId);
            order.setProductId(orderDTO.getProductId());
            order.setUserId(user.getId());
            order.setStatus(0);
            order.setCreateTime(new Date());
            int insert = 0;
            try {
                insert = orderDao.insert(order);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (insert > 0) {
                orderDTO.setOrderId(orderId);
                String jsonStr = JSONUtil.toJsonStr(orderDTO);
                mqService.send(MqTopicEnum.DALEY.getTopic(), jsonStr, 5);
                result.setCode(0);
                result.setMsg("加入订单成功，请及时支付");
                return result;
            }
        }
        result.setCode(1);
        result.setMsg("系统异常，请稍后再试");
        return result;
    }


    public Result payOrder(OrderDTO orderDTO) {
        Order order = orderDao.selectById(orderDTO.getId());
        boolean isExist = Optional.ofNullable(order).isPresent();
        Result result = new Result();
        if(!isExist){
            result.setCode(1);
            result.setMsg("订单不存在");
            return result;
        }
        if(OrderStatusEnum.DELAY.getStatus().equals(order.getStatus())||OrderStatusEnum.PAY.getStatus().equals(order.getStatus())){
            result.setCode(1);
            result.setMsg("订单状态异常");
            return result;
        }
        int payOrder = 0;
        try {
            payOrder = orderDao.payOrder(order.getId(), order.getVersion());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (payOrder > 0) {
            result.setCode(0);
            result.setMsg("支付成功");
            return result;
        }
        result.setCode(1);
        result.setMsg("服务异常，请稍后再试");
        return result;
    }
}
