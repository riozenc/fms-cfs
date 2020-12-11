/**
 * Author : czy
 * Date : 2019年11月2日 上午11:10:31
 * Title : com.riozenc.cfs.webapp.dsm.dao.BillingDataDAO.java
 *
**/
package org.fms.cfs.server.webapp.dsm.dao;

import java.util.List;
import java.util.stream.Collectors;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.fms.cfs.common.config.MongoCollectionConfig;
import org.fms.cfs.common.webapp.domain.ArrearageDomain;
import org.fms.cfs.common.webapp.domain.BillingDataBusinessDomain;
import org.fms.cfs.common.webapp.domain.MeterDomain;
import org.fms.cfs.common.webapp.domain.MeterMoneyDomain;
import org.fms.cfs.common.webapp.domain.WriteFilesDomain;
import org.springframework.stereotype.Repository;

import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.WriteModel;
import com.riozenc.titanTool.mongo.dao.MongoDAOSupport;

/**
 * 账单数据DAO
 * 
 * @author czy
 *
 */

@Repository
public class BillingDataDAO implements MongoDAOSupport {

	public int updateMeterStatusByArrearage(String date, List<ArrearageDomain> arrearageDomains) {
		List<WriteModel<Document>> meterWriteModels = updateMany(toDocuments(arrearageDomains),
				new MongoUpdateFilter() {
					@Override
					public Bson update(Document param) {
						return new Document("$set", new Document().append("status", MeterDomain.ISSUED));
					}

					@Override
					public Bson filter(Document param) {
						return Filters.and(Filters.eq("id", param.get("meterId")), Filters.eq("sn", param.get("sn")));
					}
				}, true);

		if (!meterWriteModels.isEmpty()) {
			BulkWriteResult userResult2 = getCollection(date, MongoCollectionConfig.ELECTRIC_METER.name())
					.bulkWrite(meterWriteModels);
			return userResult2.getModifiedCount();
		}
		return 0;
	}

	public List<MeterDomain> getComputedMeter(String date, List<MeterDomain> meterList) {
		List<MeterDomain> list = findMany(getCollectionName(date, MongoCollectionConfig.ELECTRIC_METER.name()),
				new MongoFindFilter() {
					@Override
					public Bson filter() {
						return Filters.and(Filters.eq("status", 2),
								Filters.in("_id", meterList.stream().peek(MeterDomain::createObjectId)
										.map(MeterDomain::get_id).collect(Collectors.toList())));
					}
				}, MeterDomain.class);

		return list;

	}

	public int insertArrearage(String date, List<ArrearageDomain> arrearageDomains) {

		List<WriteModel<Document>> arrearageWriteModels = insertMany(toDocuments(arrearageDomains));

		if (!arrearageWriteModels.isEmpty()) {
			BulkWriteResult userResult1 = getCollection(date, MongoCollectionConfig.ARREARAGE_INFO.name())
					.bulkWrite(arrearageWriteModels);
			return userResult1.getInsertedCount();
		}
		return 0;
	}

	public List<MeterMoneyDomain> getMeterMoneyList(String date, List<MeterDomain> meterList) {
		List<MeterMoneyDomain> meterMoneyDomains = findMany(
				getCollectionName(date, MongoCollectionConfig.ELECTRIC_METER_MONEY.name()), new MongoFindFilter() {

					@Override
					public Bson filter() {
						return Filters.in("meterId",
								meterList.stream().map(MeterDomain::getId).collect(Collectors.toList()));
					}
				}, MeterMoneyDomain.class);

		return meterMoneyDomains;
	}

	public List<MeterMoneyDomain> getMeterMoneyList(BillingDataBusinessDomain billingDataBusinessDomain) {
		List<MeterMoneyDomain> meterMoneyDomains = findMany(
				getCollectionName(billingDataBusinessDomain.getDate().toString(),
						MongoCollectionConfig.ELECTRIC_METER_MONEY.name()),
				new MongoFindFilter() {

					@Override
					public Bson filter() {
						return Filters.in("meterId", billingDataBusinessDomain.getMeterDomains().stream()
								.map(MeterDomain::getId).collect(Collectors.toList()));
					}
				}, MeterMoneyDomain.class);

		return meterMoneyDomains;
	}

	/**
	 * 获取抄表单数据
	 * 
	 * @return
	 */
	public List<WriteFilesDomain> getWriteFiles(BillingDataBusinessDomain billingDataBusinessDomain) {

		return findMany(getCollectionName(billingDataBusinessDomain.getDate().toString(),
				MongoCollectionConfig.WRITE_FILES.name()), new MongoFindFilter() {

					@Override
					public Bson filter() {
						return Filters.in("meterId", billingDataBusinessDomain.getMeterDomains().stream()
								.map(MeterDomain::getId).collect(Collectors.toList()));
					}
				}, WriteFilesDomain.class);

	}

}
