package com.onemt.agent.util;

public class ThreadLocalUtils {
	
	private static ThreadLocal<Integer> threadLocal = new ThreadLocal<Integer>(){
		@Override
		protected Integer initialValue() {
			return 0;
		}
		
	};
	
	public static Integer get(){
		return threadLocal.get();
	}
	
	public static void set(){
		threadLocal.set(get()+1);
	}

}
