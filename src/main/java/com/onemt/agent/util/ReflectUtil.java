package com.onemt.agent.util;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
public class ReflectUtil {
 
 private static final Map<String, List<String>> PARAM_MAP = new HashMap<String, List<String>>();
 
 private static final ReadWriteLock RWL = new ReentrantReadWriteLock();
 private static final ClassPool CLASS_POOL = ClassPool.getDefault();  
 
 public static void test(String a,String bname){
	 
 }
 
 public static void main(String[] args) {
	 List<String> paramNameList = getParamNameList(ReflectUtil.class,"test");
	 
	 for (String string : paramNameList) {
		System.out.println(string);
	}
}
 
 
 /**
  * 拿到方法的参数名称
  * 比如get(long id, String name)
  * 获得id, name
  */
 public static List<String> getParamNameList(Class<?> clazz, String methodName) {
  RWL.readLock().lock();
  List<String> paramNameList = PARAM_MAP.get(clazz.getName()+"-"+methodName);
  try {
   if(paramNameList == null || paramNameList.size() <= 0) {
    try {
     RWL.readLock().unlock();
     RWL.writeLock().lock();
     if(paramNameList == null || paramNameList.size() <= 0) {
      try {  
       paramNameList = new ArrayList<String>();
          CtClass cc = CLASS_POOL.get(clazz.getName());  
          CtMethod cm = cc.getDeclaredMethod(methodName);  
       
          // 使用javaassist的反射方法获取方法的参数名  
          MethodInfo methodInfo = cm.getMethodInfo();  
          CodeAttribute codeAttribute = methodInfo.getCodeAttribute();  
          LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);  
          if (attr != null) {  
           int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;  
           for (int i = 0; i < cm.getParameterTypes().length; i++) {
               paramNameList.add(cm.getParameterTypes()[i].getName()+"--"+attr.variableName(i + pos));
           }
          }  
      } catch (NotFoundException e) {  
          e.printStackTrace();  
      }  
      
      PARAM_MAP.put(clazz.getName()+"-"+methodName, paramNameList);
     }
    } finally {
     RWL.writeLock().unlock();
     RWL.readLock().lock();
    }
   }
   
  } finally {
   RWL.readLock().unlock();
  }
  
  return paramNameList;
 }
 
}