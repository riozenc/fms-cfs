/**
 * Author : czy
 * Date : 2019年11月29日 下午2:55:31
 * Title : com.riozenc.cfs.common.CalculationUtils.java
 *
**/
package org.fms.cfs.common.utils;

import java.math.BigDecimal;

public class CalculationUtils {

	public static BigDecimal divide(BigDecimal dividend, BigDecimal divisor) {
		return divide(dividend, divisor, 2);
	}

	public static BigDecimal divide(BigDecimal dividend, BigDecimal divisor, int scale) {
		if (divisor == null || divisor.compareTo(BigDecimal.ZERO) == 0) {
			return BigDecimal.ZERO;
		}
		return dividend.divide(divisor, scale, BigDecimal.ROUND_HALF_UP);
	}

	public static BigDecimal multiply(BigDecimal multiplier, BigDecimal multiplicand) {
		return multiply(multiplier, multiplicand, 2);
	}

	public static BigDecimal multiply(BigDecimal multiplier, BigDecimal multiplicand, int scale) {
		if(scale==-1){
			return multiplier.multiply(multiplicand);
		}else{
			return multiplier.multiply(multiplicand).setScale(scale, BigDecimal.ROUND_HALF_UP);
		}
	}




}
