/**
 * Author : czy
 * Date : 2019年4月22日 下午5:20:48
 * Title : com.riozenc.cfs.webapp.mrm.e.service.impl.WriteFilesServiceImpl.java
 *
**/
package org.fms.cfs.server.webapp.mrm.e.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.fms.cfs.common.utils.MonUtils;
import org.fms.cfs.common.webapp.domain.ElectricMeterDomain;
import org.fms.cfs.common.webapp.domain.WriteFilesDomain;
import org.fms.cfs.common.webapp.service.IWriteFilesService;
import org.fms.cfs.server.webapp.mrm.e.dao.MeterDAO;
import org.fms.cfs.server.webapp.mrm.e.dao.MeterRelationDAO;
import org.fms.cfs.server.webapp.mrm.e.dao.WriteFilesDAO;
import org.springframework.util.CollectionUtils;

import com.riozenc.titanTool.annotation.TransactionDAO;
import com.riozenc.titanTool.annotation.TransactionService;
import com.riozenc.titanTool.mybatis.MybatisEntity;

@TransactionService
public class WriteFilesServiceImpl implements IWriteFilesService {

	@TransactionDAO("billing")
	private WriteFilesDAO writeFilesDAO;

	@TransactionDAO("cim")
	private MeterDAO meterDAO;

	@TransactionDAO("cim")
	private MeterRelationDAO meterRelDAO;

	@Override
	public int insert(WriteFilesDomain t) {
		// TODO Auto-generated method stub
		return writeFilesDAO.insert(t);
	}

	@Override
	public int delete(WriteFilesDomain t) {
		// TODO Auto-generated method stub
		return writeFilesDAO.delete(t);
	}

	@Override
	public int update(WriteFilesDomain t) {
		// TODO Auto-generated method stub
		return writeFilesDAO.update(t);
	}

	@Override
	public WriteFilesDomain findByKey(WriteFilesDomain t) {
		// TODO Auto-generated method stub
		return writeFilesDAO.findByKey(t);
	}

	@Override
	public List<WriteFilesDomain> findByWhere(WriteFilesDomain t) {
		// TODO Auto-generated method stub
		return writeFilesDAO.findByWhere(t);
	}

	/**
	 * 抄表单初始化
	 */
	public long initialize(String date) {
		List<ElectricMeterDomain> electricMeterDomains = meterDAO.findByWhere(new ElectricMeterDomain());
		if (CollectionUtils.isEmpty(electricMeterDomains))
			return 0;
		List<WriteFilesDomain> writeFilesDomains = new ArrayList<WriteFilesDomain>();
		electricMeterDomains.stream().forEach(e -> {
			writeFilesDomains.add(meter2WriteFile(date, e));
		});
		return writeFilesDAO.initialize(date, writeFilesDomains);
	}

	@Override
	public long initializeByMeter(String date, String meterIds) {
		// TODO Auto-generated method stub
		List<ElectricMeterDomain> electricMeterDomains = meterDAO.findByMeter(meterIds);
		if (CollectionUtils.isEmpty(electricMeterDomains))
			return 0;
		List<WriteFilesDomain> writeFilesDomains = new ArrayList<WriteFilesDomain>();
		electricMeterDomains.stream().forEach(e -> {
			writeFilesDomains.add(meter2WriteFile(date, e));
		});
		return writeFilesDAO.initializeMany(date, writeFilesDomains);
	}

	@Override
	public long initializeByUser(String date, String userIds) {
		// TODO Auto-generated method stub
		List<ElectricMeterDomain> electricMeterDomains = meterDAO.findByUser(userIds);
		if (CollectionUtils.isEmpty(electricMeterDomains))
			return 0;
		List<WriteFilesDomain> writeFilesDomains = new ArrayList<WriteFilesDomain>();
		electricMeterDomains.stream().forEach(e -> {
			writeFilesDomains.add(meter2WriteFile(date, e));
		});
		return writeFilesDAO.initializeMany(date, writeFilesDomains);
	}

	@Override
	public long initializeByWriteSect(String date, String writeSectIds) {
		// TODO Auto-generated method stub
		List<ElectricMeterDomain> electricMeterDomains = meterDAO.findByWriteSect(writeSectIds);
		if (CollectionUtils.isEmpty(electricMeterDomains))
			return 0;
		List<WriteFilesDomain> writeFilesDomains = new ArrayList<WriteFilesDomain>();
		electricMeterDomains.stream().forEach(e -> {
			writeFilesDomains.add(meter2WriteFile(date, e));
		});
		return writeFilesDAO.initializeMany(date, writeFilesDomains);
	}

	/**
	 * 抄表
	 */
	@Override
	public long meterReading(String date, ElectricMeterDomain electricMeterDomain) {
		// TODO Auto-generated method stub

		// 从采集系统获取数据
		List<?> dayDate;
		List<WriteFilesDomain> writeFilesDomains = new ArrayList<>();
		return writeFilesDAO.meterReading(date, writeFilesDomains);
	}

	/**
	 * 部分抄表
	 */
	@Override
	public long meterReadingMany(String date, String meterIds) {
		// TODO Auto-generated method stub

		return 0;
	}

	@Override
	public long check(String date) {

		// 获取上个月的数据
		List<WriteFilesDomain> oldDates = writeFilesDAO.findByWhere(MonUtils.getLastMon(date), null);
		// 获取本月数据
		List<WriteFilesDomain> newDates = writeFilesDAO.findByWhere(date, null);

		List<MybatisEntity> list = Collections.synchronizedList(new ArrayList<>());

		return 0;
	}

	@Override
	public long checkMany(String date, String meterIds) {

		return 0;

	}

	@Override
	public long computeMany(String date, String meterIds) {
		// TODO Auto-generated method stub

		return 0;

	}

	@Override
	public long manualMeterReading(String date, List<WriteFilesDomain> writeFilesDomains) {
		// TODO Auto-generated method stub

//		MeterRelDomain meterRelDomain = new MeterRelDomain();
//		meterRelDomain.setStatus((byte) 1);
//		List<MeterRelDomain> meterRelDomains = meterRelDAO.findByWhere(meterRelDomain);
		
		

		return 0;
	}

	private WriteFilesDomain meter2WriteFile(String date, ElectricMeterDomain electricMeterDomain) {
		WriteFilesDomain writeFilesDomain = new WriteFilesDomain();
		writeFilesDomain.setMeterId(electricMeterDomain.getId());
		writeFilesDomain.setMon(Integer.valueOf(date));
		writeFilesDomain.setInitDate(new Date());
		writeFilesDomain.setWriteFlag((byte) 0);
		return writeFilesDomain;
	}

}
