/**
 * Author : czy
 * Date : 2019年4月16日 上午10:00:45
 * Title : com.riozenc.cfs.webapp.cfm.model.e.ECFModel.java
 **/
package org.fms.cfs.common.model;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fms.cfs.common.config.FixedParametersConfig;
import org.fms.cfs.common.utils.CalculationUtils;
import org.fms.cfs.common.webapp.domain.CommonParamDomain;
import org.fms.cfs.common.webapp.domain.MeterDomain;
import org.fms.cfs.common.webapp.domain.MeterMoneyDomain;
import org.fms.cfs.common.webapp.domain.MeterRelationGraphDomain;
import org.fms.cfs.common.webapp.domain.TransformerMeterRelationDomain;
import org.fms.cfs.common.webapp.domain.WriteFilesDomain;

import com.riozenc.titanTool.common.json.utils.GsonUtils;

/**
 * 电力计算模型
 *
 * @author czy
 */
public class ECFModel extends CFModel {
	protected static final Log logger = LogFactory.getLog(ECFModel.class);
	/**
	 * 正向有功总
	 */
	public static final int P1R0 = 110;
	/**
	 * 反向有功总
	 */
	public static final int P2R0 = 210;
	/**
	 * 正向无功总
	 */
	public static final int P3R0 = 120;
	/**
	 * 反向无功总
	 */
	public static final int P4R0 = 220;

	/**
	 * 正向有功峰
	 */
	public static final int F = 111;
	/**
	 * 正向有功平
	 */
	public static final int P = 112;
	/**
	 * 正向有功谷
	 */
	public static final int G = 113;
	/**
	 * 正向有功尖
	 */
	public static final int J = 114;

	// --
	/**
	 * 正向无功峰
	 */
	public static final int RF = 121;
	/**
	 * 正向无功平
	 */
	public static final int RP = 122;
	/**
	 * 正向无功谷
	 */
	public static final int RG = 123;
	/**
	 * 正向无功尖
	 */
	public static final int RJ = 124;

	public static int FILTER_NUMBER;// filter数量

	private Map<Integer, Boolean> processResults = new HashMap<>(FILTER_NUMBER);

	private Long businessPlaceCode;//营业区域
	private Long writeSectId;// 抄表区段ID
	private String userNo;//用户编号
	private Long meterId;// 计量点ID
	private String meterNo;// 计量点编号
	private Byte sn;// 算费次数
	private Byte countTimes;// 最大算费次数
	// private BigDecimal factorNum = new BigDecimal(1);// 倍率 默认为1
	private boolean isTs;// 是否分时计费
	private Byte tsType;//分时标志
	private boolean isReversed = false;// 是否翻转
	private boolean isIntegrity = false;// 数据是否完整
	private Byte meterType;//计量点类型
	private Byte msType;// 计量方式

	private BigDecimal activeEnergy = BigDecimal.ZERO;// 有功电量
	private BigDecimal reactiveEnergy = BigDecimal.ZERO;// 无功电量

	private BigDecimal readPower;// 抄见电量
	private BigDecimal changePower;// 换表电量
	private BigDecimal activeChargePower;// 有功计费电量
	private BigDecimal reactiveChargePower;// 无功计费电量
	private BigDecimal addPower;// 增减电量
	private BigDecimal deductionPower;// 套减电量
	private BigDecimal protocolPower;// 定量定比（协议用电）
	private BigDecimal refundPower;// 退补电量

	private boolean isCalculateTransformerLoss;
	private BigDecimal capacity = BigDecimal.ZERO;// 用电容量
	private Integer transformerLossType;//变损分摊类型
	private BigDecimal activeTransformerLoss = BigDecimal.ZERO;// 有功变损
	private BigDecimal reactiveTransformerLoss = BigDecimal.ZERO;// 无功变损

	private Byte lineLossType;// 线损计算方式
	private BigDecimal activeLineLossPower = BigDecimal.ZERO;// 有功线损电量
	private BigDecimal reactiveLineLossPower = BigDecimal.ZERO;// 无功线损电量
	private BigDecimal lineLossNum; // 线损率or线损值
	private BigDecimal qLineLossNum; // 无功线损率or值
	private BigDecimal lineLoss = BigDecimal.ZERO;// 线损

	private BigDecimal refundMoney = BigDecimal.ZERO;// 退补电费
	private Integer ladderSn;//所处阶梯
	private BigDecimal ladderNum = BigDecimal.ONE; // 阶梯电价用户基数
	private BigDecimal ladderTotalPower;// 阶梯总电量

	private BigDecimal volumeCharge = BigDecimal.ZERO;// 电度电费
	private List<BigDecimal> surcharges = new LinkedList<>();// 附加费
	private Map<Long,BigDecimal> surchargePrices = new HashMap<>();//附加费电价

	private Map<String, BigDecimal> surchargeDetail = new HashMap<>(8);
	private Map<String, BigDecimal> surchargeDetailByTimeSeg = new HashMap<>(8);

	private Byte basicMoneyFlag;// 基本电费计算标志
	private BigDecimal demand;// 最大需量
	private Long basicPriceId;// 基本电价ID
	private BigDecimal basicPrice = BigDecimal.ZERO;// 基本电费电价
	private BigDecimal basicCharge = BigDecimal.ZERO;

	private Byte cosType; // 力率标准
	private BigDecimal cos;// 调整率
	private BigDecimal cosRate;// 功率因数
	private BigDecimal powerRateCharge = BigDecimal.ZERO;// 力调电费

	private Long priceType;// 电价类型
	
	private BigDecimal totalAmount=BigDecimal.ZERO;//总电费=(目录/电度电费+力率/力调电费+基本电费) + 附加费
	private BigDecimal receivableAmount;//应收电费= 总电费 – 应退补电费

	private boolean isTop;// 是否顶点计量点

	private List<LadderModel> ladderDataModels = new LinkedList<>();// 阶梯数据
	private TransformerMeterRelationDomain transformerMeterRelationDomain;// 计量点与变压器关
	private TransformerCalculateModel transformerCalculateModel;// 变压器计算模型
	private MeterMoneyDomain meterMoneyDomain;
	private Map<Integer, MeterDataModel> meterDataModels = new HashMap<>();// 抄表数据细项，有且只有5项（总尖峰平谷）
	private List<MeterRelationGraphDomain> bottomMeterRelation;// 下级计量点关系

	private StringBuilder remark = new StringBuilder();

	public ECFModel(MeterDomain meterDomain, String date, Byte sn) {
		try {
			
			//临时属性
			this.userNo = meterDomain.getUserNo();
			this.businessPlaceCode = meterDomain.getBusinessPlaceCode();
			this.writeSectId = meterDomain.getWriteSectionId();
			this.meterId = meterDomain.getId();
			this.meterNo = meterDomain.getMeterNo();
			this.sn = sn;
			this.countTimes = meterDomain.getCountTimes();
			this.mon = date;
			this.meterType = meterDomain.getMeterType();
			this.isTs = meterDomain.getTsType() == 1;
			this.tsType = meterDomain.getTsType();
			this.lineLossType = meterDomain.getLineLostType();
			this.lineLossNum = meterDomain.getLineLostNum();
			this.qLineLossNum = meterDomain.getqLineLostNum();
			this.basicMoneyFlag = meterDomain.getBaseMoneyFlag();
			this.demand = meterDomain.getNeedIndex() == null ? BigDecimal.ZERO : meterDomain.getNeedIndex();
			this.priceType = meterDomain.getPriceType();
			this.basicPriceId = meterDomain.getBasicPrice();
			this.capacity = meterDomain.getChargingCapacity() == null ? BigDecimal.ZERO
					: meterDomain.getChargingCapacity();
			this.cosType = meterDomain.getCosType();
			this.ladderNum = meterDomain.getLadderNum() == null ? BigDecimal.ONE
					: BigDecimal.valueOf(meterDomain.getLadderNum());
		} catch (Exception e) {
			System.out.println(meterId);
			throw new RuntimeException("GGG");
		}

	}

	/**
	 * 获取计量点的总 抄见电量
	 *
	 * @return
	 */
	public BigDecimal getReadPower() {
		return this.readPower;
	}

	/**
	 * 是否分时计费
	 *
	 * @return
	 */
	public boolean isTs() {
		return this.isTs;
	}
	
	/**
	 * 获取分时标志
	 * @return
	 */
	public Byte getTsType() {
		return this.tsType;
	}

	/**
	 * 是否最后一次算费
	 *
	 * @return
	 */
	public boolean isLastTime() {
		return sn.byteValue() == countTimes.byteValue();
	}

	/**
	 * 获取有功计费电量
	 * 
	 * @return
	 */
	public BigDecimal getActiveChargePower() {
		return this.activeChargePower;
	}

	/**
	 * 获取无功计费电量
	 * 
	 * @return
	 */
	public BigDecimal getReactiveChargePower() {
		return this.reactiveChargePower;
	}

	public BigDecimal getActiveEnergy() {
		return this.activeEnergy;
	}

	public void addRemark(String remark) {
		this.remark.append(remark).append("\r\n");
	}

	public String getRemark() {
		return remark.toString();
	}

	public void setLineLoss(BigDecimal lineLoss) {
		this.lineLoss = lineLoss;
		meterDataModels.values().stream().filter(m -> m.isP1R0()).findFirst().get().setLineLossPower(lineLoss);
	}

	public Long getBusinessPlaceCode() {
		return this.businessPlaceCode;
	}
	public Long getWriteSectId() {
		return this.writeSectId;
	}
	
	public String getUserNo() {
		return this.userNo;
	}

	public Long getMeterId() {
		return this.meterId;
	}

	public String getMeterNo() {
		return this.meterNo;
	}

	public String getMon() {
		return super.mon;
	}

	public Byte getSn() {
		return this.sn;
	}

	public Byte getMeterType() {
		return this.meterType;
	}
	public Byte getMsType() {
		return this.msType;
	}

	/**
	 * 获取电费价格类型
	 *
	 * @return
	 */
	public Long getPriceType() {
		return this.priceType;
	}

	public Long getBasicPriceId() {
		return this.basicPriceId;
	}

	public void setBasicPrice(BigDecimal basicPrice) {
		this.basicPrice = basicPrice;
	}

	public void addSurcharge(BigDecimal surcharge) {
		this.surcharges.add(surcharge);
	}

	public BigDecimal getSurcharge() {
		return surcharges.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
//				.setScale(2,BigDecimal.ROUND_HALF_UP);
	}
	
	public void addSurchargePrice(Long itemId,BigDecimal surchargePrice) {
		this.surchargePrices.put(itemId,surchargePrice);
	}

	public void addSurchargeDetail(String name, BigDecimal surcharge) {
		this.surchargeDetail.put(name, surcharge);
	}

	public Map<String, BigDecimal> getSurchargeDetail() {
		return surchargeDetail;
	}
	
	public Map<Long,BigDecimal> getSurchargePrices(){
		return this.surchargePrices;
	}

	public void addSurchargeDetailByTimeSeg(String name, BigDecimal surcharge) {
		if (this.surchargeDetailByTimeSeg.containsKey(name)) {
			surchargeDetailByTimeSeg.put(name, surchargeDetailByTimeSeg.get(name).add(surcharge));
		} else {
			this.surchargeDetailByTimeSeg.put(name, surcharge);
		}
	}

	public Map<String, BigDecimal> getSurchargeDetailByTimeSeg() {
		return surchargeDetailByTimeSeg;
	}

	public BigDecimal getCapacity() {
		return capacity;
	}

	public TransformerMeterRelationDomain getTransformerMeterRelationDomain() {
		return this.transformerMeterRelationDomain;
	}

	public void setTransformerMeterRelationDomain(TransformerMeterRelationDomain transformerMeterRelationDomain) {
		this.transformerMeterRelationDomain = transformerMeterRelationDomain;
		this.msType = transformerMeterRelationDomain.getMsType();
	}

	public TransformerCalculateModel getTransformerCalculateModel() {
		return this.transformerCalculateModel;
	}

	public void setTransformerCalculateModel(TransformerCalculateModel transformerCalculateModel) {

		if (this.capacity.compareTo(BigDecimal.ZERO) == 0) {
			this.capacity = transformerCalculateModel.getCapacity();
		}
		this.transformerCalculateModel = transformerCalculateModel;
	}

	public MeterMoneyDomain getMeterMoneyDomain() {
		return this.meterMoneyDomain;
	}

	public void setMeterMoneyDomain(MeterMoneyDomain meterMoneyDomain) {
		this.meterMoneyDomain = meterMoneyDomain;
	}

	public Byte getLineLossType() {
		return lineLossType;
	}

	public BigDecimal getLineLossNum() {
		return lineLossNum;
	}

	public BigDecimal getqLineLossNum() {
		return qLineLossNum;
	}

	public BigDecimal getLineLoss() {
		return lineLoss;
	}

//	public BigDecimal getActiveEnergy() {
//		return activeEnergy;
//	}
	public Integer getTransformerLossType() {
		return transformerLossType;
	}
	
	public void setTransformerLossType(Integer transformerLossType) {
		this.transformerLossType = transformerLossType;
	}
	public BigDecimal getReactiveEnergy() {
		return reactiveEnergy;
	}

	public BigDecimal getActiveTransformerLoss() {
		return activeTransformerLoss;
	}

	public BigDecimal getReactiveTransformerLoss() {
		return reactiveTransformerLoss;
	}

	public BigDecimal getProtocolPower() {
		return protocolPower;
	}

	public void setProtocolPower(BigDecimal protocolPower) {
		this.protocolPower = protocolPower;
	}

	public BigDecimal addLastLadderTotalPower(BigDecimal ladderTotalPower) {
		this.ladderTotalPower = this.activeChargePower.add(ladderTotalPower);
		return this.ladderTotalPower;
	}

	public BigDecimal getLadderTotalPower() {
		return this.ladderTotalPower;
	}

	public BigDecimal getLadderNum() {
		return this.ladderNum;
	}
	
	

	public Integer getLadderSn() {
		return ladderSn;
	}

	public void setLadderSn(Integer ladderSn) {
		this.ladderSn = ladderSn;
	}

	/**
	 * 获取基本电费计算标志
	 *
	 * @return
	 */
	public Byte getBasicMoneyFlag() {
		return basicMoneyFlag;
	}

	/**
	 * 获取力调标准
	 *
	 * @return
	 */
	public Byte getCosType() {
		return cosType;
	}

	/**
	 * 获取调整率
	 *
	 * @return
	 */
	public BigDecimal getCos() {
		return this.cos;
	}

	/**
	 * 获取功率因数
	 *
	 * @return
	 */
	public BigDecimal getCosRate() {
		return this.cosRate;
	}

	public BigDecimal getBasicPrice() {
		return basicPrice;
	}

	public BigDecimal getVolumeCharge() {
		return volumeCharge;
	}

	public BigDecimal getBasicCharge() {
		return basicCharge;
	}

	public BigDecimal getPowerRateCharge() {
		return powerRateCharge;
	}

	public BigDecimal getAmount() {
		return amount;
	}
	
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	/**
	 * 用于阶梯
	 * 
	 * @param meterDataModel
	 */
	public void addMeterData(MeterDataModel meterDataModel) {
		this.meterDataModels.put(meterDataModel.getKey(), meterDataModel);
	}

	public void addLadderData(LadderModel ladderModel) {
		this.ladderDataModels.add(ladderModel);
	}

	public List<LadderModel> getLadderDataModels() {
		return this.ladderDataModels;
	}

	/**
	 * 退补
	 */
	public void setRefundMoney(BigDecimal refundMoney) {
		this.refundMoney = refundMoney;
	}
	
	
	public BigDecimal getRefundMoney() {
		return this.refundMoney;
	}

	/**
	 * 处理多个电能表对应一个计量点的特殊情况,电量相加 threeInOne
	 * 
	 * @param meterDataModel
	 */
	public void mergeMeterData(MeterDataModel meterDataModel) {
		MeterDataModel mainDataModel = this.meterDataModels.putIfAbsent(meterDataModel.getKey(), meterDataModel);
		if (mainDataModel != null) {
			mainDataModel.addThreeInOne(meterDataModel);
		}

		if (meterDataModel.isP1R0()) {// 包含正向有功总才是可计算数据
			this.isIntegrity = true;
		}
	}

	public Collection<MeterDataModel> getMeterData() {
		if (!isIntegrity()) {
			this.addRemark(getMeterNo() + ":数据完整性错误,缺少正向有功总抄表记录.");
			this.markProcessResult(0, false);
		}
		return meterDataModels.values();
	}
	
//	public void initVirtualMeterData() {
//	    int timeSeg = this.isTs ? 4 : 0;
//	    for (; timeSeg > -1; timeSeg--) {
//	      WriteFilesDomain writeFilesDomain = new WriteFilesDomain();
//	      writeFilesDomain.setTimeSeg((byte) timeSeg);
//	      writeFilesDomain.setPowerDirection((byte) 1);
//	      writeFilesDomain.setFunctionCode((byte) 1);
//	      writeFilesDomain.setPhaseSeq((byte) 4);
//	      writeFilesDomain.setStartNum(BigDecimal.ZERO);
//	      writeFilesDomain.setEndNum(BigDecimal.ZERO);
//	      writeFilesDomain.setAddPower(BigDecimal.ZERO);
//	      writeFilesDomain.setChgPower(BigDecimal.ZERO);
//
//	      MeterDataModel temp = new MeterDataModel(writeFilesDomain);
//
//	      this.meterDataModels.putIfAbsent(temp.getKey(), temp);
//
//	      if (temp.isP1R0()) {// 包含正向有功总才是可计算数据
//	        this.isIntegrity = true;
//	      }
//	    }
//	  }

	public void initVirtualMeterData(List<CommonParamDomain> timeSegParams) {		
		timeSegParams.stream().filter(t->isTs? true: t.getParamKey()==0).forEach(t->{
			WriteFilesDomain writeFilesDomain = new WriteFilesDomain();
			writeFilesDomain.setTimeSeg(t.getParamKey().byteValue());
			writeFilesDomain.setPowerDirection((byte) 1);
			writeFilesDomain.setFunctionCode((byte) 1);
			writeFilesDomain.setPhaseSeq((byte) 4);
			writeFilesDomain.setStartNum(BigDecimal.ZERO);
			writeFilesDomain.setEndNum(BigDecimal.ZERO);
			writeFilesDomain.setAddPower(BigDecimal.ZERO);
			writeFilesDomain.setChgPower(BigDecimal.ZERO);
			MeterDataModel temp = new MeterDataModel(writeFilesDomain);
			this.meterDataModels.putIfAbsent(temp.getKey(), temp);
			if (temp.isP1R0()) {// 包含正向有功总才是可计算数据
			  this.isIntegrity = true;
			}
		});
	}

	public boolean isReversed() {
		return isReversed;
	}

	public void setReversed(boolean isReversed) {
		this.isReversed = isReversed;
	}

	public void setActiveTransformerLoss(BigDecimal activeTransformerLoss) {
this.activeTransformerLoss = activeTransformerLoss;
		

		// 有功变损
		meterDataModels.values().stream().filter(m -> m.isP1R0()).findFirst().get()
				.setTransformerLossPower(activeTransformerLoss);
	
		if (isTs) {
			// 有功表有功变损分摊到峰平谷
			BigDecimal activeLoss = meterDataModels.values().stream()
					.filter(t -> t.getFunctionCode() == FixedParametersConfig.FUNCTION_CODE_1 && t.getKey() != P && t.getKey() != P1R0)
					.reduce(this.activeTransformerLoss, (l, m) -> {
						BigDecimal transformerLossPower = CalculationUtils
								.divide(m.getReadPower().multiply(activeTransformerLoss), this.readPower, 0);
						m.setTransformerLossPower(transformerLossPower);
						return l.subtract(transformerLossPower);
					}, (a, b) -> {
						// stream时这段代码无效 有parallelStream时才执行
						return b.subtract(this.activeTransformerLoss.subtract(a));
					});
			// 赋值平段变损
			MeterDataModel p1 = meterDataModels.values().stream().filter(t -> t.getFunctionCode() == 1)
					.filter(m -> m.getKey() == P).findFirst().orElse(null);
			if (p1 != null) {
				p1.setTransformerLossPower(activeLoss);
			}
		}
	}

	public void setReactiveTransformerLoss(BigDecimal reactiveTransformerLoss) {
		this.reactiveTransformerLoss = reactiveTransformerLoss;
		

		// 无功变损
		MeterDataModel p3r0 = meterDataModels.values().stream().filter(m -> m.isP3R0()).findFirst().orElse(null);
		if (p3r0 != null) {// 对 存在无功表的进行赋值
			meterDataModels.values().stream().filter(m -> m.isP3R0()).findFirst().get()
					.setTransformerLossPower(reactiveTransformerLoss);
		}

		if (isTs) {
			
			// 无功表无功变损分摊到峰平谷
			BigDecimal reactiveLoss = meterDataModels.values().stream()
					.filter(t -> t.getFunctionCode() == FixedParametersConfig.FUNCTION_CODE_2 && t.getKey() != P && t.getKey() != P3R0)
					.reduce(this.reactiveTransformerLoss, (l, m) -> {
						BigDecimal transformerLossPower = CalculationUtils.divide(m.getReadPower().multiply(reactiveTransformerLoss), this.readPower, 0);
						m.setTransformerLossPower(transformerLossPower);
						return l.subtract(transformerLossPower);		
					}, (a, b) -> {
						// TODO stream时这段代码无效 有parallelStream时才执行
						return b.subtract(this.reactiveTransformerLoss.subtract(a));
					});
			// 赋值平段变损
			MeterDataModel p3 = meterDataModels.values().stream().filter(t -> t.getFunctionCode() == 2)
					.filter(m -> m.getKey() == RP).findFirst().orElse(null);
			if (p3 != null) {
				p3.setTransformerLossPower(reactiveLoss);
			}
		}
	}

	public boolean isIntegrity() {
		return isIntegrity;
	}

	public boolean isTop() {
		return isTop;
	}

	public void setTop(boolean isTop) {
		this.isTop = isTop;
	}

	public List<MeterRelationGraphDomain> getBottomMeterRelation() {
		return bottomMeterRelation;
	}

	public void setBottomMeterRelation(List<MeterRelationGraphDomain> bottomMeterRelation) {
		this.bottomMeterRelation = bottomMeterRelation;
	}
	
	public void computeDiffNum() {
		getMeterData().stream().forEach(m -> {
			m.computeDiffNum(this);// 计算度差
		});

	}

	/**
	 * 根据起码止码倍率处理抄见电量
	 */
	public void computeReadPower() {

		getMeterData().parallelStream().forEach(m -> {
			m.setReadPower(m.getDiffNum().multiply(m.getFactorNum()).setScale(0, BigDecimal.ROUND_HALF_UP));
		});

		// 处理每个相序电能表的总电量
		this.getMeterData().stream().collect(Collectors.groupingBy(m -> m.getPhaseSeq())).forEach((i, list) -> {

			try {
				BigDecimal readPower = list.stream().filter(m -> m.isP1()).filter(m -> m.getTimeSeg() != 0)
						.map(MeterDataModel::getReadPower).reduce(BigDecimal.ZERO, BigDecimal::add);
				// 覆盖 正向有功总 的抄见电量
				if (readPower.compareTo(BigDecimal.ZERO) != 0) {

					BigDecimal totalPower = list.stream().filter(m -> m.isP1()).filter(m -> m.getTimeSeg() == 0)
							.findFirst().get().getReadPower();

					BigDecimal diffPower = list.stream().filter(m -> m.isP1()).filter(m -> m.getTimeSeg() != 0)
							.map(MeterDataModel::getReadPower).reduce(totalPower, BigDecimal::subtract);

					MeterDataModel pMeterDataModel = list.stream().filter(m -> m.getKey() == P).findFirst().get();

					pMeterDataModel.setReadPower(pMeterDataModel.getReadPower().add(diffPower));// 将改动改到平段上

				}
			} catch (Exception e) {
				e.printStackTrace();
				markProcessResult(10, false);
				addRemark(this.getMeterNo() + "异常,请检查抄表记录");
				logger.error(this.getMeterNo() + "异常,请检查抄表记录");
			}

		});

		this.getMeterData().parallelStream().forEach(meterData -> {
			meterData.computeActiveEnergy();
		});

		BigDecimal totalReadPower = this.getMeterData().stream().filter(m -> m.isP1R0())
				.map(MeterDataModel::getReadPower).reduce(BigDecimal.ZERO, BigDecimal::add);
		BigDecimal totalChargePower = this.getMeterData().stream().filter(m -> m.isP1R0())
				.map(MeterDataModel::getChargePower).reduce(BigDecimal.ZERO, BigDecimal::add);
		BigDecimal totalAddPower = this.getMeterData().stream().filter(m -> m.isP1R0()).map(MeterDataModel::getAddPower)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		BigDecimal totalChangePower = this.getMeterData().stream().filter(m -> m.isP1R0())
				.map(MeterDataModel::getChangePower).reduce(BigDecimal.ZERO, BigDecimal::add);
		BigDecimal totalProtocolPower = this.getMeterData().stream().filter(m -> m.isP1R0())
				.map(MeterDataModel::getProtocolPower).reduce(BigDecimal.ZERO, BigDecimal::add);
		BigDecimal totalDeductionPower = this.getMeterData().stream().filter(m -> m.isP1R0())
				.map(MeterDataModel::getDeductionPower).reduce(BigDecimal.ZERO, BigDecimal::add);
		BigDecimal totalCompensatingPower = this.getMeterData().stream().filter(m -> m.isP1R0())
				.map(MeterDataModel::getCompensatingPower).reduce(BigDecimal.ZERO, BigDecimal::add);

		this.readPower = totalReadPower;// 抄见有功电量
		this.addPower = totalAddPower;
		this.changePower = totalChangePower;
		this.protocolPower = totalProtocolPower;
		this.deductionPower = totalDeductionPower;
		this.refundPower = totalCompensatingPower;// 退补电量
		this.activeChargePower = totalChargePower;//
		this.activeEnergy = this.readPower.add(this.addPower).add(this.changePower);// 抄见+增减+换表
		this.reactiveEnergy = this.getMeterData().stream().filter(m -> m.isP3R0()).map(MeterDataModel::getActiveEnergy)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	/**
	 * 如果分时数据的套扣电量总和与总电量数据不符，则替换
	 */
	public void replaceProtocolPower() {

		BigDecimal protocolPower = this.getMeterData().parallelStream().filter(m -> m.isP1())
				.filter(m -> m.getTimeSeg() != 0).map(MeterDataModel::getProtocolPower)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		MeterDataModel meterDataModel = this.getMeterData().parallelStream().filter(m -> m.isP1R0()).findFirst()
				.orElse(null);
		if (meterDataModel == null) {
			throw new RuntimeException(this.getMeterNo() + " 计量点 缺少 正向有功总的抄表记录.");
		}
		// 判断是否替换协议电量
		if (protocolPower.compareTo(BigDecimal.ZERO) != 0) {
			meterDataModel.replaceProtocolPower(protocolPower);
		}

		// 已经在computeReadPower方法中，赋值了无功电量
//        getMeterData().parallelStream().forEach(m -> {
//            // 赋值无功总电量 = 正无功+反无功
//            if (m.getFunctionCode() == 2) {
//                this.reactiveEnergy = this.reactiveEnergy.add(m.getReadPower());
//            }
//        });

	}

	/**
	 * 计算电度电费
	 */
	public void computeVolumeCharge() {
		if (isTs()) {
			this.volumeCharge = getMeterData().stream().filter(m -> m.isP1()).filter(m -> m.getTimeSeg() != 0)
					.map(MeterDataModel::getCharge).reduce(BigDecimal.ZERO, BigDecimal::add)
					.setScale(2, BigDecimal.ROUND_HALF_UP);
		} else {
			this.volumeCharge = getMeterData().stream().filter(m -> m.isP1R0()).map(MeterDataModel::getCharge)
					.reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, BigDecimal.ROUND_HALF_UP);
		}

		// 处理阶梯
		if (!ladderDataModels.isEmpty()) {
//        	this.volumeCharge = this.volumeCharge.add(ladderDataModels.stream().map(LadderModel::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
			this.volumeCharge = ladderDataModels.stream().map(LadderModel::getAmount).reduce(BigDecimal.ZERO,
					BigDecimal::add);

		}
	}

	/**
	 * 计算计费电量
	 */
	public void computeChargePower() {

		this.getMeterData().parallelStream().forEach(meterData -> {
			meterData.computeChargePower();
		});
		if (isTs()) {
			this.activeChargePower = getMeterData().stream().filter(m -> m.isP1()).filter(m -> m.getTimeSeg() != 0)
					.map(MeterDataModel::getChargePower).reduce(BigDecimal.ZERO, BigDecimal::add)
					.setScale(2, BigDecimal.ROUND_HALF_UP);
			// 桂东无功只有总
			this.reactiveChargePower = getMeterData().stream().filter(m -> m.isP3R0())
					.map(MeterDataModel::getChargePower).reduce(BigDecimal.ZERO, BigDecimal::add)
					.setScale(2, BigDecimal.ROUND_HALF_UP);
		} else {
			this.activeChargePower = getMeterData().stream().filter(m -> m.isP1R0()).map(MeterDataModel::getChargePower)
					.reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, BigDecimal.ROUND_HALF_UP);
			this.reactiveChargePower = getMeterData().stream().filter(m -> m.isP3R0())
					.map(MeterDataModel::getChargePower).reduce(BigDecimal.ZERO, BigDecimal::add)
					.setScale(2, BigDecimal.ROUND_HALF_UP);
		}
	}

	/**
	 * 计算变损并分摊 先按用电量进行分摊 size 标识计量点个数 isLast标识是否最后一个计量点 subActiveTransformerLoss
	 * 前面计量点的有功变损和 subReactiveTransformerLoss 前面计量点的无功变损和
	 */
	public void calculateTransformerLoss(int size, boolean isLast, BigDecimal subActiveTransformerLoss,
			BigDecimal subReactiveTransformerLoss) {
		// 计量点数量
		BigDecimal count = new BigDecimal(size);
		// TODO
		// 暂时处理 1.计量点电量！=总电量 按电量分摊
		// 2.计量点电量=总电量！=0 变损全分在改计量点
		// 3.计量点电量=总电量=0 按计量点个数平均分摊
		// 遗留问题 变损存在小数
		if (isLast) {
			this.activeTransformerLoss = this.transformerCalculateModel.getActiveTransformerLoss()
					.subtract(subActiveTransformerLoss);
			this.reactiveTransformerLoss = this.transformerCalculateModel.getReactiveTransformerLoss()
					.subtract(subReactiveTransformerLoss);
		} else if (this.getActiveEnergy() != this.transformerCalculateModel.getActiveEnergy()) {
			this.activeTransformerLoss = CalculationUtils
					.divide(this.getActiveEnergy().multiply(this.transformerCalculateModel.getActiveTransformerLoss()),
							this.transformerCalculateModel.getActiveEnergy())
					.setScale(0, BigDecimal.ROUND_HALF_UP);
			this.reactiveTransformerLoss = CalculationUtils.divide(
					this.getActiveEnergy().multiply(this.transformerCalculateModel.getReactiveTransformerLoss()),
					this.transformerCalculateModel.getActiveEnergy()).setScale(0, BigDecimal.ROUND_HALF_UP);
		} else if (!this.getActiveEnergy().equals(BigDecimal.ZERO)) {
			this.activeTransformerLoss = this.transformerCalculateModel.getActiveTransformerLoss();
			this.reactiveTransformerLoss = this.transformerCalculateModel.getReactiveTransformerLoss();
		} else {
			this.activeTransformerLoss = CalculationUtils
					.divide(this.transformerCalculateModel.getActiveTransformerLoss(), count)
					.setScale(0, BigDecimal.ROUND_HALF_UP);
			this.reactiveTransformerLoss = CalculationUtils
					.divide(this.transformerCalculateModel.getReactiveTransformerLoss(), count)
					.setScale(0, BigDecimal.ROUND_HALF_UP);
		}

		// 有功变损
		meterDataModels.values().stream().filter(m -> m.isP1R0()).findFirst().get()
				.setTransformerLossPower(activeTransformerLoss);
		// 无功变损
		MeterDataModel p3r0 = meterDataModels.values().stream().filter(m -> m.isP3R0()).findFirst().orElse(null);
		if (p3r0 != null) {// 对 存在无功表的进行赋值
			meterDataModels.values().stream().filter(m -> m.isP3R0()).findFirst().get()
					.setTransformerLossPower(reactiveTransformerLoss);
		}

		if (isTs) {
			// 有功表有功变损分摊到峰平谷
			BigDecimal activeLoss = meterDataModels.values().stream()
					.filter(t -> t.getFunctionCode() == FixedParametersConfig.FUNCTION_CODE_1 && t.getKey() != P && t.getKey() != P1R0)
					.reduce(this.activeTransformerLoss, (l, m) -> {
						BigDecimal transformerLossPower = CalculationUtils
								.divide(m.getReadPower().multiply(activeTransformerLoss), this.readPower, 0);
						m.setTransformerLossPower(transformerLossPower);
						return l.subtract(transformerLossPower);
					}, (a, b) -> {
						// stream时这段代码无效 有parallelStream时才执行
						return b.subtract(this.activeTransformerLoss.subtract(a));
					});
			// 赋值平段变损
			MeterDataModel p1 = meterDataModels.values().stream().filter(t -> t.getFunctionCode() == 1)
					.filter(m -> m.getKey() == P).findFirst().orElse(null);
			if (p1 != null) {
				p1.setTransformerLossPower(activeLoss);
			}

			// 无功表无功变损分摊到峰平谷
			BigDecimal reactiveLoss = meterDataModels.values().stream()
					.filter(t -> t.getFunctionCode() == FixedParametersConfig.FUNCTION_CODE_2 && t.getKey() != P && t.getKey() != P3R0)
					.reduce(this.reactiveTransformerLoss, (l, m) -> {
						BigDecimal transformerLossPower = CalculationUtils
								.divide(m.getReadPower().multiply(reactiveTransformerLoss), this.readPower, 0);
						m.setTransformerLossPower(transformerLossPower);
						return l.subtract(transformerLossPower);
					}, (a, b) -> {
						// TODO stream时这段代码无效 有parallelStream时才执行
						return b.subtract(this.reactiveTransformerLoss.subtract(a));
					});
			// 赋值平段变损
			MeterDataModel p3 = meterDataModels.values().stream().filter(t -> t.getFunctionCode() == 2)
					.filter(m -> m.getKey() == RP).findFirst().orElse(null);
			if (p3 != null) {
				p3.setTransformerLossPower(reactiveLoss);
			}
		}

	}

	public boolean calculateBasicMoney(int runDays, int monthDays) {
		switch (basicMoneyFlag) {
		case 1:// 需量
			return calculateBasicMoneyByDemand(demand, basicPrice);
		case 2:// 容量
			return calculateBasicMoneyByCapacity(capacity, runDays, monthDays, basicPrice);
		default:
			this.addRemark("变损计算只支持按需量标志(1)、按容量(2)计算.目前维护值=:" + basicMoneyFlag);
			return false;
		}
	}

	/**
	 * 计算基本电费-按容量
	 */
	public boolean calculateBasicMoneyByCapacity(BigDecimal capacity, int runDays, int monthDays,
			BigDecimal basicPrice) {
		BigDecimal percentage = CalculationUtils.divide(BigDecimal.valueOf(runDays), BigDecimal.valueOf(monthDays));
		// 变压器容量*时间比例(运行天数/实际天数)*容量电价
		this.basicCharge = capacity.multiply(basicPrice).multiply(percentage);
		return true;
	}

	/**
	 * 计算基本电费-按需量 2018年，广西改为 实际需量*单价
	 */
	public boolean calculateBasicMoneyByDemand(BigDecimal demand, BigDecimal basicPrice) {
		this.basicCharge = demand.multiply(basicPrice);
		return true;
	}

	/**
	 * 计算力调电费
	 *
	 * @param
	 * @return
	 */
	public BigDecimal computePowerRateMoney(BigDecimal k, BigDecimal g) {
		this.powerRateCharge = CalculationUtils.multiply(getVolumeCharge().add(getBasicCharge()), k);
		this.cos = k;// 调整率
		this.cosRate = g;// 功率因数
		return powerRateCharge;
	}

	public BigDecimal computeAmount() {
		this.totalAmount=this.volumeCharge;
		this.surcharges.forEach(s -> {
			this.totalAmount = this.totalAmount.add(s);
		});
		this.totalAmount = this.totalAmount.add(this.basicCharge).add(this.powerRateCharge).setScale(2, BigDecimal.ROUND_HALF_UP);
		this.amount = this.totalAmount;
		return this.totalAmount;
	}
	
	public BigDecimal computeRefundMoney(BigDecimal refundMoney) {
		this.refundMoney = this.refundMoney.add(refundMoney);
		this.amount = this.amount.subtract(refundMoney);
		return this.amount;
	}

	final public void markProcessResult(int order, boolean mark) {
		this.processResults.put(order, mark);
	}

	final public boolean isComplete() {
		if (this.processResults.containsValue(false)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return GsonUtils.toJson(this);
	}
}
