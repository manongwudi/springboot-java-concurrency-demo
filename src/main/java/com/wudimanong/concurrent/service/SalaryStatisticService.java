package com.wudimanong.concurrent.service;

import com.wudimanong.concurrent.bean.StatisticReportBO;
import com.wudimanong.concurrent.bean.StatisticReportDTO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author jiangqiao
 */
public interface SalaryStatisticService {

    /**
     * 薪水计算
     *
     * @param statisticReportDTO
     * @return
     */
    StatisticReportBO statisticReport(StatisticReportDTO statisticReportDTO);

}
