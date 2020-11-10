/**
 * Author : czy
 * Date : 2019年6月23日 上午8:47:37
 * Title : com.riozenc.cfs.webapp.mrm.e.domain.PriceStandardDomain.java
 *
**/
package org.fms.cfs.common.webapp.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.riozenc.titanTool.annotation.TablePrimaryKey;
import com.riozenc.titanTool.mybatis.MybatisEntity;

/**
 * 执行电价
 * 
 * @author czy
 *
 */
public class PriceExecutionDomain implements MybatisEntity {

	@TablePrimaryKey
	private Long id;// ID ID bigint TRUE FALSE TRUE
	private Long priceTypeId;// 电价类型ID PRICE_TYPE_ID bigint FALSE FALSE FALSE
	private Long priceItemId;// 电价项目ID PRICE_ITEM_ID bigint FALSE FALSE FALSE
	private Long priceVersionId;// 版本号 VERSION_ID bigint FALSE FALSE FALSE
	private Byte timeSeg;// 时段 TIME_SEG smallint FALSE FALSE FALSE
	private BigDecimal price;// 电价 PRICE decimal(9,7) 9 7 FALSE FALSE FALSE
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createDate;// 创建日期 CREATE_DATE datetime FALSE FALSE FALSE
	private String operator;// 操作人 OPERATOR varchar(32) 32 FALSE FALSE FALSE
	private Byte status;// 状态 STATUS smallint FALSE FALSE FALSE

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPriceTypeId() {
		return priceTypeId;
	}

	public void setPriceTypeId(Long priceTypeId) {
		this.priceTypeId = priceTypeId;
	}

	public Long getPriceItemId() {
		return priceItemId;
	}

	public void setPriceItemId(Long priceItemId) {
		this.priceItemId = priceItemId;
	}

	public Long getPriceVersionId() {
		return priceVersionId;
	}

	public void setPriceVersionId(Long priceVersionId) {
		this.priceVersionId = priceVersionId;
	}

	public Byte getTimeSeg() {
		return timeSeg;
	}

	public void setTimeSeg(Byte timeSeg) {
		this.timeSeg = timeSeg;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public String getKey() {
		return this.getPriceTypeId() + "#" + this.getPriceItemId() + "#" + this.getTimeSeg();
	}
	public String getKeyByTimeSeg() {
		return this.getPriceTypeId() + "#"  + this.getTimeSeg();
	}

}
