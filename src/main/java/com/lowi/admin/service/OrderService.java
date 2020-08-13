package com.lowi.admin.service;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lowi.admin.dao.OrderDao;
import com.lowi.admin.dao.ProductDao;
import com.lowi.admin.entity.OrderRecord;
import com.lowi.admin.entity.Product;
import com.lowi.admin.entity.User;
import com.lowi.admin.enums.MqTopicEnum;
import com.lowi.admin.enums.OrderStatusEnum;
import com.lowi.admin.mq.MqService;
import com.lowi.admin.pojo.dto.OrderDTO;
import com.lowi.admin.utils.LockUtil;
import com.lowi.admin.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

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
    private ProductDao productDao;
    @Autowired
    private MqService mqService;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static String PRODUCT_KEY = "product_";


    @Transactional
    public Result submitOrder(OrderDTO orderDTO) {
        Result result = new Result();
        String productCount = stringRedisTemplate.opsForValue().get(PRODUCT_KEY + orderDTO.getProductId());
        if (productCount == null) {
            String lockValue = String.valueOf((System.currentTimeMillis() + 10 * 1000));
            boolean lock = LockUtil.lock("key_" + PRODUCT_KEY + orderDTO.getProductId(), lockValue);
            if (!lock) {
                return Result.getInstance(1, "系统忙，请重试");
            }
            try {
                QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("id", orderDTO.getProductId());
                Product product = productDao.selectOne(queryWrapper);
                if (product == null) {
                    return Result.getInstance(1, "商品不存在");
                }
                stringRedisTemplate.opsForValue().increment(PRODUCT_KEY + orderDTO.getProductId(), product.getTotal());
            } finally {
                LockUtil.unlock("key_" + PRODUCT_KEY + orderDTO.getProductId(), lockValue);
            }
        }
        String userInfo = stringRedisTemplate.opsForValue().get(orderDTO.getToken());
        User user = JSONUtil.toBean(userInfo, User.class);
        Long increment = stringRedisTemplate.opsForValue().increment(PRODUCT_KEY + orderDTO.getProductId(), -1);
        if (increment < 0) {
            stringRedisTemplate.opsForValue().increment(PRODUCT_KEY + orderDTO.getProductId(), 1);
            return Result.getInstance(1, "已售罄");
        } else {
            String orderNo = RandomUtil.randomString(32);
            OrderRecord order = new OrderRecord();
            order.setOrderNo(orderNo);
            order.setProductId(orderDTO.getProductId());
            order.setUserId(user.getId());
            order.setStatus(0);
            order.setCreateTime(new Date());
            int insert = 0;
            try {
                insert = orderDao.insert(order);
            } catch (Exception e) {
                e.printStackTrace();
                stringRedisTemplate.opsForValue().increment(PRODUCT_KEY + orderDTO.getProductId(), 1);
            }
            if (insert > 0) {
                productDao.updateCount(orderDTO.getProductId());
                orderDTO.setOrderId(order.getId());
                String jsonStr = JSONUtil.toJsonStr(orderDTO);
                Result send = mqService.send(MqTopicEnum.DELAY.getTopic(), jsonStr, 5);
                if (send.getCode() == 1) {
                    stringRedisTemplate.opsForValue().increment(PRODUCT_KEY + orderDTO.getProductId(), 1);
                    orderDao.deleteById(order.getId());
                    return Result.getInstance(1, "系统异常，请稍后再试");
                }
                return Result.getInstance(0, "加入订单成功，请及时支付");
            }
        }
        return Result.getInstance(1, "系统异常，请稍后再试");
    }


    public Result payOrder(OrderDTO orderDTO) {
        OrderRecord order = orderDao.selectById(orderDTO.getId());
        boolean isExist = Optional.ofNullable(order).isPresent();
        Result result = new Result();
        if (!isExist) {
            result.setCode(1);
            result.setMsg("订单不存在");
            return result;
        }
        if (OrderStatusEnum.DELAY.getStatus().equals(order.getStatus()) || OrderStatusEnum.PAY.getStatus().equals(order.getStatus())) {
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
