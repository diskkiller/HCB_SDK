package com.hcb.hcbsdk.service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author  Administrator
 * @des	创建线程池,执行任务,提交任务
 *
 * @version $Rev: 13 $
 * @updateAuthor $Author: admin $
 * @updateDes TODO
 */
public class ThreadPoolProxy {
	ThreadPoolExecutor mExecutor;
	ExecutorService executorService;// 只需创建一次
	int					mCorePoolSize;
	int					mMaximumPoolSize;
	long				mKeepAliveTime;

	public ThreadPoolProxy(int corePoolSize, int maximumPoolSize, long keepAliveTime) {
		super();
		mCorePoolSize = corePoolSize;
		mMaximumPoolSize = maximumPoolSize;
		mKeepAliveTime = keepAliveTime;
	}

	private ThreadPoolExecutor initThreadPoolExecutor() {//双重检查加锁
		if (mExecutor == null) {
			synchronized (ThreadPoolProxy.class) {
				if (mExecutor == null) {
					TimeUnit unit = TimeUnit.MILLISECONDS;
					BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();// 无界队列
					ThreadFactory threadFactory = Executors.defaultThreadFactory();
					RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();// 丢弃任务并抛出RejectedExecutionException异常。
					mExecutor = new ThreadPoolExecutor(//
							mCorePoolSize,// 核心的线程数
							mMaximumPoolSize,// 最大的线程数
							mKeepAliveTime, // 保持时间
							unit, // 保持时间对应的单位
							workQueue,// 缓存队列/阻塞队列
							threadFactory, // 线程工厂
							handler// 异常捕获器
					);
				}
			}
		}
		return mExecutor;
	}

	private ExecutorService initCachedThreadPoolExecutor() {//双重检查加锁
		if (executorService == null) {
			synchronized (ThreadPoolProxy.class) {
				if (executorService == null) {
					executorService = Executors.newCachedThreadPool();
				}
			}
		}
		return executorService;
	}

	private ExecutorService initSingleThreadPoolExecutor() {//双重检查加锁
		if (executorService == null) {
			synchronized (ThreadPoolProxy.class) {
				if (executorService == null) {
					executorService = Executors.newSingleThreadExecutor();
				}
			}
		}
		return executorService;
	}



	/**执行任务*/
	public void execute(Runnable task) {
		initThreadPoolExecutor();
		mExecutor.execute(task);
	}

	/**提交任务*/
	public Future<?> submit(Runnable task) {
		initThreadPoolExecutor();
		return mExecutor.submit(task);
	}

	/**移除任务*/
	public void removeTask(Runnable task) {
		initThreadPoolExecutor();
		mExecutor.remove(task);
	}
}
