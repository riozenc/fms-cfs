/**
 * Author : czy
 * Date : 2019年4月13日 上午10:18:45
 * Title : com.riozenc.cfs.webapp.mrm.dao.MeterRelationDAO.java
 *
**/
package org.fms.cfs.server.webapp.mrm.e.dao;

import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.fms.cfs.common.config.MongoCollectionConfig;
import org.fms.cfs.common.webapp.domain.MeterRelationDomain;

import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.WriteModel;
import com.riozenc.titanTool.annotation.TransactionDAO;
import com.riozenc.titanTool.mongo.dao.MongoDAOSupport;
import com.riozenc.titanTool.spring.webapp.dao.AbstractTransactionDAOSupport;
import com.riozenc.titanTool.spring.webapp.dao.BaseDAO;

@TransactionDAO
public class MeterRelationDAO extends AbstractTransactionDAOSupport
		implements MongoDAOSupport, BaseDAO<MeterRelationDomain> {

	@Override
	public int insert(MeterRelationDomain t) {
		// TODO Auto-generated method stub
		return getPersistanceManager().insert(getNamespace() + ".insert", t);
	}

	@Override
	public int delete(MeterRelationDomain t) {
		// TODO Auto-generated method stub
		return getPersistanceManager().delete(getNamespace() + ".delete", t);
	}

	@Override
	public int update(MeterRelationDomain t) {
		// TODO Auto-generated method stub
		return getPersistanceManager().update(getNamespace() + ".update", t);
	}

	@Override
	public MeterRelationDomain findByKey(MeterRelationDomain t) {
		// TODO Auto-generated method stub
		return getPersistanceManager().load(getNamespace() + ".findByKey", t);
	}

	@Override
	public List<MeterRelationDomain> findByWhere(MeterRelationDomain t) {
		// TODO Auto-generated method stub
		return getPersistanceManager().find(getNamespace() + ".findByWhere", t);
	}

	public List<MeterRelationDomain> findByMeter(String meterIds) {
		// TODO Auto-generated method stub
		return getPersistanceManager().find(getNamespace() + ".findByMeter", meterIds);
	}

	public List<MeterRelationDomain> findByUser(String userIds) {
		// TODO Auto-generated method stub
		return getPersistanceManager().find(getNamespace() + ".findByUser", userIds);
	}

	public List<MeterRelationDomain> findByWriteSect(String writeSectIds) {
		// TODO Auto-generated method stub
		return getPersistanceManager().find(getNamespace() + ".findByWriteSect", writeSectIds);
	}

	public long initialize(String date, List<MeterRelationDomain> meterRelDomains) {
		MongoCollection<Document> mongoCollection = getMongoTemplate()
				.getCollection(getCollectionName(date, MongoCollectionConfig.ELECTRIC_METER_REL.name()));
		mongoCollection.drop();
		mongoCollection.insertMany(toDocuments(meterRelDomains));
		return mongoCollection.countDocuments();
	}

	public long initializeMany(String date, List<MeterRelationDomain> meterRelDomains) {
		MongoCollection<Document> mongoCollection = getMongoTemplate()
				.getCollection(getCollectionName(date, MongoCollectionConfig.ELECTRIC_METER_REL.name()));
		List<WriteModel<Document>> requests = updateMany(toDocuments(meterRelDomains), new MongoUpdateFilter() {
			@Override
			public Bson filter(Document param) {
				// TODO Auto-generated method stub
				return Filters.eq("id", param.get("id"));
			}
		}, true);
		BulkWriteResult bulkWriteResult = mongoCollection.bulkWrite(requests);
		return bulkWriteResult.getModifiedCount();
	}

}
