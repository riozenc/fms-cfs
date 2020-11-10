/**
 *    Auth:riozenc
 *    Date:2019年3月12日 下午7:16:33
 *    Title:com.riozenc.cim.webapp.service.impl.UserServiceImpl.java
 **/
package org.fms.cfs.server.webapp.mrm.e.service;

import java.io.IOException;
import java.util.List;

import org.fms.cfs.common.webapp.domain.UserDomain;
import org.fms.cfs.common.webapp.service.IUserService;
import org.fms.cfs.server.webapp.mrm.e.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.riozenc.titanTool.annotation.TransactionDAO;
import com.riozenc.titanTool.annotation.TransactionService;
import com.riozenc.titanTool.common.json.utils.JSONUtil;

@TransactionService
public class UserServiceImpl implements IUserService {

	@TransactionDAO("cim")
	private UserDAO userDAO;

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public int insert(UserDomain t) {
		// TODO Auto-generated method stub
		return userDAO.insert(t);
	}

	@Override
	public int delete(UserDomain t) {
		// TODO Auto-generated method stub
		return userDAO.delete(t);
	}

	@Override
	public int update(UserDomain t) {
		// TODO Auto-generated method stub
		return userDAO.update(t);
	}

	@Override
	public UserDomain findByKey(UserDomain t) {
		// TODO Auto-generated method stub
		return userDAO.findByKey(t);
	}

	@Override
	public List<UserDomain> findByWhere(UserDomain t) {
		// TODO Auto-generated method stub
		return userDAO.findByWhere(t);
	}

	@Override
	public List<UserDomain> getUserAllInfo(UserDomain userDomain) {
		// TODO Auto-generated method stub
		return userDAO.getUserAllInfo(userDomain);
	}

	public List<UserDomain> getCurrentMonUser() {

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(null,
				httpHeaders);

		String result = restTemplate.postForObject("http://CIM-SERVER/user?method=getUserAllInfo", requestEntity,
				String.class);

		try {
			List<UserDomain> userDomains = JSONUtil.readValue(result, new TypeReference<List<UserDomain>>() {
			});

			return userDomains;
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public long initialize(String date) {
		// TODO Auto-generated method stub

		List<UserDomain> userDomains = getCurrentMonUser();
		
		

		return 0;
	}

}
