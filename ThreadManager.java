package com.aaisme.Aa.util;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.util.Log;

/**
 * �̳߳ع�����
 * @author du
 * @version 1.0
 * **/
public class ThreadManager {

	private static ThreadPoolProxy poolProxy;
	/**
	 * �ķ������ڻ�ȡ���̳߳ش�������󣬻�ȡ�������execute(Runnable task)��������ִ������
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
	 * �̳߳ش��������
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
		 * ִ������ķ���
		 * @param 
		 * task ��Ҫִ�е�����
		 * **/
		@SuppressLint("NewApi")
		public void execute(Runnable task){
			Log.i("��ǰ����ִ��������̵߳���Ϣ��", "�߳�-->"+Thread.currentThread().getName()+",�߳�ID��"+Thread.currentThread().getId()+",����"+"ִ������"+task.getClass().getName());
			if(pool ==null||pool.isShutdown()){
				pool =new ThreadPoolExecutor(
						corePoolSize, //�����߳���
						maximumPoolSize, //����̳߳�����
						keepAliveTime, //�̳߳����̲߳��ɻ�Ĵ��ʱ��
						TimeUnit.MILLISECONDS, 
						new LinkedBlockingDeque<Runnable>(), //������񵽴�ʱ���������
						Executors.defaultThreadFactory(), //ʹ��Ĭ�ϵ��̹߳��������߳�
						new RejectedExecutionHandler() { //��ִ������ʧ���ǵ��쳣����
							@Override
							public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
							}
						});
			}
			//��ʼִ������
			pool.execute(task);;
		}
	}
}
