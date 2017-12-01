package com.onemt.agent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import org.apache.commons.lang3.StringUtils;

import com.onemt.agent.annotation.TraceClass;
import com.onemt.agent.annotation.TraceMethod;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;

public class MyTransformer implements ClassFileTransformer {

	final static String prefix = "long startTime = System.currentTimeMillis();";
	final static String postfix = "long endTime = System.currentTimeMillis();";

	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
		className = className.replace("/", ".");
		CtClass ctclass = null;
		try {
			ClassPool pool = ClassPool.getDefault();
			//ctclass = ClassPool.getDefault().get(className);// 使用全称,用于取得字节码类<使用javassist>
			ctclass = pool.makeClass(new java.io.ByteArrayInputStream(classfileBuffer));
			
			if (ctclass.hasAnnotation(TraceClass.class)) {
				CtMethod[] declaredMethods = ctclass.getDeclaredMethods();
				for (CtMethod ctMethod : declaredMethods) {
					if(ctMethod.hasAnnotation(TraceMethod.class)){
						//TraceMethod annotation = (TraceMethod) ctMethod.getAnnotation(TraceMethod.class);
						
						//Boolean isEntry =StringUtils.isNotBlank(annotation.methodDescription());
						
						String methodName = ctMethod.getName();
						
						/*CtClass[] parameterTypes = ctMethod.getParameterTypes();
						int length = parameterTypes!=null?parameterTypes.length:0;*/
						String returnName = ctMethod.getReturnType().getName();
						String newMethodName = methodName + "$old";// 新定义一个方法叫做比如sayHello$old
						ctMethod.setName(newMethodName);// 将原来的方法名字修改
						// 创建新的方法，复制原来的方法，名字为原来的名字
						CtMethod newMethod = CtNewMethod.copy(ctMethod, methodName, ctclass, null);
						
						// 构建新的方法体
						StringBuilder bodyStr = new StringBuilder();
						bodyStr.append("{")
						//.append("\nLong startTime = System.currentTimeMillis();")
						.append("\ncom.onemt.agent.vo.TraceVo traceVo = com.onemt.agent.util.ThreadLocalUtils.get();")
						//.append("\ntraceVo.setCreateTime(startTime);")
						.append("\ntraceVo.setClassName(\""+className+"\");")
						.append("\ntraceVo.setMethodName(\""+methodName+"\");");
					/*	if(length>0){
							bodyStr.append("List<Map<String,String>> inParams = new ArrayList<Map<String,String>>();");
							for (int i=0;i<length;i++) {
								bodyStr.append("Map<String,String> inParam"+i+" = new HashMap<String, String>();")
								.append("inParam"+i+".put(\""+parameterTypes[i].getName()+"\", $"+i+");")
								.append("inParams.add(inParam"+i+");");
							}
							bodyStr.append("traceVo.setInParams(inParams);");
						}*/
						bodyStr.append("\ntraceVo.setParentId(traceVo.getSpanId());")
						.append("\ntraceVo.setSpanId(java.util.UUID.randomUUID().toString());")
						//.append("traceVo.setIsEntry("+isEntry+");")
						;
						if (!"void".equals(returnName)) {
								bodyStr.append("\n"+returnName+" retVal = null; ");
							}
						bodyStr.append("\ntry {\n");
						if (!"void".equals(returnName)) {
							bodyStr.append("retVal = ");
						}
						bodyStr.append(newMethodName + "($$);")
						/*.append("} catch (Throwable $e) {")
						//.append("traceVo.setErrCode(1);")
						//.append("traceVo.setErrorMessage($e.getMessage());")
						.append("throw $e;")*/
						.append("\n}finally{");
						//.append("\nLong endTime = System.currentTimeMillis();")
						//.append("\ntraceVo.setReturnTime(endTime);");
						//.append("traceVo.setCallTime((endTime-startTime));");
						if (!"void".equals(returnName)) {
							//bodyStr.append("traceVo.setRetVal(retVal);");
						}
						bodyStr.append("\ncom.onemt.agent.log.LogOutput.output(traceVo);")
						.append("\ncom.onemt.agent.util.ThreadLocalUtils.set(traceVo);")
						.append("\n}");
						if (!"void".equals(returnName)) {
							bodyStr.append("\nreturn retVal;");
						}	
						bodyStr.append("\n}");
						System.out.println(bodyStr.toString());
						
						newMethod.setBody(bodyStr.toString());// 替换新方法
						ctclass.addMethod(newMethod);// 增加新方法
						return ctclass.toBytecode();
					}
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
		}
		
		return null;
	}
	
	
    
    /*// 获取方法的参数  
    MethodInfo methodInfo = method.getMethodInfo();  
    CodeAttribute codeAttribute = methodInfo.getCodeAttribute();  
    LocalVariableAttribute localVariableAttribute = (LocalVariableAttribute)codeAttribute.getAttribute(LocalVariableAttribute.tag);  
      
    for (int i = 0; i < method.getParameterTypes().length; i++) {  
        System.out.println("第" + (i + 1) + "个参数名称为: " + localVariableAttribute.variableName(staticIndex + i));  
    }  */
}
