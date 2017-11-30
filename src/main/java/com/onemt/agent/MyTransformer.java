package com.onemt.agent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import com.onemt.agent.annotation.Agent;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;

public class MyTransformer implements ClassFileTransformer {

	final static String prefix = "\nlong startTime = System.currentTimeMillis();\n";
	final static String postfix = "\nlong endTime = System.currentTimeMillis();\n";

	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
		className = className.replace("/", ".");
		CtClass ctclass = null;
		try {
			ClassPool pool = ClassPool.getDefault();
			//ctclass = ClassPool.getDefault().get(className);// 使用全称,用于取得字节码类<使用javassist>
			ctclass = pool.makeClass(new java.io.ByteArrayInputStream(classfileBuffer));  
			if (ctclass.hasAnnotation(Agent.class)) {
				CtMethod[] declaredMethods = ctclass.getDeclaredMethods();
				for (CtMethod ctMethod : declaredMethods) {
					String methodName = ctMethod.getName();
					String outputStr = "\nSystem.out.println(\"		|---this method " + methodName
							+ " cost:\" +(endTime - startTime) +\"ms.\");";

					String newMethodName = methodName + "$old";// 新定义一个方法叫做比如sayHello$old
					ctMethod.setName(newMethodName);// 将原来的方法名字修改

					// 创建新的方法，复制原来的方法，名字为原来的名字
					CtMethod newMethod = CtNewMethod.copy(ctMethod, methodName, ctclass, null);

					// 构建新的方法体
					StringBuilder bodyStr = new StringBuilder();
					bodyStr.append("{");
					bodyStr.append("\nSystem.out.println(\"|---调用链序号:\"+com.onemt.agent.util.ThreadLocalUtils.get()+\"---"+((Agent) ctclass.getAnnotation(Agent.class)).value()+"\");\ncom.onemt.agent.util.ThreadLocalUtils.set();");
					bodyStr.append(prefix);
					if (!"void".equals(ctMethod.getReturnType().getName())) {
						bodyStr.append("\n"+ctMethod.getReturnType().getName()+" retVal = ");
					}
					bodyStr.append(newMethodName + "($$);\n");// 调用原有代码，类似于method();($$)表示所有的参数
					bodyStr.append(postfix);
					bodyStr.append(outputStr);
					if(!"void".equals(ctMethod.getReturnType().getName())){
						bodyStr.append("\nreturn retVal;");
					}
					bodyStr.append("\n}");
					newMethod.setBody(bodyStr.toString());// 替换新方法
					ctclass.addMethod(newMethod);// 增加新方法
					return ctclass.toBytecode();
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
		}

		return null;
	}
}
