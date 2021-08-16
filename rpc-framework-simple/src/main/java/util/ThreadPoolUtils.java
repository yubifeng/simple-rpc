package util;

import java.util.Map;
import java.util.concurrent.*;

/**
 * 创建ThreadPool（线程池）工具类
 *
 * @author fanfanli
 * @date 2021/8/16
 */
public class ThreadPoolUtils {
    private static final int corePoolSize = 5;
    private static final int maximumPoolSize = 50;
    private static final long keepAliveTime = 60;

    public static ExecutorService createDefaultThreadPool(){
        BlockingQueue<Runnable> workingQueue = new ArrayBlockingQueue<>(100);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        return new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workingQueue, threadFactory);
    }




}
