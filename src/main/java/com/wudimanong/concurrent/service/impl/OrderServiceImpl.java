package com.wudimanong.concurrent.service.impl;

import com.wudimanong.concurrent.bean.CreateOrderBO;
import com.wudimanong.concurrent.bean.CreateOrderDTO;
import com.wudimanong.concurrent.service.DataDealTask;
import com.wudimanong.concurrent.service.OrderService;
import com.wudimanong.concurrent.threadpool.AsyncManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author jiangqiao
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Override
    public CreateOrderBO createOrder(CreateOrderDTO createOrderDTO) {
        //1、同步处理核心业务逻辑
        log.info("同步处理业务逻辑");
        //2、通过线程池提交，异步处理非核心逻辑，例如日志埋点
        AsyncManager.service.execute(() -> {
            System.out.println("线程->" + Thread.currentThread().getName() + ",正在执行异步日志处理任务");
        });
        //3、Future异步处理返回执行结果
        //定义接收线程执行结果的FutureTask对象
        List<Future<Integer>> results = Collections.synchronizedList(new ArrayList<>());
        //实现Callable接口定义线程执行逻辑
        results.add(AsyncManager.service.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                int a = 1, b = 2;
                System.out.println("Callable接口执行中");
                return a + b;
            }
        }));
        //输出线程返回结果
        for (Future<Integer> future : results) {
            try {
                //这里获取结果，等待时间设置200毫秒
                System.out.println("a+b=" + future.get(200, TimeUnit.MILLISECONDS));
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }
        //4、CountDownLatch的使用示例
        //模拟待处理数据生成
        Integer[] array = {10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 101, 102};
        List<Integer> list = new ArrayList<>();
        Arrays.asList(array).stream().map(o -> list.add(o)).collect(Collectors.toList());
        //对数据进行分组处理(5条记录为1组)
        Map<String, List<?>> entityMap = this.groupListByAvg(list, 6);
        //根据数据分组数量，确定同步计数器的值
        CountDownLatch latch = new CountDownLatch(entityMap.size());
        Iterator<Entry<String, List<?>>> it = entityMap.entrySet().iterator();
        try {
            //将分组数据分批提交给不同线程处理
            while (it.hasNext()) {
                DataDealTask dataDealTask = new DataDealTask((List<Integer>) it.next().getValue(), latch);
                AsyncManager.service.submit(dataDealTask);
            }
            //等待分批处理线程处理完成
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return CreateOrderBO.builder().result(true).build();
    }

    /**
     * 批量数据分组方法
     *
     * @param list0
     * @param limit
     * @return
     */
    public Map<String, List<?>> groupListByAvg(List<?> list0, int limit) {

        // 分组结果
        Map<String, List<?>> groupResultMap = Collections.synchronizedMap(new HashMap<>());
        // 单独计数器
        int counter = 0;
        // 分组处理
        List<?> list = list0;
        synchronized (list) {
            if (null != list && !list.isEmpty()) {
                int pointsDataLimit = limit;
                Integer size = list.size();
                // 判断是否有必要分批
                if (pointsDataLimit < size) {
                    // 分批数
                    int part = size % pointsDataLimit == 0 ? size / pointsDataLimit : size / pointsDataLimit + 1;
                    try {
                        for (int i = 0; i < part; i++) {
                            // 分页数据视图
                            List<?> listPage;
                            // 真实数据
                            List<Object> realListPage = Collections.synchronizedList(new ArrayList<>());
                            // 1000条
                            listPage = list.subList(0, pointsDataLimit);
                            // 真实数据填充
                            // 完成真实数据填充
                            realListPage.addAll(listPage);
                            groupResultMap.put("index_" + counter, realListPage);
                            // 数据剔除
                            list.removeAll(listPage);
                            // 计数器累加
                            counter++;
                        }
                        if (!list.isEmpty()) {
                            // 将剩下的数据放入Map
                            groupResultMap.put("index_" + (counter + 1), list);
                        }
                    } catch (Exception e) {
                        log.error(e.getMessage() + "_" + e, e);
                    }
                } else {
                    groupResultMap.put("index_" + counter, list);
                }
                log.info("共有 ： " + size + "条，！" + " 分为 ：" + groupResultMap.size() + "批");
            }
        }
        return groupResultMap;
    }
}
