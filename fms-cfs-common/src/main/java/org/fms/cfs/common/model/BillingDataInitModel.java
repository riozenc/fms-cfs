/**
 * Author : czy
 * Date : 2019年6月26日 下午3:12:09
 * Title : com.riozenc.cfs.webapp.mrm.model.BillingDataInitModel.java
 *
**/
package org.fms.cfs.common.model;

import java.util.List;

import org.fms.cfs.common.webapp.domain.MeterDomain;
import org.fms.cfs.common.webapp.domain.MeterInductorAssetsRelDomain;
import org.fms.cfs.common.webapp.domain.MeterMeterAssetsRelDomain;
import org.fms.cfs.common.webapp.domain.MeterRelationDomain;
import org.fms.cfs.common.webapp.domain.MeterReplaceDomain;
import org.fms.cfs.common.webapp.domain.SettlementMeterRelDomain;
import org.fms.cfs.common.webapp.domain.TransformerDomain;
import org.fms.cfs.common.webapp.domain.TransformerLoadParamDomain;
import org.fms.cfs.common.webapp.domain.TransformerLossFormulaParamDomain;
import org.fms.cfs.common.webapp.domain.TransformerLossTableParamDomain;
import org.fms.cfs.common.webapp.domain.TransformerMeterRelationDomain;
import org.fms.cfs.common.webapp.domain.UserDomain;

public class BillingDataInitModel extends InitModel {
	private Byte sn;
	private List<UserDomain> userDomains;
	private List<MeterDomain> meterDomains;
	private List<MeterRelationDomain> meterRelationDomains;// 计量点关系
	private List<MeterReplaceDomain> meterReplaceDomains;// 计量点 换边记录
	private List<TransformerDomain> transformerDomains;// 变压器信息
	private List<TransformerMeterRelationDomain> transformerMeterRelationDomains;// 变压器与计量点关系
	private List<MeterMeterAssetsRelDomain> meterMeterAssetsRelDomains;// 计量点与电能表资产关系
	private List<MeterInductorAssetsRelDomain> meterInductorAssetsRelDomains;// 计量点与互感器资产关系
	private List<TransformerLossFormulaParamDomain> transformerLossFormulaParamDomains;
	private List<TransformerLossTableParamDomain> transformerLossTableParamDomains;
	private List<TransformerLoadParamDomain> transformerLoadParamDomains;
	private List<SettlementMeterRelDomain> settlementMeterRelDomains;

	private String date;
	private String errorMessage;

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = this.errorMessage + " " + errorMessage;
	}

	public List<UserDomain> getUserDomains() {
		return userDomains;
	}

	public void setUserDomains(List<UserDomain> userDomains) {
		this.userDomains = userDomains;
	}

	public List<MeterDomain> getMeterDomains() {
		return meterDomains;
	}

	public void setMeterDomains(List<MeterDomain> meterDomains) {
		this.meterDomains = meterDomains;
	}

	public List<MeterRelationDomain> getMeterRelationDomains() {
		return meterRelationDomains;
	}

	public void setMeterRelationDomains(List<MeterRelationDomain> meterRelationDomains) {
		this.meterRelationDomains = meterRelationDomains;
	}

	public List<MeterReplaceDomain> getMeterReplaceDomains() {
		return meterReplaceDomains;
	}

	public void setMeterReplaceDomains(List<MeterReplaceDomain> meterReplaceDomains) {
		this.meterReplaceDomains = meterReplaceDomains;
	}

	public List<TransformerDomain> getTransformerDomains() {
		return transformerDomains;
	}

	public void setTransformerDomains(List<TransformerDomain> transformerDomains) {
		this.transformerDomains = transformerDomains;
	}

	public List<TransformerMeterRelationDomain> getTransformerMeterRelationDomains() {
		return transformerMeterRelationDomains;
	}

	public void setTransformerMeterRelationDomains(
			List<TransformerMeterRelationDomain> transformerMeterRelationDomains) {
		this.transformerMeterRelationDomains = transformerMeterRelationDomains;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Byte getSn() {
		return sn;
	}

	public void setSn(Byte sn) {
		this.sn = sn;
	}

	public List<MeterMeterAssetsRelDomain> getMeterMeterAssetsRelDomains() {
		return meterMeterAssetsRelDomains;
	}

	public void setMeterMeterAssetsRelDomains(List<MeterMeterAssetsRelDomain> meterMeterAssetsRelDomains) {
		this.meterMeterAssetsRelDomains = meterMeterAssetsRelDomains;
	}

	public List<MeterInductorAssetsRelDomain> getMeterInductorAssetsRelDomains() {
		return meterInductorAssetsRelDomains;
	}

	public void setMeterInductorAssetsRelDomains(List<MeterInductorAssetsRelDomain> meterInductorAssetsRelDomains) {
		this.meterInductorAssetsRelDomains = meterInductorAssetsRelDomains;
	}

	public List<TransformerLossFormulaParamDomain> getTransformerLossFormulaParamDomains() {
		return transformerLossFormulaParamDomains;
	}

	public void setTransformerLossFormulaParamDomains(
			List<TransformerLossFormulaParamDomain> transformerLossFormulaParamDomains) {
		this.transformerLossFormulaParamDomains = transformerLossFormulaParamDomains;
	}

	public List<TransformerLossTableParamDomain> getTransformerLossTableParamDomains() {
		return transformerLossTableParamDomains;
	}

	public void setTransformerLossTableParamDomains(
			List<TransformerLossTableParamDomain> transformerLossTableParamDomains) {
		this.transformerLossTableParamDomains = transformerLossTableParamDomains;
	}

	public List<TransformerLoadParamDomain> getTransformerLoadParamDomains() {
		return transformerLoadParamDomains;
	}

	public void setTransformerLoadParamDomains(List<TransformerLoadParamDomain> transformerLoadParamDomains) {
		this.transformerLoadParamDomains = transformerLoadParamDomains;
	}

	public List<SettlementMeterRelDomain> getSettlementMeterRelDomains() {
		return settlementMeterRelDomains;
	}

	public void setSettlementMeterRelDomains(List<SettlementMeterRelDomain> settlementMeterRelDomains) {
		this.settlementMeterRelDomains = settlementMeterRelDomains;
	}

}
