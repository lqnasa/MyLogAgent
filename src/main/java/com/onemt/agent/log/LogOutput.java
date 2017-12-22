package com.onemt.agent.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.onemt.agent.vo.TraceVo;

public class LogOutput {

	//private static final Logger logger = LoggerFactory.getLogger(LogOutput.class);

	private static final ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(10);

	//private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

	public static void output(final TraceVo traceVo) {
		newFixedThreadPool.execute(() -> {

			/*List<Map<String, Object>> inParams = traceVo.getInParams();
			Map<String, Object> retVal = traceVo.getRetVal();
			Object errorMessage = traceVo.getErrorMessage();

			if (inParams != null) {
				inParams.forEach(param -> {
					param.forEach((k, v) -> {
						param.replace(k, toJson(v));
					});
				});
			}

			if (retVal != null) {
				retVal.forEach((k, v) -> {
					retVal.replace(k, toJson(v));
				});
			}
			if(errorMessage!=null){
				traceVo.setErrorMessage(printStackTraceToString((Throwable)errorMessage));
			}*/
			
			System.out.println("====================toString================="+traceVo);
			//logger.info(gson.toJson(traceVo));
		});

	}

	public static Object toJson(Object object) {
		if (object instanceof String || object instanceof Integer || object instanceof Boolean || object instanceof Long
				|| object instanceof Boolean || object instanceof Float || object instanceof Double) {
			return object;
		} else {
			return ""+object;
		}

	}

	public static String printStackTraceToString(Throwable t) {
		StringWriter sw = new StringWriter();
		t.printStackTrace(new PrintWriter(sw, true));
		return sw.getBuffer().toString();
	}

}
