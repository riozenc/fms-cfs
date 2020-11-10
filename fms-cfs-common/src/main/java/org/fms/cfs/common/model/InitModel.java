/**
 * Author : czy
 * Date : 2019年6月26日 上午10:11:41
 * Title : com.riozenc.cfs.webapp.mrm.model.InitModel.java
 *
**/
package org.fms.cfs.common.model;

public class InitModel {

	private StringBuilder executeResult = new StringBuilder();

	public String getExecuteResult() {
		return this.executeResult.toString();
	}

	public void addExecuteResult(String executeResult) {
		this.executeResult.append(executeResult).append("\n");
	}

}
