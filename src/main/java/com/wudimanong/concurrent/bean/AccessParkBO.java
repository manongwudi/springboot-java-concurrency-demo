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
public class AccessParkBO {

    /**
     * 车牌号
     */
    private String carNo;
    /**
     * 当前车位总数
     */
    private Integer currentPositionCount;
    /**
     * 是否允许进入
     */
    private Boolean isPermitAccess;

}
