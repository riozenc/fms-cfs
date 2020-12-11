/**
 * Author : czy
 * Date : 2019年11月1日 下午5:30:09
 * Title : com.riozenc.cfs.webapp.cfm.service.impl.ElectricBillServiceImpl.java
 *
**/
package org.fms.cfs.server.webapp.dsm.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bson.conversions.Bson;
import org.fms.cfs.common.config.MongoCollectionConfig;
import org.fms.cfs.common.webapp.domain.ArrearageDomain;
import org.fms.cfs.common.webapp.domain.BillingDataBusinessDomain;
import org.fms.cfs.common.webapp.domain.MeterDomain;
import org.fms.cfs.common.webapp.domain.MeterMoneyDomain;
import org.fms.cfs.common.webapp.domain.WriteFilesDomain;
import org.fms.cfs.common.webapp.service.IElectricBillService;
import org.fms.cfs.server.webapp.dsm.dao.BillingDataDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mongodb.client.model.Filters;
import com.riozenc.titanTool.mongo.dao.MongoDAOSupport.MongoDeleteFilter;

@Service
public class ElectricBillServiceImpl implements IElectricBillService {

	@Autowired
	private BillingDataDAO billingDataDAO;

	@Override
	public Map<String, List<?>> issue(BillingDataBusinessDomain billingDataBusinessDomain) {

		Map<Long, MeterDomain> meterMap = billingDataBusinessDomain.getMeterDomains().parallelStream()
				.collect(Collectors.toMap(MeterDomain::getId, k -> k));

		// 获取计量点电费明细
		List<MeterMoneyDomain> meterMoneyDomains = billingDataDAO.getMeterMoneyList(billingDataBusinessDomain);
		
		
		//getComputedMeter
		
//		List<MeterMoneyDomain> meterMoneyDomains2 = billingDataDAO.getMeterMoneyList(billingDataBusinessDomain.getDate().toString(), meterList);

		// 根据计量点电费明细生成欠费记录
		List<ArrearageDomain> arrearageDomains = meterMoneyDomains.stream().map(mmd -> {

			MeterDomain meterDomain = meterMap.get(mmd.getMeterId());

			return new ArrearageDomain(mmd, meterDomain);
		}).collect(Collectors.toList());

		List<WriteFilesDomain> writeFilesDomains = billingDataDAO.getWriteFiles(billingDataBusinessDomain);

		Map<String, List<?>> resultMap = new HashMap<>();

		resultMap.put("arrearageDomains", arrearageDomains);
		resultMap.put("writeFilesDomains", writeFilesDomains);
		resultMap.put("meterMoneyDomains", meterMoneyDomains);

		return resultMap;
	}

	@Override
	public int issueComplete(BillingDataBusinessDomain billingDataBusinessDomain,
			List<ArrearageDomain> arrearageDomains) {

		billingDataDAO.deleteMany(billingDataDAO.getCollectionName(billingDataBusinessDomain.getDate().toString(),
				MongoCollectionConfig.ARREARAGE_INFO.name()), new MongoDeleteFilter() {

					@Override
					public Bson filter() {
						// TODO Auto-generated method stub
						return Filters.in("_id",
								arrearageDomains.stream().map(ArrearageDomain::get_id).collect(Collectors.toList()));
					}
				});

		// 插入欠费记录
		int i = billingDataDAO.insertArrearage(billingDataBusinessDomain.getDate().toString(), arrearageDomains);

		// 根据计量点电费数据更新对应的计量点状态
		int u = billingDataDAO.updateMeterStatusByArrearage(billingDataBusinessDomain.getDate().toString(),
				arrearageDomains);

		return i % (u + 1);
	}

}
