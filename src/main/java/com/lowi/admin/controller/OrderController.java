package com.lowi.admin.controller;

import com.lowi.admin.pojo.dto.OrderDTO;
import com.lowi.admin.service.OrderService;
import com.lowi.admin.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * OrderController.java
 * ==============================================
 * Copy right 2015-2017 by http://www.51lick.com
 * ----------------------------------------------
 * This is not a free software, without any authorization is not allowed to use and spread.
 * ==============================================
 *
 * @author : gengyy
 * @version : v2.0
 * @desc :
 * @since : 2020/8/12 17:44
 */
@RestController
@RequestMapping("order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @RequestMapping("/submitOrder")
    public Result submitOrder(@RequestBody OrderDTO orderDTO){
        return orderService.submitOrder(orderDTO);
    }

    @RequestMapping("/payOrder")
    public Result payOrder(@RequestBody OrderDTO orderDTO){
        return orderService.payOrder(orderDTO);
    }
}
