/**
 * Author : czy
 * Date : 2019年10月26日 下午6:27:50
 * Title : com.riozenc.cfs.webapp.cfm.filter.e.IntegrityCheckFilter.java
 *
**/
package org.fms.cfs.server.webapp.cfm.filter.e;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.json.JsonMode;
import org.bson.json.JsonWriterSettings;
import org.fms.cfs.common.config.FixedParametersConfig;
import org.fms.cfs.common.config.MongoCollectionConfig;
import org.fms.cfs.common.model.ECFModel;
import org.fms.cfs.common.model.TransformerCalculateModel;
import org.fms.cfs.common.model.exchange.CFModelExchange;
import org.fms.cfs.common.utils.MongoUtils;
import org.fms.cfs.common.webapp.domain.CommonParamDomain;
import org.fms.cfs.common.webapp.domain.MeterDomain;
import org.fms.cfs.common.webapp.domain.MeterMpedRelDomain;
import org.fms.cfs.common.webapp.domain.MeterRelationDomain;
import org.fms.cfs.common.webapp.domain.MeterRelationGraphDomain;
import org.fms.cfs.common.webapp.domain.PriceExecutionDomain;
import org.fms.cfs.common.webapp.domain.TransformerDomain;
import org.fms.cfs.common.webapp.domain.TransformerMeterRelationDomain;
import org.fms.cfs.server.webapp.cfm.filter.CFFilterChain;
import org.fms.cfs.server.webapp.cfm.filter.EcfFilter;

import com.mongodb.client.model.Filters;
import com.riozenc.titanTool.common.json.utils.GsonUtils;
import com.riozenc.titanTool.mongo.dao.MongoDAOSupport;

import reactor.core.publisher.Mono;

/**
 * 完整性检查
 * 
 * 已经发行的计量点不允许重新计算
 * 
 * @author czy
 *
 */
public class IntegrityCheckFilter implements EcfFilter, MongoDAOSupport {
	protected static final Log logger = LogFactory.getLog(IntegrityCheckFilter.class);

	@Override
	public int getOrder() {
		return HIGHEST_PRECEDENCE;
	}

	@Override
	public Mono<Void> filter(CFModelExchange<ECFModel> exchange, CFFilterChain filterChain) {

		// 根据变压器关系补充计量点
		checkPrice(exchange);
		// TODO 剔除公变
		supplementMeter3(exchange);
		// 根据套扣关系补充计量点
		supplementMeter(exchange);
		// 初始化虚拟表计量点的抄表记录
		initVirtualMeterData(exchange);
		// 计量点 基本电价ID转基本电价金额
		initBasicPrice(exchange);
		// 变压器计算模型
		initTransformerCalculateModel(exchange);

		// TODO 退补电费

		return filterChain.filter(exchange);
	}

	// 检查电价
	private void checkPrice(CFModelExchange<ECFModel> exchange) {

		exchange.getModels().values().parallelStream().filter(e -> e.getPriceType() == null).forEach(e -> {
			e.markProcessResult(this.getOrder(), false);
			e.addRemark("电价为空,请检查档案信息.");
		});

	}

	private void supplementMeter(CFModelExchange<ECFModel> exchange) {
		logger.info("***supplementMeter 数据量（前）=" + exchange.getModels().size());

		// 获取计量点ID集合
		List<Long> uncheckMeterIds = exchange.getModels().values().stream().map(ECFModel::getMeterId)
				.collect(Collectors.toList());

		// 根据现有的计量点找到有效的套扣关系
		List<MeterRelationDomain> meterRelationDomains = findMany(
				getCollectionName(exchange.getDate().toString(), MongoCollectionConfig.ELECTRIC_METER_REL.name()),
				new MongoFindFilter() {
					@Override
					public Bson filter() {
						return Filters.or(Filters.in("meterId", uncheckMeterIds),
								Filters.in("pMeterId", uncheckMeterIds));
					}
				}, MeterRelationDomain.class);

		// 根据meterId找到最上级计量点
//		List<Long> mongoMeterIds = new ArrayList<>();
//		Map<Long, List<MeterRelationGraphDomain>> topMeterMap = new HashMap<>();
		List<Long> mongoMeterIds = Collections.synchronizedList(new ArrayList<>());
		Map<Long, List<MeterRelationGraphDomain>> topMeterMap = Collections.synchronizedMap(new HashMap<>());
		meterRelationDomains.parallelStream().forEach(mr -> {
			// 跳过已经查询到的计量点
			if (mongoMeterIds.contains(mr.getMeterId())) {
				return;
			}
			// 获取计量点关系的顶端计量点ID
			Set<Long> ids = getTopMeter(exchange.getDate().toString(), mr.getMeterId());
			ids.forEach(i -> {
				List<MeterRelationGraphDomain> meterRelationGraphDomains = getBottomMeterRelation(
						exchange.getDate().toString(), i);
				// 记录顶端计量点及下级关系
				topMeterMap.put(i, meterRelationGraphDomains);
				meterRelationGraphDomains.forEach(mrg -> {
					mongoMeterIds.add(mrg.getpMeterId());
					mongoMeterIds.add(mrg.getMeterId());

					mrg.getRel().forEach(r -> {
						mongoMeterIds.add(r.getpMeterId());
						mongoMeterIds.add(r.getMeterId());
					});
				});
			});
		});

		// 检查计量点列表是否都存在，若不存在，则查询计量点及抄表单信息，若已抄表，则将计量点和抄表单信息加入到列表中，若未抄表，则整体移除计算

		// 差集
		List<String> differenceSet = mongoMeterIds.parallelStream().filter(Objects::nonNull)
				.filter(i -> !uncheckMeterIds.contains(i)).map(meterId -> {
					return MongoUtils.createObjectId(meterId, exchange.getSn());
				}).collect(Collectors.toList());

		// 将差集中的计量点从mongo补充到exchane中
		List<MeterDomain> meterDomains = findMany(
				getCollectionName(exchange.getDate().toString(), MongoCollectionConfig.ELECTRIC_METER.name()),
				new MongoFindFilter() {
					@Override
					public Bson filter() {
						return Filters.in("_id", differenceSet);
					}
				}, MeterDomain.class);

		meterDomains.forEach(m -> {
			ECFModel ecfModel = new ECFModel(m, exchange.getDate().toString(), exchange.getSn());
			exchange.getModels().put(m.getId(), ecfModel);
		});

		// 标记顶点计量点及关系
		topMeterMap.entrySet().forEach(e -> {
			exchange.getModels().get(e.getKey()).setTop(true);
			exchange.getModels().get(e.getKey()).setBottomMeterRelation(e.getValue());// 下级关系
		});

		logger.info("***supplementMeter 数据量（后）=" + exchange.getModels().size());

	}
	
	public void supplementMeter3(CFModelExchange<ECFModel> exchange) {

		logger.info("***supplementMeter3 数据量（前）=" + exchange.getModels().size());
		// 获取计量点ID集合
		List<Long> meterIds = exchange.getModels().values().stream().map(ECFModel::getMeterId)
				.collect(Collectors.toList());

		List<TransformerMeterRelationDomain> transformerMeterRelationDomains = findMany(
				getCollectionName(exchange.getDate().toString(), MongoCollectionConfig.TRANSFORMER_METER_REL.name()),
				new MongoFindFilter() {
					@Override
					public Bson filter() {
						return Filters.and(Filters.in("meterId", meterIds),
								Filters.eq("msType", FixedParametersConfig.MS_MODE_2));
					}
				}, TransformerMeterRelationDomain.class);

		List<Long> transformerIds = transformerMeterRelationDomains.stream()
				.map(TransformerMeterRelationDomain::getTransformerId).collect(Collectors.toList());

		List<TransformerMeterRelationDomain> transformerMeterRelationDomains2 = findMany(
				getCollectionName(exchange.getDate().toString(), MongoCollectionConfig.TRANSFORMER_METER_REL.name()),
				new MongoFindFilter() {
					@Override
					public Bson filter() {
						return Filters.and(Filters.in("transformerId", transformerIds),
								Filters.eq("msType", FixedParametersConfig.MS_MODE_2));
					}
				}, TransformerMeterRelationDomain.class);

		List<Long> allMeterIds = transformerMeterRelationDomains2.stream()
				.map(TransformerMeterRelationDomain::getMeterId).collect(Collectors.toList());

		// 差集
		List<Long> differenceSet = allMeterIds.stream().filter(Objects::nonNull).filter(i -> !meterIds.contains(i))
				.collect(Collectors.toList());

		// 将差集中的计量点从mongo补充到exchane中
		List<MeterDomain> meterDomains = findMany(
				getCollectionName(exchange.getDate().toString(), MongoCollectionConfig.ELECTRIC_METER.name()),
				new MongoFindFilter() {
					@Override
					public Bson filter() {
						return Filters.in("id", differenceSet);
					}
				}, MeterDomain.class);

		meterDomains.forEach(m -> {
			ECFModel ecfModel = new ECFModel(m, exchange.getDate().toString(), exchange.getSn());
			exchange.getModels().put(m.getId(), ecfModel);
		});

		logger.info("***supplementMeter3 数据量（后）=" + exchange.getModels().size());
	}

	/**
	 * 根据变压器关系补充计量点
	 * 
	 * @param exchange
	 */
	private void supplementMeter2(CFModelExchange<ECFModel> exchange) {

		// 获取计量点ID集合
		List<Long> meterIds = exchange.getModels().values().stream().map(ECFModel::getMeterId)
				.collect(Collectors.toList());

		List<Document> documents = aggregate(
				getCollectionName(exchange.getDate().toString(), MongoCollectionConfig.TRANSFORMER_METER_REL.name()),
				new MongoAggregateLookupFilter() {

					@Override
					public List<? extends Bson> getPipeline() {
						List<Bson> pipeLine = new LinkedList<>();
						pipeLine.add(this.getMatch());
						pipeLine.add(this.getLookup());
						pipeLine.add(new Document("$unwind", "$transformerMeterRel"));
						pipeLine.add(new Document("$project", Document.parse("{\"transformerMeterRel\":1,\"_id\":0}")));
						return pipeLine;
					}

					@Override
					public Bson setMatch() {
						return Document.parse("{$expr:{ $in: [ '$meterId'," + meterIds + " ] }}");
					}

					@Override
					public Bson setLookup() {
						Document document = new Document();
						document.put("from", getCollectionName(exchange.getDate().toString(),
								MongoCollectionConfig.TRANSFORMER_METER_REL.name()));
						document.put("localField", "transformerId");
						document.put("foreignField", "transformerId");
						document.put("as", "transformerMeterRel");
						return document;
					}
				});

		List<Document> result = documents.stream().map(d -> {
			return d.get("transformerMeterRel", Document.class);
		}).collect(Collectors.toList());

		List<Long> allMeterIds = GsonUtils
				.readValueToList(GsonUtils.toJson(result), TransformerMeterRelationDomain.class).stream()
				.map(TransformerMeterRelationDomain::getMeterId).collect(Collectors.toList());

		// 差集
		List<Long> differenceSet = allMeterIds.stream().filter(Objects::nonNull).filter(i -> !meterIds.contains(i))
				.collect(Collectors.toList());

		// 将差集中的计量点从mongo补充到exchane中
		List<MeterDomain> meterDomains = findMany(
				getCollectionName(exchange.getDate().toString(), MongoCollectionConfig.ELECTRIC_METER.name()),
				new MongoFindFilter() {
					@Override
					public Bson filter() {
						return Filters.in("id", differenceSet);
					}
				}, MeterDomain.class);

		meterDomains.forEach(m -> {
			ECFModel ecfModel = new ECFModel(m, exchange.getDate().toString(), exchange.getSn());
			exchange.getModels().put(m.getId(), ecfModel);
		});

	}

	private Set<Long> getTopMeter(String date, Long meterId) {
		Set<Long> meterIds = new HashSet<>();
		List<Document> documents = aggregate(getCollectionName(date, MongoCollectionConfig.ELECTRIC_METER_REL.name()),
				new MongoAggregateGraphLookupFilter() {
					@Override
					public List<? extends Bson> getPipeline() {
						List<Bson> pipeLine = new LinkedList<>();
						pipeLine.add(getMatch());
						pipeLine.add(getGraphLookup());
						return pipeLine;
					}

					@Override
					public Bson setMatch() {
						Document group = new Document();
						group.put("meterId", meterId);
						return group;
					}

					@Override
					public Bson setGraphLookup() {
						Document graphLookup = new Document();
						graphLookup.put("from",
								getCollectionName(date, MongoCollectionConfig.ELECTRIC_METER_REL.name()));
						graphLookup.put("startWith", "$pMeterId");
						graphLookup.put("connectFromField", "pMeterId");
						graphLookup.put("connectToField", "meterId");
						graphLookup.put("depthField", "numConnections");
						graphLookup.put("as", "rel");
						return graphLookup;
					}
				});
		if (!documents.isEmpty()) {
			documents.forEach(d -> {

				if (!d.getList("rel", Document.class).isEmpty()) {
					meterIds.add(d.getList("rel", Document.class).stream()
//							.max(Comparator.comparingInt(i -> i.getInteger("numConnections"))).get()
							.max(Comparator.comparingLong(i -> i.getLong("numConnections"))).get()
							.getInteger("pMeterId").longValue());
				} else {
					// 没有其他关系，自身关系就是最上级
					meterIds.add(d.getInteger("pMeterId").longValue());
				}
			});
		}
		return meterIds;
	}

	/**
	 * 获取下级计量点ID
	 * 
	 * @param date
	 * @param pMeterId
	 * @return
	 */
//	public LinkedHashSet<Long> getBottomMeterRelation(String date, Long pMeterId) {

	public List<MeterRelationGraphDomain> getBottomMeterRelation(String date, Long pMeterId) {
		List<Document> result = aggregate(getCollectionName(date, MongoCollectionConfig.ELECTRIC_METER_REL.name()),
				new MongoAggregateGraphLookupFilter() {
					@Override
					public List<? extends Bson> getPipeline() {
						List<Bson> pipeLine = new LinkedList<>();
						pipeLine.add(getMatch());
						pipeLine.add(getGraphLookup());
						return pipeLine;
					}

					@Override
					public Bson setMatch() {
						Document group = new Document();
						group.put("pMeterId", pMeterId);
						return group;
					}

					@Override
					public Bson setGraphLookup() {
						Document graphLookup = new Document();
						graphLookup.put("from",
								getCollectionName(date, MongoCollectionConfig.ELECTRIC_METER_REL.name()));
						graphLookup.put("startWith", "$meterId");
						graphLookup.put("connectFromField", "meterId");
						graphLookup.put("connectToField", "pMeterId");
						graphLookup.put("depthField", "numConnections");
						graphLookup.put("as", "rel");
						return graphLookup;
					}
				});

		List<MeterRelationGraphDomain> meterRelationGraphDomains = result.stream().map(r -> {
			return GsonUtils.readValue(r.toJson(JsonWriterSettings.builder().outputMode(JsonMode.RELAXED).build()),
					MeterRelationGraphDomain.class);
		}).collect(Collectors.toList());

		return meterRelationGraphDomains;
	}

	/**
	 * 初始化计量点的抄表记录
	 */
	private void initVirtualMeterData(CFModelExchange<ECFModel> exchange) {

		// 获取时段 TIME_SEG
		List<CommonParamDomain> timeSegList = findMany(
				getCollection(exchange.getDate().toString(), MongoCollectionConfig.SYSTEM_COMMON.name()),
				new MongoFindFilter() {
					@Override
					public Bson filter() {
						return Filters.eq("type", "TIME_SEG");
					}
				}, CommonParamDomain.class);

		// 初始化虚拟表的抄表记录
		List<MeterMpedRelDomain> virtualMeterMpedRelList = findMany(
				getCollectionName(exchange.getDate().toString(), MongoCollectionConfig.METER_MPED_REL.name()),
				new MongoFindFilter() {
					@Override
					public Bson filter() {
						return Filters.and(Filters.eq("functionCode", FixedParametersConfig.FUNCTION_CODE_3),
								Filters.in("meterId", exchange.getModels().keySet()));// 虚拟表
					}
				}, MeterMpedRelDomain.class);
		virtualMeterMpedRelList.forEach(v -> {
			ECFModel model = exchange.getModels().get(v.getMeterId());
			model.initVirtualMeterData(timeSegList);
		});
	}

	private void initBasicPrice(CFModelExchange<ECFModel> exchange) {

		Map<Long, List<PriceExecutionDomain>> priceExecutionMap = findMany(
				getCollectionName(exchange.getDate().toString(), MongoCollectionConfig.PRICE_EXECUTION.name()),
				new MongoFindFilter() {
					@Override
					public Bson filter() {
						return new Document();
					}
				}, PriceExecutionDomain.class).stream()
						.collect(Collectors.groupingBy(PriceExecutionDomain::getPriceTypeId));

		exchange.getModels().values().parallelStream()
				.filter(e -> e.getBasicMoneyFlag() != null && e.getBasicMoneyFlag() != 0).forEach(e -> {
					if (e.getBasicPriceId() != null) {
						PriceExecutionDomain priceExecutionDomain = priceExecutionMap.get(e.getBasicPriceId()).get(0);
						if (priceExecutionDomain != null) {
							e.setBasicPrice(priceExecutionDomain.getPrice());
						} else {
							e.addRemark("计量点" + e.getMeterId() + "的基本电价" + e.getBasicPriceId() + "不存在");
							e.markProcessResult(getOrder(), false);
						}
					} else {
						e.addRemark("基本电价未维护.");
						e.markProcessResult(getOrder(), false);
					}
				});
	}

	private void initTransformerCalculateModel(CFModelExchange<ECFModel> exchange) {
		Map<Long, ECFModel> ecfModelMap = exchange.getModels();
		List<TransformerCalculateModel> transformerCalculateModels = Collections.synchronizedList(new LinkedList<>());
		// 获取变压器与计量点关系
		List<TransformerMeterRelationDomain> transformerMeterRelaDomains = findMany(
				getCollectionName(exchange.getDate().toString(), MongoCollectionConfig.TRANSFORMER_METER_REL.name()),
				new MongoFindFilter() {
					@Override
					public Bson filter() {
						return Filters.in("meterId", exchange.getModels().keySet());
					}
				}, TransformerMeterRelationDomain.class);

		// 检查变压器与计量点关系属性值
		transformerMeterRelaDomains.parallelStream().forEach(tmr -> {

			if (tmr.getTransLostType() == null) {
				ecfModelMap.get(tmr.getMeterId()).markProcessResult(getOrder(), false);
				ecfModelMap.get(tmr.getMeterId())
						.addRemark(ecfModelMap.get(tmr.getMeterId()).getMeterNo() + " 该计量点变损分摊方式属性异常,请检查.");
				throw new RuntimeException(ecfModelMap.get(tmr.getMeterId()).getMeterNo() + " 该计量点变损分摊方式属性异常,请检查.");
			}
		});

		// 获取变压器
		Map<Long, TransformerDomain> transformerMap = findMany(
				getCollection(exchange.getDate().toString(), MongoCollectionConfig.TRANSFORMER_INFO.name()),
				new MongoFindFilter() {
					@Override
					public Bson filter() {
						return Filters.in("id", transformerMeterRelaDomains.stream()
								.map(TransformerMeterRelationDomain::getTransformerId).collect(Collectors.toList()));
					}
				}, TransformerDomain.class).stream().collect(Collectors.toMap(TransformerDomain::getId, v -> v));

		// 通过变压器组号进行组合
		Map<String, List<TransformerMeterRelationDomain>> tmrGroupMap = transformerMeterRelaDomains.parallelStream()
				.filter(tmr -> tmr.getTransformerGroupNo() != null && !tmr.getTransformerGroupNo().isEmpty())
				.collect(Collectors.groupingBy(TransformerMeterRelationDomain::getTransformerGroupNo));

		// 将变压器组 封装成一个变压器计算模型
		tmrGroupMap.forEach((groupNo, transformerMeterRelaList) -> {
			List<TransformerDomain> transformerList = new ArrayList<>();
			transformerMeterRelaList.forEach(tmr -> {
				transformerList.add(transformerMap.get(tmr.getTransformerId()));
			});
			TransformerCalculateModel transformerCalculateModel = new TransformerCalculateModel(transformerList,
					transformerMeterRelaList, new Long(groupNo));
			transformerCalculateModels.add(transformerCalculateModel);
		});

		// 将单个变压器封装成一个变压器计算模型
		transformerMeterRelaDomains.parallelStream()
				.filter(tmr -> tmr.getTransformerGroupNo() == null || tmr.getTransformerGroupNo().isEmpty())
				.collect(Collectors.groupingBy(TransformerMeterRelationDomain::getTransformerId))
				.forEach((id, transformerMeterRelaList) -> {

					TransformerCalculateModel transformerCalculateModel = new TransformerCalculateModel(
							transformerMap.get(id), transformerMeterRelaList);
					transformerCalculateModels.add(transformerCalculateModel);
				});

		// 组合 计量点 对应的变压器
		Map<Long, List<ECFModel>> tmMap = transformerMeterRelaDomains.parallelStream()
				.collect(Collectors.groupingBy(t -> {
					if (t.getTransformerGroupNo() != null) {
						return new Long(t.getTransformerGroupNo());
					} else {
						return t.getTransformerId();
					}
				}, Collectors.mapping(t -> {
					ECFModel temp = ecfModelMap.get(t.getMeterId());
					temp.setTransformerLossType(t.getTransLostType());// 变损分摊方式
					temp.setTransformerMeterRelationDomain(t);
					return ecfModelMap.get(t.getMeterId());
				}, Collectors.toList())));

		transformerCalculateModels.forEach(transformerCalculateModel -> {
			tmMap.get(transformerCalculateModel.getKey()).stream().forEach(ecfModel -> {
				// 赋值变压器计算模型
				ecfModel.setTransformerCalculateModel(transformerCalculateModel);

			});
		});

	}

}
