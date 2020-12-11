/**
 * Author : czy
 * Date : 2019年4月13日 下午3:01:20
 * Title : com.riozenc.cfs.webapp.mrm.handler.WritePowerHandler.java
 *
**/
package org.fms.cfs.server.webapp.cfm.filter.e;

import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bson.conversions.Bson;
import org.fms.cfs.common.config.MongoCollectionConfig;
import org.fms.cfs.common.model.ECFModel;
import org.fms.cfs.common.model.MeterDataModel;
import org.fms.cfs.common.model.exchange.CFModelExchange;
import org.fms.cfs.common.webapp.domain.WriteFilesDomain;
import org.fms.cfs.server.webapp.cfm.filter.CFFilterChain;
import org.fms.cfs.server.webapp.cfm.filter.EcfFilter;

import com.mongodb.client.model.Filters;
import com.riozenc.titanTool.mongo.dao.MongoDAOSupport;

import reactor.core.publisher.Mono;

/**
 * 桂东电力 抄见电量
 * 
 * @author czy
 *
 */
public class WritePowerFilter implements EcfFilter, MongoDAOSupport {
	private Log logger = LogFactory.getLog(WritePowerFilter.class);

	@Override
	public int getOrder() {
		return 10;
	}

	@Override
	public Mono<Void> filter(CFModelExchange<ECFModel> exchange, CFFilterChain filterChain) {
		logger.info("处理抄见电量...");

		// 初始化
		meterReading(exchange);// 读取抄表数据
		
		

		Collection<ECFModel> ecfModels = exchange.getModels().values();

		// 0.获取表码数据
		// 1.换表记录
		// 2.止码-起码，并处理满码情况
		// 3.抄见电量=度差*倍率

		ecfModels.parallelStream().forEach(ecfModel -> {

			// 检查起码止码
			ecfModel.getMeterData().stream().forEach(m -> {

				if (m.getEndNum() == null || m.getStartNum() == null) {

					ecfModel.markProcessResult(getOrder(), false);
					ecfModel.addRemark(ecfModel.getMeterNo() + " 计量点起码止码存着异常,请检查(起码=" + m.getStartNum() + ",止码="
							+ m.getEndNum() + ").");
					throw new RuntimeException(ecfModel.getMeterNo() + " 计量点起码止码存着异常,请检查(起码=" + m.getStartNum() + ",止码="
							+ m.getEndNum() + ").");
				}

				m.getThreeInOne().forEach(mm -> {

					if (mm.getEndNum() == null || mm.getStartNum() == null) {

						ecfModel.markProcessResult(getOrder(), false);
						ecfModel.addRemark(ecfModel.getMeterNo() + " 计量点起码止码存着异常,请检查(起码=" + mm.getStartNum() + ",止码="
								+ mm.getEndNum() + ").");
						throw new RuntimeException(ecfModel.getMeterNo() + " 计量点起码止码存着异常,请检查(起码=" + mm.getStartNum()
								+ ",止码=" + mm.getEndNum() + ").");
					}
				});

			});

			compensatingPower(ecfModel);
			ecfModel.computeDiffNum();// 度差
			ecfModel.computeReadPower();// 抄见

			// 合并成一个计量点

		});

		return filterChain.filter(exchange);

	}

	/**
	 * 根据计量点获取抄表单，计算抄见电量
	 * 
	 * @param exchange
	 */
	private void meterReading(CFModelExchange<ECFModel> exchange) {

		List<WriteFilesDomain> writeFilesDomains = findMany(
				getCollection(exchange.getDate().toString(), MongoCollectionConfig.WRITE_FILES.name()),
				new MongoFindFilter() {
					@Override
					public Bson filter() {
						return Filters.and(Filters.eq("mon", exchange.getDate()), Filters.eq("sn", exchange.getSn()),
								Filters.in("meterId", exchange.getModels().keySet()));
					}
				}, WriteFilesDomain.class);

		// 已抄表的进行计算
		writeFilesDomains.parallelStream().filter(w -> w.getWriteFlag() != null && w.getWriteFlag() == 1).forEach(w -> {
			ECFModel model = exchange.getModels().get(w.getMeterId());
			model.mergeMeterData(new MeterDataModel(w));

		});

		// 剔除(标记)没有抄表记录的计量点
		writeFilesDomains.parallelStream().filter(w -> w.getWriteFlag() == null || w.getWriteFlag() == 0).forEach(w -> {
			ECFModel model = exchange.getModels().get(w.getMeterId());
			model.addRemark(model.getMeterNo() + "不存在抄表记录[" + w.get_id() + "].");
			model.markProcessResult(getOrder(), false);
		});
	}

	/**
	 * 退补电量
	 * 
	 * @return
	 */
	public ECFModel compensatingPower(ECFModel ecfModel) {
		// 查退补记录

		ecfModel.getMeterData().stream().forEach(m -> {

			m.setCompensatingPower(null);

		});

		return ecfModel;
	}

}
