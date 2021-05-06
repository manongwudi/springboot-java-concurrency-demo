package com.wudimanong.concurrent.service.impl;

import com.wudimanong.concurrent.bean.StatisticReportBO;
import com.wudimanong.concurrent.bean.StatisticReportDTO;
import com.wudimanong.concurrent.service.SalaryStatisticService;
import com.wudimanong.concurrent.threadpool.AsyncManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author jiangqiao
 */
@Slf4j
@Service
public class SalaryStatisticServiceImpl implements SalaryStatisticService {

    /**
     * 模拟部门员工存储数据
     */
    public static Map<String, List<EmployeeSalaryInfo>> employeeMap = Collections.synchronizedMap(new HashMap<>());

    static {
        EmployeeSalaryInfo employeeA = new EmployeeSalaryInfo();
        employeeA.setEmployeeNo("100");
        employeeA.setBaseSalaryAmount(10000);
        employeeA.setSubsidyAmount(3000);
        EmployeeSalaryInfo employeeB = new EmployeeSalaryInfo();
        employeeB.setEmployeeNo("101");
        employeeB.setBaseSalaryAmount(30000);
        employeeB.setSubsidyAmount(3000);
        List<EmployeeSalaryInfo> list = new ArrayList<>();
        list.add(employeeA);
        list.add(employeeB);
        employeeMap.put("10", list);
    }

    @Override

    public StatisticReportBO statisticReport(StatisticReportDTO statisticReportDTO) {
        //查询部门下所有员工信息（模拟）
        List<EmployeeSalaryInfo> employeeSalaryInfos = employeeMap.get(statisticReportDTO.getDepartmentNo());
        if (employeeSalaryInfos == null) {
            log.info("部门员工信息不存在");
            return StatisticReportBO.builder().build();
        }
        //定义统计总工资的安全变量
        AtomicInteger totalSalary = new AtomicInteger();
        //开启栅栏(在各线程触发之后触发)
        CyclicBarrier cyclicBarrier = new CyclicBarrier(employeeSalaryInfos.size(), new Runnable() {
            //执行顺序-B1(随机)
            //该线程不会阻塞主线程
            @Override
            public void run() {
                log.info("汇总已分别计算出的两个员工的工资->" + totalSalary.get() + ",执行顺序->B");
            }
        });
        //执行顺序-A
        for (EmployeeSalaryInfo e : employeeSalaryInfos) {
            AsyncManager.service.submit(new Callable<Integer>() {
                @Override
                public Integer call() {
                    int totalAmount = e.getSubsidyAmount() + e.getBaseSalaryAmount();
                    log.info("计算出员工{}", e.getEmployeeNo() + "的工资->" + totalAmount + ",执行顺序->A");
                    //汇总总工资
                    totalSalary.addAndGet(totalAmount);
                    try {
                        //等待其他线程同步
                        cyclicBarrier.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                    return totalAmount;
                }
            });

        }
        //执行顺序-A/B（之前或之后随机,totalSalary值不能保证一定会得到，所以CyclicBarrier更适合无返回的可重复并行计算）
        //封装响应参数
        StatisticReportBO statisticReportBO = StatisticReportBO.builder().employeeCount(employeeSalaryInfos.size())
                .departmentNo(statisticReportDTO.getDepartmentNo())
                .salaryTotalAmount(totalSalary.get()).build();
        log.info("封装接口响应参数，执行顺序->A/B");
        return statisticReportBO;
    }

    @Data
    public static class EmployeeSalaryInfo {

        /**
         * 员工编号
         */
        private String employeeNo;
        /**
         * 基本工资
         */
        private Integer baseSalaryAmount;
        /**
         * 补助金额
         */
        private Integer subsidyAmount;
    }
}
