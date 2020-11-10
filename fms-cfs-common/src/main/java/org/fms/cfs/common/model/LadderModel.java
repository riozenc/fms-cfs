/**
 * Author : czy
 * Date : 2020年2月21日 下午3:51:40
 * Title : com.riozenc.cfs.webapp.cfm.model.LadderModel.java
 *
**/
package org.fms.cfs.common.model;

import java.math.BigDecimal;

/**
 * 阶梯模型
 * 
 * @author czy
 *
 */
public class LadderModel {
	private Integer ladderSn; // 阶梯
	private BigDecimal ladderValue; // 阶梯值
	private BigDecimal price;// 电价
	private BigDecimal chargePower;// 量
	private BigDecimal amount;// 费

	public LadderModel(int ladder, BigDecimal power, BigDecimal ladderPrice) {
		this.ladderSn = ladder;
		this.chargePower = power;
		this.price = ladderPrice;
		this.amount = this.chargePower.multiply(this.price).setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	public Integer getLadderSn() {
		return ladderSn;
	}

	public BigDecimal getLadderValue() {
		return ladderValue;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public BigDecimal getChargePower() {
		return chargePower;
	}

	public BigDecimal getAmount() {
		return amount;
	}
	
	
	

}
