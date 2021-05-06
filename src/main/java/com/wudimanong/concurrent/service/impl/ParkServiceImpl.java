package com.wudimanong.concurrent.service.impl;

import com.wudimanong.concurrent.bean.AccessParkBO;
import com.wudimanong.concurrent.bean.AccessParkDTO;
import com.wudimanong.concurrent.service.ParkService;
import com.wudimanong.concurrent.threadpool.AsyncManager;
import java.util.Random;
import java.util.concurrent.Semaphore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author jiangqiao
 */
@Service
@Slf4j
public class ParkServiceImpl implements ParkService {

    /**
     * 模拟停车场的车位数
     */
    private static Semaphore semaphore = new Semaphore(2);

    @Override
    public AccessParkBO accessPark(AccessParkDTO accessParkDTO) {
        AsyncManager.service.execute(() -> {
            if (semaphore.availablePermits() == 0) {
                log.info(Thread.currentThread().getName() + ",车牌号->" + accessParkDTO.getCarNo() + ",车位不足请耐心等待");
            } else {
                try {
                    //获取令牌尝试进入停车场
                    semaphore.acquire();
                    log.info(Thread.currentThread().getName() + ",车牌号->" + accessParkDTO.getCarNo() + ",成功进入停车场");
                    //模拟车辆在停车场停留的时间（30秒）
                    Thread.sleep(30000);
                    //释放令牌，腾出停车场车位
                    semaphore.release();
                    log.info(Thread.currentThread().getName() + ",车牌号->" + accessParkDTO.getCarNo() + ",驶出停车场");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        });
        //封装返回信息
        return AccessParkBO.builder().carNo(accessParkDTO.getCarNo())
                .currentPositionCount(semaphore.availablePermits())
                .isPermitAccess(semaphore.availablePermits() > 0 ? true : false).build();
    }
}
