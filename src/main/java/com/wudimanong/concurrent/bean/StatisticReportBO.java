package com.wudimanong.concurrent.bean;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiangqiao
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatisticReportBO implements Serializable {

    /**
     * 部门编号
     */
    private String departmentNo;

    /**
     * 员工总数
     */
    private Integer employeeCount;
    /**
     * 薪水总金额
     */
    private Integer salaryTotalAmount;
}
