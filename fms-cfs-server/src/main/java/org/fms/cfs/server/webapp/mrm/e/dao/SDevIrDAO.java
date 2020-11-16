/**
 * Author : chizf
 * Date : 2020年11月14日 下午4:49:19
 * Title : org.fms.cfs.server.webapp.mrm.e.dao.SDevIrDAO.java
 *
**/
package org.fms.cfs.server.webapp.mrm.e.dao;

import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.fms.cfs.common.config.MongoCollectionConfig;
import org.fms.cfs.common.webapp.domain.SDevIrDomain;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.WriteModel;
import com.riozenc.titanTool.annotation.TransactionDAO;
import com.riozenc.titanTool.mongo.dao.MongoDAOSupport;
import com.riozenc.titanTool.mongo.dao.MongoDAOSupport.MongoUpdateFilter;
import com.riozenc.titanTool.spring.webapp.dao.AbstractTransactionDAOSupport;
import com.riozenc.titanTool.spring.webapp.dao.BaseDAO;

@TransactionDAO
public class SDevIrDAO extends AbstractTransactionDAOSupport implements MongoDAOSupport, BaseDAO<SDevIrDomain> {

	@Override
	public int insert(SDevIrDomain t) {
		return getPersistanceManager().insert(getNamespace() + ".insert", t);
	}

	@Override
	public int delete(SDevIrDomain t) {
		return getPersistanceManager().delete(getNamespace() + ".delete", t);
	}

	@Override
	public int update(SDevIrDomain t) {
		return getPersistanceManager().update(getNamespace() + ".update", t);
	}

	@Override
	public SDevIrDomain findByKey(SDevIrDomain t) {
		return getPersistanceManager().load(getNamespace() + ".findByKey", t);
	}

	@Override
	public List<SDevIrDomain> findByWhere(SDevIrDomain t) {
		return getPersistanceManager().find(getNamespace() + ".findByWhere", t);
	}
	
	public List<SDevIrDomain> findByMeter(String meterIds) {
		return getPersistanceManager().find(getNamespace() + ".findByMeter", meterIds);
	}

	public List<SDevIrDomain> findByUser(String userIds) {
		return getPersistanceManager().find(getNamespace() + ".findByUser", userIds);
	}

	public List<SDevIrDomain> findByWriteSect(String writeSectIds) {
		return getPersistanceManager().find(getNamespace() + ".findByWriteSect", writeSectIds);
	}

	public long initialize(String date, List<SDevIrDomain> meterReplaceDomains) {
		MongoTemplate mongoTemplate = getMongoTemplate();
		mongoTemplate.getCollection(getCollectionName(date, MongoCollectionConfig.S_DEV_IR.name()))
				.drop();
		mongoTemplate.getCollection(getCollectionName(date, MongoCollectionConfig.S_DEV_IR.name()))
				.insertMany(toDocuments(meterReplaceDomains));// 参数批量入库
		return mongoTemplate.getCollection(getCollectionName(date, MongoCollectionConfig.S_DEV_IR.name()))
				.countDocuments();
	}

	public long initializeMany(String date, List<SDevIrDomain> meterReplaceDomains) {
		MongoTemplate mongoTemplate = getMongoTemplate();
		List<WriteModel<Document>> requests = updateMany(toDocuments(meterReplaceDomains), new MongoUpdateFilter() {
			@Override
			public Bson filter(Document param) {
				return Filters.eq("id", param.get("id"));
			}
		}, true);
		BulkWriteResult bulkWriteResult = mongoTemplate
				.getCollection(getCollectionName(date, MongoCollectionConfig.S_DEV_IR.name()))
				.bulkWrite(requests);
		return bulkWriteResult.getModifiedCount();
	}

}