package com.wudimanong.concurrent.service;

import com.wudimanong.concurrent.bean.AccessParkBO;
import com.wudimanong.concurrent.bean.AccessParkDTO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author jiangqiao
 */
public interface ParkService {

    /**
     * 停车进入
     *
     * @param accessParkDTO
     * @return
     */
    AccessParkBO accessPark(AccessParkDTO accessParkDTO);

}
