package com.onemt.agent.util;

import java.util.UUID;

import com.onemt.agent.vo.Trace;

public class ThreadLocalUtils {
	
	private static ThreadLocal<Trace> threadLocal = new InheritableThreadLocal<Trace>(){
		@Override
		protected Trace initialValue() {
			Trace trace = new Trace();
			trace.setHostIp(InetAddressUtils.getHostAddress());
			trace.setTraceId(UUID.randomUUID().toString());
			return trace;
		}
	};
	
	public static Trace get(){
		return threadLocal.get();
	}
	
	public static void set(Trace trace){
			threadLocal.set(trace);
	}

}
