/**
 *    Auth:riozenc
 *    Date:2019年3月12日 下午7:15:06
 *    Title:com.riozenc.cim.webapp.service.IUserService.java
 **/
package org.fms.cfs.common.webapp.service;

import java.util.List;

import org.fms.cfs.common.webapp.domain.UserDomain;

import com.riozenc.titanTool.spring.webapp.service.BaseService;

public interface IUserService extends BaseService<UserDomain> {

	public List<UserDomain> getUserAllInfo(UserDomain userDomain);

	public long initialize(String date);
}
