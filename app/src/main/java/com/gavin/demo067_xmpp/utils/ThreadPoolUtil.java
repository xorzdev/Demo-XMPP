package com.gavin.demo067_xmpp.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程池管理工具类
 */
public class ThreadPoolUtil {
    public static ExecutorService catchPool = null;
    public static ExecutorService singlePool = null;

    /**
     * 创建corePoolSize为0
     * 最大线程数为整型的最大数
     * 线程keepAliveTime为1分钟
     * 缓存任务的队列为SynchronousQueue的线程池
     */
    public static void insertTaskToCatchPool(Runnable command) {
        if (catchPool == null) {
            catchPool = Executors.newCachedThreadPool();
        }
        catchPool.execute(command);
    }

    /**
     * 创建大小为1的固定线程池
     */
    public static void insertTaskToSinglePool(Runnable command) {
        if (singlePool == null) {
            singlePool = Executors.newSingleThreadExecutor();
        }
        singlePool.execute(command);
    }

    /**
     * 关闭所有正在执行的线程
     */
    public static void closeAllThreadPool() {
        if (catchPool != null) {
            catchPool.shutdownNow();
        }
        if (singlePool != null) {
            singlePool.shutdownNow();
        }
    }

}
