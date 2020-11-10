/**
 *    Auth:riozenc
 *    Date:2019年3月18日 上午9:49:40
 *    Title:com.riozenc.cim.webapp.archives.domain.ManagerParamEntity.java
 **/
package org.fms.cfs.common.webapp.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.riozenc.titanTool.mybatis.pagination.Page;

public class ManagerParamEntity extends Page {

	@JsonProperty(access = Access.WRITE_ONLY)
	private String managerId;
	@JsonProperty(access = Access.WRITE_ONLY)
	private String roleIds;
	@JsonProperty(access = Access.WRITE_ONLY)
	private String deptIds;

	public String getManagerId() {
		return managerId;
	}

	public void setManagerId(String managerId) {
		this.managerId = managerId;
	}

	public String getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}

	public String getDeptIds() {
		return deptIds;
	}

	public void setDeptIds(String deptIds) {
		this.deptIds = deptIds;
	}

}