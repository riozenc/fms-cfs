/**
 * Author : chizf
 * Date : 2020年6月23日 下午3:52:12
 * Title : com.riozenc.cfs.webapp.mrm.e.domain.TransformerLoadDomain.java
 *
**/
package org.fms.cfs.common.webapp.domain;

import java.math.BigDecimal;

/**
 * 变压器负荷表
 * @author czy
 *
 */
public class TransformerLoadParamDomain {
	private BigDecimal capacity;// 变压器容量
	private BigDecimal load1;
	private BigDecimal load2;
	private BigDecimal load3;

	public BigDecimal getCapacity() {
		return capacity;
	}

	public void setCapacity(BigDecimal capacity) {
		this.capacity = capacity;
	}

	public BigDecimal getLoad1() {
		return load1;
	}

	public void setLoad1(BigDecimal load1) {
		this.load1 = load1;
	}

	public BigDecimal getLoad2() {
		return load2;
	}

	public void setLoad2(BigDecimal load2) {
		this.load2 = load2;
	}

	public BigDecimal getLoad3() {
		return load3;
	}

	public void setLoad3(BigDecimal load3) {
		this.load3 = load3;
	}

}
