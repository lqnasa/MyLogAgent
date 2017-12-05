package com.onemt.agent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import com.onemt.agent.annotation.TraceClass;
import com.onemt.agent.annotation.TraceMethod;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.Modifier;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

public class MyTransformer implements ClassFileTransformer {

	final static String prefix = "long startTime = System.currentTimeMillis();";
	final static String postfix = "long endTime = System.currentTimeMillis();";

	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
		className = className.replace("/", ".");
		CtClass ctclass = null;
		try {
			if(!className.contains("com")){
				return null;
			}
			System.out.println("===============MyTransformer className============"+className);
			System.out.println("===============MyTransformer classfileBuffer============"+classfileBuffer);
			ClassPool pool =ClassPool.getDefault();
			//ctclass = ClassPool.getDefault().get(className);// 使用全称,用于取得字节码类<使用javassist>
			ctclass = pool.makeClass(new java.io.ByteArrayInputStream(classfileBuffer));
			System.out.println("===============MyTransformer classfileBuffer className============"+ctclass.getName());
			
			
			if (ctclass.hasAnnotation(TraceClass.class)) {
				CtMethod[] declaredMethods = ctclass.getDeclaredMethods();
				for (CtMethod ctMethod : declaredMethods) {
					if(ctMethod.hasAnnotation(TraceMethod.class)){
						
						System.out.println("===============MyTransformer classfileBuffer hasAnnotation============");
						
						Object annotation = ctMethod.getAnnotation(TraceMethod.class);
						
						System.out.println("===============MyTransformer classfileBuffer annotation============"+annotation);
						
						boolean isEntry = true;
						String methodName = ctMethod.getName();
						CtClass[] parameterTypes = ctMethod.getParameterTypes();
						int length = parameterTypes!=null?parameterTypes.length:0;
						
						MethodInfo methodInfo = ctMethod.getMethodInfo();
						CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
						LocalVariableAttribute attribute = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
						int pos = Modifier.isStatic(ctMethod.getModifiers()) ? 0 : 1;  
						
						String returnName = ctMethod.getReturnType().getName();
						
						System.out.println("===============MyTransformer classfileBuffer returnName============"+returnName);
						
						String newMethodName = methodName + "$old";// 新定义一个方法叫做比如sayHello$old
						ctMethod.setName(newMethodName);// 将原来的方法名字修改
						// 创建新的方法，复制原来的方法，名字为原来的名字
						CtMethod newMethod = CtNewMethod.copy(ctMethod, methodName, ctclass, null);
						// 构建新的方法体.
						StringBuilder bodyStr = new StringBuilder();
						
						bodyStr.append("{")
						.append("\ncom.onemt.agent.vo.Trace trace = com.onemt.agent.util.ThreadLocalUtils.get();")
						.append("\ncom.onemt.agent.vo.TraceVo traceVo = new com.onemt.agent.vo.TraceVo();")
						.append("\ntraceVo.setHostIp(trace.getHostIp());")
						.append("\ntraceVo.setTraceId(trace.getTraceId());")
						.append("\ntraceVo.setClassName(\""+className+"\");")
						.append("\ntraceVo.setMethodName(\""+ctMethod.getName()+"\");")
						.append("\ntraceVo.setThreadName(Thread.currentThread().getName());");
						if(length>0){
							bodyStr.append("\njava.util.List inParams = new java.util.ArrayList();");
							for (int i=0;i<length;i++) {
								bodyStr.append("\njava.util.Map inParam"+i+" = new java.util.HashMap();")
								//parameterTypes[i].getName()
								.append("\ninParam"+i+".put(\""+attribute.variableName(i+pos)+"\", $"+(i+1)+");")
								.append("\ninParams.add(inParam"+i+");");
							}
							bodyStr.append("\ntraceVo.setInParams(inParams);");
						}
						bodyStr.append("\ntraceVo.setParentId(trace.getSpanId());")
						.append("\nString spanId = java.util.UUID.randomUUID().toString();")
						.append("\ntraceVo.setSpanId(spanId);")
						.append("\ntrace.setSpanId(spanId);")
						.append("\ntrace.setParentId(traceVo.getSpanId());")
						.append("\ntraceVo.setIsEntry(Boolean.valueOf("+isEntry+"));")
						;
						if (!"void".equals(returnName)) {
								bodyStr.append("\n"+returnName+" retVal = null; ");
							}
						bodyStr.append("\nlong startTime = System.currentTimeMillis();")
						.append("\ntry {\n");
						if (!"void".equals(returnName)) {
							bodyStr.append("retVal = ");
						}
						bodyStr.append(newMethodName + "($$);")
						.append("\n} catch (Throwable e) {")
						.append("\ntraceVo.setErrCode(Integer.valueOf(1));")
						.append("\ntraceVo.setErrorMessage(e);")
						.append("\nthrow e;")
						.append("\n}finally{")
						.append("\nlong endTime = System.currentTimeMillis();")
						.append("\ntraceVo.setCreateTime(Long.valueOf(startTime));")
						.append("\ntraceVo.setReturnTime(Long.valueOf(endTime));")
						.append("\ntraceVo.setCallTime(Long.valueOf(endTime-startTime));");
						if (!"void".equals(returnName)) {
							bodyStr.append("\njava.util.Map returnOjbect = new java.util.HashMap();")
							.append("\nreturnOjbect.put(\""+returnName+"\",retVal);")
							.append("\ntraceVo.setRetVal(returnOjbect);");
						}
						bodyStr.append("\ncom.onemt.agent.log.LogOutput.output(traceVo);")
						.append("\ncom.onemt.agent.util.ThreadLocalUtils.set(trace);")
						.append("\n}");
						
						if (!"void".equals(returnName)) {
							bodyStr.append("\nreturn retVal;");
						}	
						bodyStr.append("\n}");
						System.out.println(bodyStr.toString());
						
						newMethod.setBody(bodyStr.toString());// 替换新方法
						ctclass.addMethod(newMethod);// 增加新方法
					}
				}
				return ctclass.toBytecode();
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
