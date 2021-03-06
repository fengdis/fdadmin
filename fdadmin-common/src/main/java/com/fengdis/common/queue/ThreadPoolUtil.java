package com.fengdis.common.queue;

import java.util.concurrent.*;

/**
 * @version 1.0
 * @Descrittion: 线程池工具类
 * @author: fengdi
 * @since: 2018/9/3 0003 22:51
 */
public class ThreadPoolUtil {

	private ThreadPoolUtil() {

	}

	public static ExecutorService getExcutorService() {
		return new ThreadPoolExecutor(4, 8, 5, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	}

	public static ExecutorService getExcutorService1() {
		return new ThreadPoolExecutor(4, 8, 5, TimeUnit.SECONDS, new SynchronousQueue<>(), new ThreadPoolExecutor.CallerRunsPolicy());
	}
}
