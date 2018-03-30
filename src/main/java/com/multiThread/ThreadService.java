package com.multiThread;

import java.text.SimpleDateFormat;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadService {

	public static void main(String[] args) throws Exception{
		CallableTask task=new CallableTask();
		FutureTask<Integer> futureTask=new FutureTask<Integer>(task);
		
		Thread t=new Thread(futureTask);
		t.start();
        
		
	}

	public static ExecutorService createThreadPool(int type,int num){
		ExecutorService  threadPool=null;
		if(type==1){//创建固定线程数量的线程池
			threadPool=Executors.newFixedThreadPool(num);
		}else if(type==2){//创建动态增长的线程池
			threadPool=Executors.newCachedThreadPool();
		}else if(type==3){// 创建定长线程池 执行定时任务
			threadPool=Executors.newScheduledThreadPool(num);
		}else if(type==4){// 创建单个线程 唯一执行任务，保证 FIFO   LIFO 优先级 的执行
			threadPool=Executors.newSingleThreadExecutor();
			
		}
		
		return threadPool;
	}
	public static void createThreadPool(){
		ThreadPoolExecutor threadPool=new ThreadPoolExecutor(10, 50, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(1024));
		
	}
}
class CallableTask implements Callable<Integer>{

	public Integer call() throws Exception {
		long time=System.currentTimeMillis();
		String date=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time);
		System.out.println(" 现在的时间是： "+date);

		return 200;
	}
	
}









