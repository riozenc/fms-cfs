/**
 * Author : czy
 * Date : 2019年6月26日 下午7:08:03
 * Title : com.riozenc.cfs.webapp.mrm.filter.MeterInitFilter.java
 *
**/
package org.fms.cfs.server.webapp.mrm.filter.datainit;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.fms.cfs.common.config.FixedParametersConfig;
import org.fms.cfs.common.config.MongoCollectionConfig;
import org.fms.cfs.common.model.BillingDataInitModel;
import org.fms.cfs.common.model.exchange.InitModelExchange;
import org.fms.cfs.common.utils.CollectionOperationUtils;
import org.fms.cfs.common.webapp.domain.MeterDomain;
import org.fms.cfs.common.webapp.domain.MeterMpedRelDomain;
import org.fms.cfs.common.webapp.domain.UserDomain;
import org.fms.cfs.common.webapp.domain.WriteSectDomain;
import org.fms.cfs.server.webapp.mrm.filter.BillingDataInitFilter;
import org.fms.cfs.server.webapp.mrm.filter.InitFilterChain;

import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.WriteModel;
import com.riozenc.titanTool.mongo.dao.MongoDAOSupport;

import reactor.core.publisher.Mono;

/**
 * 计量点
 * 
 * @author czy
 *
 */
public class MeterInitFilter implements BillingDataInitFilter, MongoDAOSupport {

	@Override
	public int getOrder() {
		return 20;
	}

	@Override
	public Mono<Void> filter(InitModelExchange exchange, InitFilterChain filterChain) {
		BillingDataInitModel billingDataInitModel = (BillingDataInitModel) exchange.getModel();

		if (billingDataInitModel.getMeterDomains().size() == 0) {
			billingDataInitModel.addExecuteResult("电计量点数据为0,请检查电计量点档案.");
			return filterChain.filter(exchange);
		}

		List<Long> meterIds = billingDataInitModel.getMeterDomains().stream().map(MeterDomain::getId)
				.collect(Collectors.toList());

		// 删除已经发行的计量点
		List<Long> invalidMeterIds = findMany(
				getCollectionName(billingDataInitModel.getDate(), MongoCollectionConfig.ELECTRIC_METER.name()),
				new MongoFindFilter() {
					@Override
					public Bson filter() {
						return Filters.and(Filters.in("id", meterIds), Filters.eq("status", MeterDomain.ISSUED));
					}
				}, MeterDomain.class).stream().map(MeterDomain::getId).collect(Collectors.toList());

		// 重新赋值meterList和meterMeterAssetsRel
		billingDataInitModel.setMeterDomains(
				(List<MeterDomain>) CollectionOperationUtils.intersection(billingDataInitModel.getMeterDomains(), a -> {
					return !invalidMeterIds.contains(a.getId());
				}));

		billingDataInitModel.setMeterMpedRelDomains((List<MeterMpedRelDomain>) CollectionOperationUtils
				.intersection(billingDataInitModel.getMeterMpedRelDomains(), a -> {
					return !invalidMeterIds.contains(a.getMeterId());
				}));

		Map<Long, UserDomain> userMap = billingDataInitModel.getUserDomains().parallelStream()
				.collect(Collectors.toMap(UserDomain::getId, k -> k));

		List<MeterDomain> meterList = billingDataInitModel.getMeterDomains().stream()
				.filter(m -> userMap.keySet().contains(m.getUserId())).collect(Collectors.toList());

		List<Long> invalidMeterIdList = billingDataInitModel.getMeterDomains().stream()
				.filter(m -> m.getStatus() == 0 || !userMap.keySet().contains(m.getUserId())).map(MeterDomain::getId)
				.collect(Collectors.toList());

		logger.info("计量点初始化过程中,剔除无效计量点: " + invalidMeterIdList.size() + " 个。");

		deleteMany(getCollectionName(billingDataInitModel.getDate(), MongoCollectionConfig.ELECTRIC_METER.name()),
				new MongoDeleteFilter() {

					@Override
					public Bson filter() {
						return Filters.in("id", invalidMeterIdList);
					}
				});

		billingDataInitModel.setMeterDomains(meterList);

		Map<Long, WriteSectDomain> writeSectMap = findMany(
				getCollection(billingDataInitModel.getDate(), MongoCollectionConfig.WRITE_SECT.name()),
				new MongoFindFilter() {
					@Override
					public Bson filter() {
						return Filters.in("id", billingDataInitModel.getMeterDomains().stream()
								.map(MeterDomain::getWriteSectionId).collect(Collectors.toList()));
					}
				}, WriteSectDomain.class).stream().collect(Collectors.toMap(WriteSectDomain::getId, v -> v));

		List<WriteModel<Document>> meterMongoList = updateMany(
				toDocuments(billingDataInitModel.getMeterDomains(), new ToDocumentCallBack<MeterDomain>() {
					@Override
					public MeterDomain call(MeterDomain meterDomain) {

						meterDomain.setUserName(userMap.get(meterDomain.getUserId()).getUserName());
						meterDomain.setUserNo(userMap.get(meterDomain.getUserId()).getUserNo());
						meterDomain.setSn(billingDataInitModel.getSn());

						WriteSectDomain writeSectDomain = writeSectMap.get(meterDomain.getWriteSectionId());
						if (writeSectDomain == null) {
							billingDataInitModel.addExecuteResult("计量点：" + meterDomain.getMeterNo() + "抄表区段："
									+ meterDomain.getWriteSectionId() + "不存在！");
						} else {
							meterDomain.setWriteSectNo(writeSectDomain.getWriteSectNo());
							meterDomain.setWriteSectName(writeSectDomain.getWriteSectName());
							meterDomain.setBusinessPlaceCode(writeSectDomain.getBusinessPlaceCode());
							meterDomain.setWritorId(writeSectDomain.getWritorId());
						}

						meterDomain.createObjectId();
						return meterDomain;
					}
				}), new MongoUpdateFilter() {
					@Override
					public Bson filter(Document param) {

						return Filters.eq("_id", param.get("_id"));
//						return Filters.and(Filters.eq("id", param.get("id")), Filters.eq("sn", param.get("sn")));
					}
				}, true);

		BulkWriteResult meterWriteResult = getCollection(billingDataInitModel.getDate(),
				MongoCollectionConfig.ELECTRIC_METER.name()).bulkWrite(meterMongoList);

		billingDataInitModel.addExecuteResult(
				"计量点：" + (billingDataInitModel.getMeterDomains().size() - meterWriteResult.getModifiedCount()));
		return filterChain.filter(exchange);
	}

}
