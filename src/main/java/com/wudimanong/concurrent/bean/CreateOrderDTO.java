package com.wudimanong.concurrent.bean;

import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author jiangqiao
 */
@Data
public class CreateOrderDTO {

    @NotNull(message = "订单号不能为空")
    private String orderId;
    @NotNull(message = "订单金额不能为空")
    private Integer amount;
}
