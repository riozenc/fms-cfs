/**
 * Author : czy
 * Date : 2019年4月22日 下午4:00:03
 * Title : com.riozenc.cfs.webapp.mrm.e.dao.MeterReplaceDAO.java
 *
**/
package org.fms.cfs.server.webapp.mrm.e.dao;

import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.fms.cfs.common.config.MongoCollectionConfig;
import org.fms.cfs.common.webapp.domain.MeterReplaceDomain;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.WriteModel;
import com.riozenc.titanTool.annotation.TransactionDAO;
import com.riozenc.titanTool.mongo.dao.MongoDAOSupport;
import com.riozenc.titanTool.spring.webapp.dao.AbstractTransactionDAOSupport;
import com.riozenc.titanTool.spring.webapp.dao.BaseDAO;

@TransactionDAO
public class MeterReplaceDAO extends AbstractTransactionDAOSupport
		implements MongoDAOSupport, BaseDAO<MeterReplaceDomain> {

	@Override
	public int insert(MeterReplaceDomain t) {
		// TODO Auto-generated method stub
		return getPersistanceManager().insert(getNamespace() + ".insert", t);
	}

	@Override
	public int delete(MeterReplaceDomain t) {
		// TODO Auto-generated method stub
		return getPersistanceManager().delete(getNamespace() + ".delete", t);
	}

	@Override
	public int update(MeterReplaceDomain t) {
		// TODO Auto-generated method stub
		return getPersistanceManager().update(getNamespace() + ".update", t);
	}

	@Override
	public MeterReplaceDomain findByKey(MeterReplaceDomain t) {
		// TODO Auto-generated method stub
		return getPersistanceManager().load(getNamespace() + ".findByKey", t);
	}

	@Override
	public List<MeterReplaceDomain> findByWhere(MeterReplaceDomain t) {
		// TODO Auto-generated method stub
		return getPersistanceManager().find(getNamespace() + ".findByWhere", t);
	}

	public List<MeterReplaceDomain> findByMeter(String meterIds) {
		return getPersistanceManager().find(getNamespace() + ".findByMeter", meterIds);
	}

	public List<MeterReplaceDomain> findByUser(String userIds) {
		return getPersistanceManager().find(getNamespace() + ".findByUser", userIds);
	}

	public List<MeterReplaceDomain> findByWriteSect(String writeSectIds) {
		return getPersistanceManager().find(getNamespace() + ".findByWriteSect", writeSectIds);
	}

	public long initialize(String date, List<MeterReplaceDomain> meterReplaceDomains) {
		MongoTemplate mongoTemplate = getMongoTemplate();
		mongoTemplate.getCollection(getCollectionName(date, MongoCollectionConfig.ELECTRIC_METER_REPLACE.name()))
				.drop();
		mongoTemplate.getCollection(getCollectionName(date, MongoCollectionConfig.ELECTRIC_METER_REPLACE.name()))
				.insertMany(toDocuments(meterReplaceDomains));// 参数批量入库
		return mongoTemplate.getCollection(getCollectionName(date, MongoCollectionConfig.ELECTRIC_METER_REPLACE.name()))
				.countDocuments();
	}

	public long initializeMany(String date, List<MeterReplaceDomain> meterReplaceDomains) {
		MongoTemplate mongoTemplate = getMongoTemplate();
		List<WriteModel<Document>> requests = updateMany(toDocuments(meterReplaceDomains), new MongoUpdateFilter() {
			@Override
			public Bson filter(Document param) {
				// TODO Auto-generated method stub
				return Filters.eq("id", param.get("id"));
			}
		}, true);
		BulkWriteResult bulkWriteResult = mongoTemplate
				.getCollection(getCollectionName(date, MongoCollectionConfig.ELECTRIC_METER_REPLACE.name()))
				.bulkWrite(requests);
		return bulkWriteResult.getModifiedCount();
	}

}
