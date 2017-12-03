package com.onemt.agent.log;

import java.lang.reflect.Type;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.onemt.agent.vo.TraceVo;

public class LogOutput {
	
	public String a;
	
	private static final ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(10);

	private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	
	public static void output(final TraceVo traceVo){
		System.out.println(gson.toJson(traceVo));
	/*	newFixedThreadPool.execute(()->{
		});*/
	}
	
	public static String toJson(Object object){
		return gson.toJson(object);
	}
	
	public static String toJson(Object object,Type type){
		
		if(object instanceof Class){
			return type.getTypeName();
		}
		
		String str = "";
		try {
			str = gson.toJson(object, type);
		} catch (Exception e) {
			str ="gson cannot to json for this :"+object;
		}
		return str;
	}

}
