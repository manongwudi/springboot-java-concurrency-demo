package com.wudimanong.concurrent.controller;

import com.wudimanong.concurrent.bean.CreateOrderBO;
import com.wudimanong.concurrent.bean.CreateOrderDTO;
import com.wudimanong.concurrent.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jiangqiao
 */
@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderServiceImpl;

    /**
     * 模拟订单创建接口
     *
     * @param createOrderDTO
     * @return
     */
    @PostMapping("/createOrder")
    @ResponseBody
    public CreateOrderBO createOrder(@RequestBody @Validated CreateOrderDTO createOrderDTO) {
        return orderServiceImpl.createOrder(createOrderDTO);
    }
}
