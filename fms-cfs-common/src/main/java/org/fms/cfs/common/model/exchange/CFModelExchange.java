/**
 * Author : czy
 * Date : 2019年6月13日 下午3:42:22
 * Title : com.riozenc.cfs.webapp.cfm.model.exchange.CFModelExchange.java
 *
**/
package org.fms.cfs.common.model.exchange;

import java.util.Map;

import org.fms.cfs.common.model.CFModel;

public interface CFModelExchange<T extends CFModel> {

	Integer getDate();// 获取计算日期

	public Byte getSn();// 获取计算次数

	Map<Long, T> getModels();/// 获取计算数据

	public StringBuilder writeComputeLog(String log);// 写入计算记录

	public String getComputeLog();// 获取计算记录

	public String getCallbackUrl();
	
	public String getOperator();

}
