package com.wudimanong.concurrent.controller;

import com.wudimanong.concurrent.bean.AccessParkBO;
import com.wudimanong.concurrent.bean.AccessParkDTO;
import com.wudimanong.concurrent.bean.CreateOrderBO;
import com.wudimanong.concurrent.bean.CreateOrderDTO;
import com.wudimanong.concurrent.service.ParkService;
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
@RequestMapping("/api/park")
public class ParkController {

    @Autowired
    private ParkService parkServiceImpl;


    /**
     * 模拟申请停车接口
     *
     * @param accessParkDTO
     * @return
     */
    @PostMapping("/accessPark")
    @ResponseBody
    public AccessParkBO accessPark(@RequestBody @Validated AccessParkDTO accessParkDTO) {
        return parkServiceImpl.accessPark(accessParkDTO);
    }
}
