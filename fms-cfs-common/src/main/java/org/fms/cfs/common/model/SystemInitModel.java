/**
 * Author : czy
 * Date : 2019年6月26日 下午2:15:24
 * Title : com.riozenc.cfs.webapp.mrm.model.SystemInitModel.java
 *
**/
package org.fms.cfs.common.model;

import java.util.List;

import org.fms.cfs.common.webapp.domain.DeptMonDomain;
import org.fms.cfs.common.webapp.domain.PriceExecutionDomain;
import org.fms.cfs.common.webapp.domain.WriteSectDomain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SystemInitModel extends InitModel {

	private List<Integer> deptId;
	private String date;
	private List<WriteSectDomain> writeSectDomains;
	private List<PriceExecutionDomain> priceExecutionDomains;
	private List<DeptMonDomain> deptMonDomains;
	private String errorMessage;

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("抄表区段：").append(writeSectDomains.size());
		stringBuilder.append("电价：").append(priceExecutionDomains.size());
		stringBuilder.append("营业区域电费月份：").append(deptMonDomains.size());
		return stringBuilder.toString();
	}

	public List<WriteSectDomain> getWriteSectDomains() {
		return writeSectDomains;
	}

	public void setWriteSectDomains(List<WriteSectDomain> writeSectDomains) {
		this.writeSectDomains = writeSectDomains;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {

		this.errorMessage = this.errorMessage + " " + errorMessage;
	}

	public List<PriceExecutionDomain> getPriceExecutionDomains() {
		return priceExecutionDomains;
	}

	public void setPriceExecutionDomains(List<PriceExecutionDomain> priceExecutionDomains) {
		this.priceExecutionDomains = priceExecutionDomains;
	}

	public List<DeptMonDomain> getDeptMonDomains() {
		return deptMonDomains;
	}

	public void setDeptMonDomains(List<DeptMonDomain> deptMonDomains) {
		this.deptMonDomains = deptMonDomains;
	}

	public List<Integer> getDeptId() {
		return deptId;
	}

	public void setDeptId(List<Integer> deptId) {
		this.deptId = deptId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
