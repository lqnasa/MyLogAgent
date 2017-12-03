package com.onemt.agent.vo;

import java.util.List;
import java.util.Map;

public class TraceVo{

	private String hostIp; // 192.168.4.1
	private String threadName;
	private String instanceName; // TracingDemo
	private Boolean isEntry; // true
	private Integer errCode; // 0
	private String traceId; // UUID "e5549498-60f3-4870-8483-fe26f6d0367b",
	private String spanId; // "3cbfe7f0-141c-4597-8b15-38d2fb145e01",
	private String parentId; // "16a52a9f-e697-45ce-92fb-7395339eae4b",
	private String className; // "dao.impl.ProductDaoImpl"
	private String methodName;// "queryProduct",
	private List<Map<String,String>> inParams; // 参数值
	private Map<String,String> retVal; //返回值
	private String errorType; //异常类型
	private String errorMessage;  //异常信息
	private Long createTime; // 1448442004537,
	private Long returnTime; // 1448442004537,
	private Long callTime; // 5

	/**
	 * 
	 */
	public TraceVo() {
	}

	public TraceVo(String hostIp, String threadName, String instanceName, Boolean isEntry, Integer errCode,
			String traceId, String spanId, String parentId, String className, String methodName,
			List<Map<String, String>> inParams, Map<String, String> retVal, String errorType, String errorMessage,
			Long createTime, Long returnTime, Long callTime) {
		this.hostIp = hostIp;
		this.threadName = threadName;
		this.instanceName = instanceName;
		this.isEntry = isEntry;
		this.errCode = errCode;
		this.traceId = traceId;
		this.spanId = spanId;
		this.parentId = parentId;
		this.className = className;
		this.methodName = methodName;
		this.inParams = inParams;
		this.retVal = retVal;
		this.errorType = errorType;
		this.errorMessage = errorMessage;
		this.createTime = createTime;
		this.returnTime = returnTime;
		this.callTime = callTime;
	}

	public String getHostIp() {
		return hostIp;
	}

	public void setHostIp(String hostIp) {
		this.hostIp = hostIp;
	}

	public String getThreadName() {
		return threadName;
	}

	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}

	public String getInstanceName() {
		return instanceName;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

	public Boolean getIsEntry() {
		return isEntry;
	}

	public void setIsEntry(Boolean isEntry) {
		this.isEntry = isEntry;
	}

	public Integer getErrCode() {
		return errCode;
	}

	public void setErrCode(Integer errCode) {
		this.errCode = errCode;
	}

	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}

	public String getSpanId() {
		return spanId;
	}

	public void setSpanId(String spanId) {
		this.spanId = spanId;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public List<Map<String, String>> getInParams() {
		return inParams;
	}

	public void setInParams(List<Map<String, String>> inParams) {
		this.inParams = inParams;
	}

	public Map<String, String> getRetVal() {
		return retVal;
	}

	public void setRetVal(Map<String, String> retVal) {
		this.retVal = retVal;
	}

	public String getErrorType() {
		return errorType;
	}

	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getReturnTime() {
		return returnTime;
	}

	public void setReturnTime(Long returnTime) {
		this.returnTime = returnTime;
	}

	public Long getCallTime() {
		return callTime;
	}

	public void setCallTime(Long callTime) {
		this.callTime = callTime;
	}
	
}
