/**
 * Author : chizf
 * Date : 2020年7月22日 下午4:09:47
 * Title : com.riozenc.cfs.common.CollectionOperationUtils.java
 *
**/
package org.fms.cfs.common.utils;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CollectionOperationUtils {

	public static <T> Collection<T> intersection(Collection<T> list1, Collection<T> list2) {
		if (list1 == null || list2 == null) {
			throw new RuntimeException("两个list找交集功能的两个参数均不能为空.");
		}
		Collection<T> aList;
		Collection<T> bList;
		if (list1.size() > list2.size()) {
			aList = list1;
			bList = list2;
		} else {
			aList = list2;
			bList = list1;
		}
		return aList.stream().filter(a -> bList.contains(a)).collect(Collectors.toList());

	}

	public static <T> Collection<T> intersection(Collection<T> collection, Function<T, Boolean> function) {
		if (collection == null) {
			throw new RuntimeException("交集功能的参数均不能为空.");
		}
		return collection.stream().filter(a -> function.apply(a)).collect(Collectors.toList());
	}
}
