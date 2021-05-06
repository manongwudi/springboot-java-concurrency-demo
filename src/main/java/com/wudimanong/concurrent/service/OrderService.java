package com.wudimanong.concurrent.service;

import com.wudimanong.concurrent.bean.CreateOrderBO;
import com.wudimanong.concurrent.bean.CreateOrderDTO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author jiangqiao
 */
public interface OrderService {

    /**
     * 创建订单
     *
     * @param createOrderDTO
     * @return
     */
    CreateOrderBO createOrder(CreateOrderDTO createOrderDTO);

}
