package com.wudimanong.concurrent.controller;

import com.wudimanong.concurrent.bean.StatisticReportBO;
import com.wudimanong.concurrent.bean.StatisticReportDTO;
import com.wudimanong.concurrent.service.SalaryStatisticService;
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
@RequestMapping("/api/salary")
public class SalaryStatisticController {

    @Autowired
    private SalaryStatisticService salaryStatisticServiceImpl;

    /**
     * 根据传入的部门编号，计算该部门下所有员工的工资，并汇总
     *
     * @param statisticReportDTO
     * @return
     */
    @PostMapping("/statisticReport")
    @ResponseBody
    public StatisticReportBO statisticReport(@RequestBody @Validated StatisticReportDTO statisticReportDTO) {
        return salaryStatisticServiceImpl.statisticReport(statisticReportDTO);
    }
}
