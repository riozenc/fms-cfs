/**
 * Author : czy
 * Date : 2019年8月14日 上午11:25:06
 * Title : com.riozenc.cfs.webapp.cfm.model.MeterDataModel.java
 *
**/
package org.fms.cfs.common.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.fms.cfs.common.utils.CalculationUtils;
import org.fms.cfs.common.webapp.domain.WriteFilesDomain;

public class MeterDataModel extends CFModel {

	private int key;
	private String timeSeg;// 标识 （总、尖、峰、平、谷、正无、反无）
	private Byte functionCode;// 功能代码 (有功、无功)
	private Byte powerDirection;// 功率方向(正向、反向)
	private Byte phaseSeq;// 相序
	private Integer ladder;
	private BigDecimal startNum;// 起码
	private BigDecimal endNum;// 止码
	private BigDecimal diffNum;// 度差
	private BigDecimal readPower = BigDecimal.ZERO;// 抄见电量
	private BigDecimal addPower = BigDecimal.ZERO;// 增减电量
	private BigDecimal changePower = BigDecimal.ZERO;// 换表电量
	private BigDecimal deductionPower = BigDecimal.ZERO;// 套减电量
	private BigDecimal protocolPower = BigDecimal.ZERO;// 定量定比（协议用电）
	private BigDecimal compensatingPower = BigDecimal.ZERO;// 退补电量
	private BigDecimal lineLossPower = BigDecimal.ZERO;// 线损电量
	private BigDecimal transformerLossPower = BigDecimal.ZERO;// 变损电量
	private BigDecimal chargePower = BigDecimal.ZERO;// 计费电量
	private BigDecimal activeEnergy = BigDecimal.ZERO;// meterPower-》电能表电量=抄见+增减+换表
	private BigDecimal charge = BigDecimal.ZERO;
	private BigDecimal factorNum = BigDecimal.ONE;
	private List<MeterDataModel> threeInOne = Collections.synchronizedList(new ArrayList<>(2));// 针对老现场的特殊情况：3个单相表当作一个三相表用

	private boolean isProtocol = true;// 判断电量是协议来的还是自己用的，以便于正确处理套扣电量

	public MeterDataModel(WriteFilesDomain writeFilesDomain) {
		this.setLadder(0);
		this.setTimeSeg(writeFilesDomain.getTimeSeg());// 分段标识
		this.setPowerDirection(writeFilesDomain.getPowerDirection());
		this.setFunctionCode(writeFilesDomain.getFunctionCode());
		this.setPhaseSeq(writeFilesDomain.getPhaseSeq());
		this.setStartNum(writeFilesDomain.getStartNum());
		this.setEndNum(writeFilesDomain.getEndNum());
		this.setAddPower(writeFilesDomain.getAddPower());// 增减电量
		this.setChangePower(writeFilesDomain.getChgPower());// 换表电量
		this.setFactorNum(writeFilesDomain.getFactorNum() == null ? factorNum : writeFilesDomain.getFactorNum());
		this.createKey();
	}

	public boolean isProtocol() {
		return isProtocol;
	}

	public void setProtocol(boolean isProtocol) {
		this.isProtocol = isProtocol;
	}

	public String getTimeSeg() {
		return timeSeg;
	}

	public void setTimeSeg(String timeSeg) {
		this.timeSeg = timeSeg;
	}

	public BigDecimal getStartNum() {
		return startNum;
	}

	public void setStartNum(BigDecimal startNum) {
		this.startNum = startNum;
	}

	public BigDecimal getEndNum() {
		return endNum;
	}

	public void setEndNum(BigDecimal endNum) {
		this.endNum = endNum;
	}

	public BigDecimal getDiffNum() {
		return diffNum;
	}

	public void setDiffNum(BigDecimal diffNum) {
		this.diffNum = diffNum;
	}

	public BigDecimal getReadPower() {
		return readPower;
	}

	protected void setReadPower(BigDecimal readPower) {
		this.readPower = readPower;
	}

	public BigDecimal getChargePower() {
		return chargePower;
	}

	public void setChargePower(BigDecimal chargePower) {
		this.chargePower = chargePower;
	}

	public BigDecimal getAddPower() {
		return addPower;
	}

	public void setAddPower(BigDecimal addPower) {
		this.addPower = addPower == null ? BigDecimal.ZERO : addPower;
	}

	public BigDecimal getChangePower() {
		return changePower;
	}

	public void setChangePower(BigDecimal changePower) {
		this.changePower = changePower == null ? BigDecimal.ZERO : changePower;
	}

	public BigDecimal getDeductionPower() {
		return deductionPower;
	}

	public void setDeductionPower(BigDecimal deductionPower) {
		this.deductionPower = deductionPower;
	}

	public BigDecimal getCharge() {
		return charge;
	}

	public BigDecimal getActiveEnergy() {
		return activeEnergy;
	}

	public BigDecimal getProtocolPower() {
//		if (isProtocol) {
//			return protocolPower;
//		} else {
//			return BigDecimal.ZERO;
//		}
		return protocolPower;

	}

	/**
	 * 给下级增加抄见电量
	 * 
	 * @param protocolPower
	 */
	public void addReadPowerByProtocol(BigDecimal protocolPower) {
		this.readPower = this.readPower.add(protocolPower);
	}

	public void addProtocolPower(BigDecimal protocolPower) {
		if (isProtocol) {
			this.protocolPower = this.protocolPower.add(protocolPower);
		}
//		else {
//			this.deductionPower = this.deductionPower.add(protocolPower);
//		}
	}

	public void replaceProtocolPower(BigDecimal protocolPower) {
		this.protocolPower = protocolPower;
	}

	public BigDecimal getCompensatingPower() {
		return compensatingPower;
	}

	public void setCompensatingPower(BigDecimal compensatingPower) {
		this.compensatingPower = compensatingPower == null ? BigDecimal.ZERO : compensatingPower;
	}

	public BigDecimal computeCharge(BigDecimal price, BigDecimal surcharge) {
		surcharge = surcharge == null ? BigDecimal.ZERO : surcharge.setScale(2, BigDecimal.ROUND_HALF_UP);
		this.charge = this.chargePower.multiply(price).setScale(2, BigDecimal.ROUND_HALF_UP).subtract(surcharge);
		this.price = price;
		return this.charge;
	}
	
	public void setCharge(BigDecimal charge) {
		this.charge = charge;
	}

	public Byte getFunctionCode() {
		return functionCode;
	}

	public void setFunctionCode(Byte functionCode) {
		this.functionCode = functionCode;
	}

	public Byte getPowerDirection() {
		return powerDirection;
	}

	public void setPowerDirection(Byte powerDirection) {
		this.powerDirection = powerDirection;
	}

	public BigDecimal getLineLossPower() {
		return lineLossPower;
	}

	public void setLineLossPower(BigDecimal lineLossPower) {
		this.lineLossPower = lineLossPower;
	}

	public BigDecimal getTransformerLossPower() {
		return transformerLossPower;
	}

	public void setTransformerLossPower(BigDecimal transformerLossPower) {
		this.transformerLossPower = transformerLossPower;
	}

	public Byte getPhaseSeq() {
		return phaseSeq;
	}

	public void setPhaseSeq(Byte phaseSeq) {
		this.phaseSeq = phaseSeq;
	}

	public BigDecimal getFactorNum() {
		return factorNum;
	}

	public void setFactorNum(BigDecimal factorNum) {
		this.factorNum = factorNum;
	}

	public Integer getLadder() {
		return ladder;
	}

	public void setLadder(Integer ladder) {
		this.ladder = ladder;
	}

	public int getKey() {
		return key;
	}

	public List<MeterDataModel> getThreeInOne() {
		return threeInOne;
	}

	public List<MeterDataModel> addThreeInOne(MeterDataModel meterDataModel) {
		this.threeInOne.add(meterDataModel);
		return this.threeInOne;
	}

	/**
	 * 是否正向有功总
	 * 
	 * @return
	 */
	public boolean isP1R0() {
		return "0".equals(this.timeSeg) && powerDirection == 1 && functionCode == 1;
	}

	/**
	 * 是否正向无功总
	 * 
	 * @return
	 */
	public boolean isP3R0() {
		return "0".equals(this.timeSeg) && powerDirection == 1 && functionCode == 2;
	}

	/**
	 * 是否正向有功
	 * 
	 * @return
	 */
	public boolean isP1() {
		return this.powerDirection == 1 && this.functionCode == 1;
	}

	/**
	 * 是否正向无功
	 * 
	 * @return
	 */
	public boolean isP3() {
		return this.powerDirection == 1 && this.functionCode == 2;
	}

	// 根据规则生成key 由阶梯、功率方向、功能代码、时段
	public void createKey() {
//		this.key = getLadder() * 1000 + getPowerDirection() * 100 + getFunctionCode() * 10 + getTimeSeg();
		this.key = getPowerDirection() * 100 + getFunctionCode() * 10 + Integer.parseInt(getTimeSeg());
	}

	public int getComputeKey() {
		return getPowerDirection() * 100 + getFunctionCode() * 10 + Integer.parseInt(getTimeSeg());
	}

	public void setComputeKey(int computeKey) {
		this.setPowerDirection((byte) (computeKey / 100));
		this.setFunctionCode((byte) (computeKey / 10 % 10));
		this.setTimeSeg(String.valueOf((computeKey % 10)));
	}

	public BigDecimal computeDiffNum(ECFModel ecfModel) {

		BigDecimal diffNum;

		switch (this.getEndNum().compareTo(this.getStartNum())) {
		case -1:
			String full = String.valueOf(this.getStartNum().intValue());//
			int length = full.length();// 获取长度
			diffNum = BigDecimal.TEN.pow(length).subtract(this.getStartNum()).add(this.getEndNum());
			// 加标记
			ecfModel.setReversed(true);// 翻转
			break;
		case 0:
			diffNum = BigDecimal.ZERO;
			break;
		case 1:
			diffNum = this.getEndNum().subtract(this.getStartNum());
			break;
		default:
			diffNum = this.getEndNum().subtract(this.getStartNum());
			break;
		}
		this.setDiffNum(diffNum);// 度差

		getThreeInOne().forEach(m -> {
			m.computeDiffNum(ecfModel);
		});

		return this.getDiffNum();
	}

	public BigDecimal computeSurcharge(Long itemId, BigDecimal surPrice, int scale) {
		return CalculationUtils.multiply(getChargePower(), surPrice, scale);
	}

	public BigDecimal computeActiveEnergy() {
		this.activeEnergy = this.readPower.add(this.addPower).add(this.changePower);
		return this.activeEnergy;
	}

	public BigDecimal computeChargePower() {
		this.chargePower = this.activeEnergy.add(this.lineLossPower).add(this.transformerLossPower)
				.add(this.compensatingPower);
		if (isProtocol) {
			this.chargePower = this.chargePower.add(this.protocolPower);
		}
//		else {
//			this.chargePower = this.chargePower.add(this.deductionPower);
//		}
		return this.chargePower;
	}
}
