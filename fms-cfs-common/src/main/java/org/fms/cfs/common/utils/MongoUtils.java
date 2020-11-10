/**
 * Author : czy
 * Date : 2019年12月21日 上午10:19:34
 * Title : com.riozenc.cfs.common.mongo.MongoUtils.java
 *
**/
package org.fms.cfs.common.utils;

public final class MongoUtils {

	public static String createObjectId(Object... params) {
		StringBuilder stringBuilder = new StringBuilder();
		for (Object param : params) {
			stringBuilder.append(param).append("#");
		}
		stringBuilder.setLength(stringBuilder.length() - 1);
		return stringBuilder.toString();
	}

}
