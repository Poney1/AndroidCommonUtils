package com.aaisme.Aa.util;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.util.Log;

/**
 * 线程池管理类
 * @author du
 * @version 1.0
 * **/
public class ThreadManager {

	private static ThreadPoolProxy poolProxy;
	/**
	 * 改方法用于获取到线程池代理类对象，获取到后调用execute(Runnable task)方法用来执行任务
	 * **/
	public static ThreadPoolProxy getInstance(int coreThreadNumber,int maxThreadNumber,long keepAliveTime){
		if(poolProxy ==null){
			synchronized (ThreadManager.class) {
				if(poolProxy==null){
					poolProxy =new ThreadPoolProxy(coreThreadNumber, maxThreadNumber, keepAliveTime);
				}
			}
		}
		return poolProxy;
	}
	/**
	 * 线程池代理类对象
	 * **/
	public static class ThreadPoolProxy{
		private int corePoolSize;
		private int maximumPoolSize;
		private long keepAliveTime;
		private ThreadPoolExecutor pool;
		private ThreadPoolProxy(int corePoolSize,int maximumPoolSize,long keepAliveTime){
			this.corePoolSize =corePoolSize;
			this.maximumPoolSize =maximumPoolSize;
			this.keepAliveTime =keepAliveTime;
		}
		/**
		 * 执行任务的方法
		 * @param 
		 * task 需要执行的任务
		 * **/
		@SuppressLint("NewApi")
		public void execute(Runnable task){
			Log.i("当前正在执行任务的线程的信息：", "线程-->"+Thread.currentThread().getName()+",线程ID："+Thread.currentThread().getId()+",正在"+"执行任务："+task.getClass().getName());
			if(pool ==null||pool.isShutdown()){
				pool =new ThreadPoolExecutor(
						corePoolSize, //核心线程数
						maximumPoolSize, //最大线程池容量
						keepAliveTime, //线程池中线程不干活的存活时间
						TimeUnit.MILLISECONDS, 
						new LinkedBlockingDeque<Runnable>(), //多个任务到达时的任务队列
						Executors.defaultThreadFactory(), //使用默认的线程工厂创建线程
						new RejectedExecutionHandler() { //当执行任务失败是的异常处理
							@Override
							public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
							}
						});
			}
			//开始执行任务
			pool.execute(task);;
		}
	}
}
