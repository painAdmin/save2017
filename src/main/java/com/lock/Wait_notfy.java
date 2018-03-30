package com.lock;

public class Wait_notfy {

	private static final Object lock=new Object();
	public Object lockClass=new Object();
	
	public static void main(String[] args){
//		testWaitAndNotify();
	    new Wait_notfy().testLock();
		
	}
	public  void testLock(){
	
	  for(int i=0;i<10;i++){
		  Task task=new Task(lock);
		  Thread t=new Thread(task);
		  t.start();
		  Task task1=new Task(lockClass);
		  Thread t1=new Thread(task1);
		  t1.start();
	  }
		
	}
  class Task implements Runnable{
        private Object obj=null;
        public Task(Object o){
        	this.obj=o;
        }
		public void run() {
			synchronized(obj){
				System.out.println(Thread.currentThread()+"获取 锁对象 ");
			try {
			    Thread.sleep(60*1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		System.out.println("释放锁 执行完毕");
	}
	  
  }
	/**
	 * 测试 wait and notify
	 */
    public  static void testWaitAndNotify(){
		new Thread(new Runnable(){

			public void run() {
				synchronized(lock){
					System.out.println(" 线程1    获取的lock 锁");
					try {
						lock.wait();
//						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
				}
				System.out.println(" 线程1    释放 锁 执行完毕");
				
			}

			
			
		}).start();
		new Thread(new Runnable(){

			public void run() {
				synchronized(lock){
					System.out.println(" 线程2     获取的lock 锁");
					try {
						lock.wait();
//						Thread.sleep(500);
					} catch (Exception e) {	
						e.printStackTrace();
					}
					
				}
				System.out.println(" 线程2      释放 锁 执行完毕");
			
			}
			
		}).start();
		new Thread(new Runnable(){

			public void run() {
				synchronized(lock){
					System.out.println(" 线程3     获取的lock 锁");
					try {

					    lock.notifyAll();
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
				System.out.println(" 线程3      释放 锁 执行完毕");
			}
			
		}).start();
   }
}
