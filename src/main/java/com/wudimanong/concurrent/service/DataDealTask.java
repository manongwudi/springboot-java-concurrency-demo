package com.wudimanong.concurrent.service;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author jiangqiao
 */
public class DataDealTask implements Runnable {

    private List<Integer> list;
    private CountDownLatch latch;

    public DataDealTask(List<Integer> list, CountDownLatch latch) {
        this.list = list;
        this.latch = latch;
    }

    @Override
    public void run() {
        try {
            System.out.println("线程->" + Thread.currentThread().getName() + "，处理" + list.size());
        } finally {
            //处理完计数器递减
            latch.countDown();
        }
    }
}
