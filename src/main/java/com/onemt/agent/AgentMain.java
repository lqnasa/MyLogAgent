package com.onemt.agent;

import java.lang.instrument.Instrumentation;

public class AgentMain {

	public static void premain(String args, Instrumentation inst) throws Exception {
		System.out.println("Loading Agent..");
		inst.addTransformer(new MyTransformer());
	}

}
