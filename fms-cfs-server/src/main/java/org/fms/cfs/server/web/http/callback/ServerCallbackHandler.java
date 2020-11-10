/**
 * Author : czy
 * Date : 2019年11月3日 上午11:08:03
 * Title : com.riozenc.cfs.handler.ServerCallbackHandler.java
 *
**/
package org.fms.cfs.server.web.http.callback;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.riozenc.titanTool.spring.web.client.TitanTemplate;
import com.riozenc.titanTool.spring.web.client.TitanTemplate.TitanCallback;
import com.riozenc.titanTool.spring.web.http.HttpResult;

public class ServerCallbackHandler {

	private TitanTemplate titanTemplate;

	public ServerCallbackHandler(TitanTemplate titanTemplate) {
		this.titanTemplate = titanTemplate;
	}

	public HttpResult callback(String serverName, String callbackUrl, Object params) throws Exception {
		// 回调
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);

		TitanCallback<HttpResult> callback = titanTemplate.postCallBack(serverName, callbackUrl, httpHeaders, params,
				HttpResult.class);
		HttpResult httpResult = callback.call();
		return httpResult;
	}

}
