/**
 * Author : czy
 * Date : 2019年6月24日 下午2:26:30
 * Title : com.riozenc.cfs.webapp.mrm.e.service.IArchivesService.java
 *
**/
package org.fms.cfs.common.webapp.service;

import java.util.concurrent.Callable;

public interface IArchivesService {

	public Callable<?> archivesInit(String date);
}
