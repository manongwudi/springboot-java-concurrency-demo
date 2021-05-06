package com.wudimanong.concurrent.threadpool;

import java.util.concurrent.ExecutorService;

/**
 * @author jiangqiao
 */
public class AsyncManager {

    /**
     * 任务处理公共线程池
     */
    public static final ExecutorService service = SingleBlockPoolExecutor.getInstance().getPool();

}
