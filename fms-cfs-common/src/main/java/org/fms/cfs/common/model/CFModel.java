/**
 * Author : czy
 * Date : 2019年4月16日 上午9:48:49
 * Title : com.riozenc.cfs.webapp.cfm.model.CFModel.java
 *
**/
package org.fms.cfs.common.model;

import java.math.BigDecimal;

public abstract class CFModel {

	protected String mon;

	
	//量
	

	//价
	protected BigDecimal price;
	
	//金额
	protected BigDecimal amount = BigDecimal.ZERO;// 总费用
}
