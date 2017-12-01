package com.onemt.agent.log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.onemt.agent.vo.TraceVo;

public class LogOutput {
	
	private static final ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(10);

	private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	
	public static void output(final TraceVo traceVo){
		System.out.println(gson.toJson(traceVo));
	/*	newFixedThreadPool.execute(()->{
		});*/
	}

}
