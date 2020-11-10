/**
 * Author : czy
 * Date : 2019年10月24日 下午8:06:20
 * Title : com.riozenc.cfs.webapp.mrm.filter.WriteFilesInitFilter2.java
 *
**/
package org.fms.cfs.server.webapp.mrm.filter.datainit;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.fms.cfs.common.config.FixedParametersConfig;
import org.fms.cfs.common.config.MongoCollectionConfig;
import org.fms.cfs.common.model.BillingDataInitModel;
import org.fms.cfs.common.model.exchange.InitModelExchange;
import org.fms.cfs.common.utils.MonUtils;
import org.fms.cfs.common.webapp.domain.CommonParamDomain;
import org.fms.cfs.common.webapp.domain.MeterDomain;
import org.fms.cfs.common.webapp.domain.MeterInductorAssetsRelDomain;
import org.fms.cfs.common.webapp.domain.MeterMeterAssetsRelDomain;
import org.fms.cfs.common.webapp.domain.MeterReplaceDomain;
import org.fms.cfs.common.webapp.domain.UserDomain;
import org.fms.cfs.common.webapp.domain.WriteFilesDomain;
import org.fms.cfs.server.webapp.mrm.filter.BillingDataInitFilter;
import org.fms.cfs.server.webapp.mrm.filter.InitFilterChain;

import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.WriteModel;
import com.riozenc.titanTool.mongo.dao.MongoDAOSupport;

import reactor.core.publisher.Mono;

/**
 * 抄表初始化销户的行为
 * 
 * @author czy
 *
 */
public class WriteFilesInitFilter2 implements BillingDataInitFilter, MongoDAOSupport {

	private static final Log logger = LogFactory.getLog(WriteFilesInitFilter2.class);

	@Override
	public int getOrder() {
		return 40;
	}

	@Override
	public Mono<Void> filter(InitModelExchange exchange, InitFilterChain filterChain) {

		// 组装抄表单数据
		BillingDataInitModel billingDataInitModel = (BillingDataInitModel) exchange.getModel();
		String date = billingDataInitModel.getDate();

		Map<Long, MeterDomain> meterMap = billingDataInitModel.getMeterDomains().stream()
				.collect(Collectors.toMap(MeterDomain::getId, u -> u, (k, v) -> v));

		List<WriteFilesDomain> writeFilesList = Collections.synchronizedList(new LinkedList<WriteFilesDomain>());

		Map<Long, UserDomain> userMap = billingDataInitModel.getUserDomains().parallelStream()
				.collect(Collectors.toMap(UserDomain::getId, u -> u, (k, v) -> v));

		// 计量点与电能表资产关系
		List<MeterMeterAssetsRelDomain> meterMeterAssetsRelDomains = billingDataInitModel
				.getMeterMeterAssetsRelDomains();
		// 计量点与互感器资产关系
		List<MeterInductorAssetsRelDomain> meterInductorAssetsRelDomains = billingDataInitModel
				.getMeterInductorAssetsRelDomains();

		// 获取时段 TIME_SEG
		List<CommonParamDomain> timeSegList = findMany(getCollection(date, MongoCollectionConfig.SYSTEM_COMMON.name()),
				new MongoFindFilter() {
					@Override
					public Bson filter() {
						return Filters.eq("type", "TIME_SEG");
					}
				}, CommonParamDomain.class);

		if (timeSegList.size() == 0) {
			throw new RuntimeException(date+ "月份的参数表时段维护错误,请检查.");
		}

		// functionCode = 3(虚拟表) 剔除虚拟表
		meterMeterAssetsRelDomains.parallelStream().filter(mma -> mma.getFunctionCode() != 3).forEach(mma -> {
			MeterDomain meterDomain = meterMap.get(mma.getMeterId());
			if (meterDomain != null) {
				if (billingDataInitModel.getSn() <= meterDomain.getCountTimes()) {// 是否多次算费（当前算费次数小于等于应算费次数）
					UserDomain userDomain = userMap.get(meterDomain.getUserId());
					if (userDomain != null) {
						// 根据电能表资产生成抄表记录
						if (mma.getFunctionCode() == 1) {// 有功
							writeFilesList.add(createWriteFilesDomain(date, userDomain, meterDomain, mma, (byte) 0));// timeSeg

//							是否分时表，分时表额外创建4个对象尖、峰、平、谷
							if (mma.getTsFlag() != null && mma.getTsFlag() == 1) {

								timeSegList.stream().filter(t -> t.getParamKey() != 0).forEach(t -> {
									writeFilesList.add(createWriteFilesDomain(date, userDomain, meterDomain, mma,
											t.getParamKey().byteValue()));
								});

//								writeFilesList
//										.add(createWriteFilesDomain(date, userDomain, meterDomain, mma, (byte) 1));
//								writeFilesList
//										.add(createWriteFilesDomain(date, userDomain, meterDomain, mma, (byte) 2));
//								writeFilesList
//										.add(createWriteFilesDomain(date, userDomain, meterDomain, mma, (byte) 3));
//								writeFilesList
//										.add(createWriteFilesDomain(date, userDomain, meterDomain, mma, (byte) 4));
							}
						} else {
							// 无功
							// 是否最后一次算费
							if (billingDataInitModel.getSn() == meterDomain.getCountTimes()
									// 判断电能表功能代码和功率方向 | FUNCTION_CODE = 2 代表 无功 | POWER_DIRECTION = 1 代表正向
									&& (mma.getFunctionCode() == 2)) {
								writeFilesList
										.add(createWriteFilesDomain(date, userDomain, meterDomain, mma, (byte) 0));// 无功
							}
						}

					} else {
						billingDataInitModel.addExecuteResult(meterDomain.getUserId() + "用户ID未找到,请检查!");
					}

				}

			} else {
				billingDataInitModel.addExecuteResult(mma.getMeterId() + "计量点ID未找到,请检查！");
			}
		});

		Set<Long> meterIds = writeFilesList.stream().map(WriteFilesDomain::getMeterId).collect(Collectors.toSet());// 有效meterId

		// 获取mongo中的已抄数据
		List<WriteFilesDomain> oldWriteFilesList = findMany(
				getCollection(date, MongoCollectionConfig.WRITE_FILES.name()), new MongoFindFilter() {
					@Override
					public Bson filter() {
						return Filters.and(Filters.eq("writeFlag", 1), Filters.in("meterId", meterIds));
					}
				}, WriteFilesDomain.class);

		// 赋值有效数据的起码
		assignmentStartNum(date, writeFilesList);// 获取上个月起码

		// 赋值已抄数据的止码
		assignmentEndNum(writeFilesList, oldWriteFilesList);

		// 删除mongo中的数据
		deleteMany(getCollectionName(date, MongoCollectionConfig.WRITE_FILES.name()), new MongoDeleteFilter() {
			@Override
			public Bson filter() {
				return Filters.in("meterId", meterIds);
			}
		});

		// 插入新的抄表记录
		List<WriteModel<Document>> insertResult = insertMany(toDocuments(writeFilesList));

		if (insertResult.size() != 0) {
			BulkWriteResult bulkWriteResult = getCollection(date, MongoCollectionConfig.WRITE_FILES.name())
					.bulkWrite(insertResult);
			billingDataInitModel.addExecuteResult("抄表单数据操作：" + bulkWriteResult.getUpserts().size());
		}

		return filterChain.filter(exchange);

	}

	private WriteFilesDomain createWriteFilesDomain(String date, UserDomain userDomain, MeterDomain meterDomain,
			MeterMeterAssetsRelDomain meterMeterAssetsRelDomain, byte timeSeg) {

		WriteFilesDomain writeFilesDomain = new WriteFilesDomain();
		writeFilesDomain.setMeterId(meterDomain.getId());
		writeFilesDomain.setMeterNo(meterDomain.getMeterNo());
//		writeFilesDomain.setInitDate(DateUtil.getDateFromUTC());
		writeFilesDomain.setInitDate(new Date());
		writeFilesDomain.setMon(Integer.valueOf(date));
		writeFilesDomain.setSn(meterDomain.getSn());// 算费次数
		writeFilesDomain.setWriteFlag((byte) 0);// 抄表方式
		writeFilesDomain.setWriteMethod(meterDomain.getWriteMethod());// ????未定义
		writeFilesDomain.setTimeSeg(timeSeg);//
		writeFilesDomain.setTgId(meterDomain.getTgId());
		writeFilesDomain.setLineId(meterDomain.getLineId());
		writeFilesDomain.setSubsId(meterDomain.getSubsId());
		writeFilesDomain.setUserId(meterDomain.getUserId());
		writeFilesDomain.setWriteSectionId(userDomain.getWriteSectId());
		writeFilesDomain.setBusinessPlaceCode(userDomain.getBusinessPlaceCode());

		writeFilesDomain.setMeterAssetsId(meterMeterAssetsRelDomain.getMeterAssetsId());// 电能表资产ID
		writeFilesDomain.setPhaseSeq(meterMeterAssetsRelDomain.getPhaseSeq());// 相序
		writeFilesDomain.setFunctionCode(meterMeterAssetsRelDomain.getFunctionCode());// 功能代码
		writeFilesDomain.setPowerDirection(meterMeterAssetsRelDomain.getPowerDirection());// 功率方向
		writeFilesDomain.setFactorNum(meterMeterAssetsRelDomain.getFactorNum());// 倍率

		writeFilesDomain.setUserNo(userDomain.getUserNo());
		writeFilesDomain.setUserName(userDomain.getUserName());
		writeFilesDomain.setAddress(userDomain.getAddress());

		writeFilesDomain.createObjectId();
		return writeFilesDomain;
	}

	private void assignmentStartNum(String date, List<WriteFilesDomain> writeFilesList) {

		// 获取上月抄表记录
		Map<String, WriteFilesDomain> lastWriteFilesDomains = findMany(
				getCollection(MonUtils.getLastMon(date), MongoCollectionConfig.WRITE_FILES.name()),
				new MongoFindFilter() {
					@Override
					public Bson filter() {
						return Filters.in("_id",
								writeFilesList.stream().map(WriteFilesDomain::get_id).collect(Collectors.toList()));
					}
				}, WriteFilesDomain.class).parallelStream().collect(Collectors.toMap(WriteFilesDomain::get_id, k -> k));

		writeFilesList.forEach(w -> {
			WriteFilesDomain last = lastWriteFilesDomains.get(w.get_id());

			if (last == null || last.getStartNum() == null) {
				w.setStartNum(BigDecimal.ZERO);
			} else {
				w.setStartNum(last.getEndNum());
				w.setLastWriteDate(last.getWriteDate());// 上次抄表时间
			}

		});
		meterChange(date, writeFilesList);// 处理换表
	}

	private void assignmentEndNum(List<WriteFilesDomain> writeFilesList, List<WriteFilesDomain> oldWriteFilesList) {
		Map<String, WriteFilesDomain> owfm = oldWriteFilesList.stream()
				.collect(Collectors.toMap(WriteFilesDomain::get_id, k -> k));
		writeFilesList.stream().filter(w -> owfm.get(w.get_id()) != null).forEach(w -> {
			w.setEndNum(owfm.get(w.get_id()).getEndNum());
			w.setDiffNum(w.getEndNum().subtract(w.getStartNum()));
			w.setWriteFlag(owfm.get(w.get_id()).getWriteFlag());
		});

	}

	// TODO
	private void meterChange(String date, List<WriteFilesDomain> writeFilesList) {
		Map<Long, List<MeterReplaceDomain>> meterReplaceMap = findMany(
				getCollectionName(date, MongoCollectionConfig.ELECTRIC_METER_REPLACE.name()), new MongoFindFilter() {
					@Override
					public Bson filter() {
						return Filters.in("meterId",
								writeFilesList.stream().map(WriteFilesDomain::getMeterId).collect(Collectors.toList()));
					}
				}, MeterReplaceDomain.class).stream().collect(Collectors.groupingBy(MeterReplaceDomain::getMeterId));

		Map<Long, List<WriteFilesDomain>> writeFilesMap = writeFilesList.stream()
				.collect(Collectors.groupingBy(WriteFilesDomain::getMeterId));

		meterReplaceMap.forEach((meterId, list) -> {// list 获取最新的换表记录

			List<WriteFilesDomain> writeFilesDomains = writeFilesMap.get(meterId);

			Date minDate = writeFilesDomains.stream().map(WriteFilesDomain::getLastWriteDate)
					.filter(lastWriteDate -> null != lastWriteDate).min((a, b) -> a.compareTo(b)).orElse(null);

			// OPERATE_TYPE = 1 装表
			Map<String, MeterReplaceDomain> installMap = list.stream().filter(mr -> {
				if (minDate == null) {
					return true;
				} else {
					return mr.getReplaceDate().after(minDate);
				}
			}).filter(mr -> mr.getOperateType() == FixedParametersConfig.OPERATE_TYPE_INSTALL)
					.sorted(Comparator.comparing(MeterReplaceDomain::getCreateDate)).collect(Collectors.toMap(mr -> {
						return mr.getPowerDirection() + "#" + mr.getFunctionCode();
					}, k -> k, (k, v) -> {
						if (k.getReplaceDate().after(v.getReplaceDate())) {
							return k;
						} else {
							return v;
						}
					}));

			installMap.values().forEach(mr -> {

				// 电能表
				writeFilesDomains.stream()

						.filter(w -> w.getMeterAssetsId().compareTo(mr.getMeterAssetsId()) == 0)
						.filter(w -> w.getFunctionCode().compareTo(mr.getFunctionCode()) == 0)
						.filter(w -> w.getPowerDirection().compareTo(mr.getPowerDirection()) == 0).forEach(w -> {
							switch (mr.getPowerDirection() * 100 + mr.getFunctionCode() * 10 + w.getTimeSeg()) {
							case 110:// 正向有功总
								w.setStartNum(mr.getP1r0() == null ? BigDecimal.ZERO : mr.getP1r0());
								break;

							case 111:// 正向有功峰
								w.setStartNum(mr.getP1r1() == null ? BigDecimal.ZERO : mr.getP1r1());
								break;
							case 112:// 正向有功平
								w.setStartNum(mr.getP1r2() == null ? BigDecimal.ZERO : mr.getP1r2());
								break;
							case 113:// 正向有功谷
								w.setStartNum(mr.getP1r3() == null ? BigDecimal.ZERO : mr.getP1r3());
								break;
							case 114:// 正向有功尖
								w.setStartNum(mr.getP1r4() == null ? BigDecimal.ZERO : mr.getP1r4());
								break;
							case 120:// 正向无功总
								w.setStartNum(mr.getP3r0() == null ? BigDecimal.ZERO : mr.getP3r0());
								break;
							case 220:// 反向无功总
								w.setStartNum(mr.getP4r0() == null ? BigDecimal.ZERO : mr.getP4r0());
								break;

							default:
								// 错误的
								;
							}

						});

			});

			// OPERATE_TYPE = 2 拆表。只有拆表的时候才有换表电量
			Map<String, List<MeterReplaceDomain>> dismantleStream = list.stream()
					.filter(mr -> mr.getOperateType() == FixedParametersConfig.OPERATE_TYPE_DISMANTLE)
					.filter(mr -> mr.getEquipmentType() == FixedParametersConfig.EQUIPMENT_TYPE_1)
					.collect(Collectors.groupingBy(mr -> {
						return mr.getPowerDirection() + "#" + mr.getFunctionCode();
					}));

			dismantleStream.forEach((key, dismantleList) -> {
				writeFilesDomains.stream().filter(w -> {
					String pf = w.getPowerDirection() + "#" + w.getFunctionCode();
					return key.equals(pf);
				}).forEach(w -> {
					BigDecimal changePower = dismantleList.stream().filter(d -> d.getP5r0() != null)
							.map(MeterReplaceDomain::getP5r0).reduce(BigDecimal.ZERO, BigDecimal::add);
					w.addChgPower(changePower);
				});

			});

		});

	}

}
