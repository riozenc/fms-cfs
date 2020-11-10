package org.fms.cfs.server.webapp.mrm.e.dao;

import java.util.List;

import org.bson.Document;
import org.fms.cfs.common.webapp.domain.CommonParamDomain;

import com.mongodb.client.MongoCollection;
import com.riozenc.titanTool.annotation.TransactionDAO;
import com.riozenc.titanTool.mongo.dao.MongoDAOSupport;
import com.riozenc.titanTool.properties.Global;
import com.riozenc.titanTool.spring.webapp.dao.AbstractTransactionDAOSupport;
import com.riozenc.titanTool.spring.webapp.dao.BaseDAO;

@TransactionDAO
public class CommonParamDAO extends AbstractTransactionDAOSupport
		implements MongoDAOSupport, BaseDAO<CommonParamDomain> {
	private static final String PARAM = "PARAM";

	@Override
	public int insert(CommonParamDomain t) {
		// TODO Auto-generated method stub
		return getPersistanceManager().insert(getNamespace() + ".insert", t);
	}

	@Override
	public int delete(CommonParamDomain t) {
		// TODO Auto-generated method stub
		return getPersistanceManager().delete(getNamespace() + ".delete", t);
	}

	@Override
	public int update(CommonParamDomain t) {
		// TODO Auto-generated method stub
		return getPersistanceManager().update(getNamespace() + ".update", t);

	}

	@Override
	public CommonParamDomain findByKey(CommonParamDomain t) {
		// TODO Auto-generated method stub
		return getPersistanceManager().load(getNamespace() + ".findByKey", t);
	}

	@Override
	public List<CommonParamDomain> findByWhere(CommonParamDomain t) {
		// TODO Auto-generated method stub
		return getPersistanceManager().find(getNamespace() + ".findByWhere", t);
	}

	public List<CommonParamDomain> getAllType(CommonParamDomain t) {
		// TODO Auto-generated method stub
		return getPersistanceManager().find(getNamespace() + ".getAllType", t);
	}

	public List<CommonParamDomain> getAllTypeForList(String t) {
		// TODO Auto-generated method stub
		return getPersistanceManager().find(getNamespace() + ".getAllTypeForList", t);
	}

	public int nextMon(CommonParamDomain t) {
		// TODO Auto-generated method stub
		return getPersistanceManager().update(getNamespace() + ".nextMon", t);

	}

	public long paramsInit(String date, List<Document> documents) {
		MongoCollection<Document> mongoCollection = getMongoTemplate(Global.getConfig("mongo.databaseName"))
				.getCollection(getCollectionName(date, PARAM));
		mongoCollection.drop();
		mongoCollection.insertMany(documents);
		return mongoCollection.countDocuments();
	}
}
