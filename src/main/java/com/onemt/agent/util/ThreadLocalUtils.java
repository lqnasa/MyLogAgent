package com.onemt.agent.util;

import java.util.UUID;

import com.onemt.agent.vo.TraceVo;

public class ThreadLocalUtils {
	
	private static ThreadLocal<TraceVo> threadLocal = new ThreadLocal<TraceVo>(){
		@Override
		protected TraceVo initialValue() {
			TraceVo traceVo = new TraceVo();
			traceVo.setHostIp(InetAddressUtils.getHostAddress());
			//traceVo.setCreateTime(Instant.now().getEpochSecond());
			traceVo.setErrCode(0);
			traceVo.setIsEntry(true);
			traceVo.setTraceId(UUID.randomUUID().toString());
			traceVo.setSpanId(UUID.randomUUID().toString());
			
			return traceVo;
		}
		
	};
	
	public static TraceVo get(){
		return threadLocal.get();
	}
	
	public static void set(TraceVo traceVo){
			threadLocal.set(traceVo);
	}

}
