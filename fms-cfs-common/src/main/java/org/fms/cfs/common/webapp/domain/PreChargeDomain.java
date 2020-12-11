/**
 * Author : czy
 * Date : 2019年12月30日 下午5:03:46
 * Title : com.riozenc.cfs.webapp.dsm.domain.PreChargeDomain.java
 *
**/
package org.fms.cfs.common.webapp.domain;

import java.math.BigDecimal;

public class PreChargeDomain {
	private Long id;
	private Long settlementId;
	private BigDecimal balance;
	private Byte status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSettlementId() {
		return settlementId;
	}

	public void setSettlementId(Long settlementId) {
		this.settlementId = settlementId;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

}
