package com.onemt.agent.vo;

public class Trace {

	private String hostIp; // 192.168.4.1
	private String traceId; // UUID "e5549498-60f3-4870-8483-fe26f6d0367b",
	private String spanId; // "3cbfe7f0-141c-4597-8b15-38d2fb145e01",
	private String parentId; // "16a52a9f-e697-45ce-92fb-7395339eae4b",
	
	public Trace() {
	}
	public Trace(String hostIp, String traceId, String spanId, String parentId) {
		this.hostIp = hostIp;
		this.traceId = traceId;
		this.spanId = spanId;
		this.parentId = parentId;
	}
	public String getHostIp() {
		return hostIp;
	}
	public void setHostIp(String hostIp) {
		this.hostIp = hostIp;
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
	
	
	
}
