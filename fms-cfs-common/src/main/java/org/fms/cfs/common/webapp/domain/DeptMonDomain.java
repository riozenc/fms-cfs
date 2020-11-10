/**
 * Author : czy
 * Date : 2019年6月25日 下午3:37:56
 * Title : com.riozenc.cfs.webapp.mrm.e.domain.DeptMonDomain.java
 *
**/
package org.fms.cfs.common.webapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.riozenc.titanTool.annotation.TablePrimaryKey;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DeptMonDomain {
	@TablePrimaryKey
	private Integer id;

	private Integer deptId;

	private String mon;

	private String remark;

	private Byte status;

	private String title;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDeptId() {
		return deptId;
	}

	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}

	public String getMon() {
		return mon;
	}

	public void setMon(String mon) {
		this.mon = mon;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
