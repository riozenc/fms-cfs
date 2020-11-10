/**
 * Author : czy
 * Date : 2019年7月31日 下午5:09:03
 * Title : com.riozenc.cfs.webapp.cfm.model.ECFModelExchange.java
 *
**/
package org.fms.cfs.common.model.exchange;

import java.util.Map;

import org.fms.cfs.common.model.ECFModel;

public class ECFModelExchange implements CFModelExchange<ECFModel> {

	private StringBuilder computeLog;// 计算记录
	private Integer date;
	private Byte sn;
	private Map<Long, ECFModel> models;
	private String callback;
	private String operator;

	public ECFModelExchange(Integer date, Byte sn, Map<Long, ECFModel> models, String callback,String operator) {
		this.date = date;
		this.sn = sn;
		this.models = models;
		this.computeLog = new StringBuilder();
		this.callback = callback;
		this.operator = operator;
	}

	@Override
	public Integer getDate() {
		return this.date;
	}

	@Override
	public Map<Long, ECFModel> getModels() {
		return this.models;
	}

	@Override
	public Byte getSn() {
		return this.sn;
	}

	public StringBuilder writeComputeLog(String log) {
		return computeLog.append(log);
	}

	public String getComputeLog() {
		return computeLog.toString();
	}

	@Override
	public String getCallbackUrl() {
		return this.callback;
	}

	@Override
	public String getOperator() {
		return this.operator;
	}

}
