package com.wudimanong.concurrent.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiangqiao
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccessParkDTO {

    /**
     * 车牌号
     */
    private String carNo;

}
