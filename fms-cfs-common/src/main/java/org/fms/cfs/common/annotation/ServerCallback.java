/**
 * Author : czy
 * Date : 2019年11月4日 上午9:38:20
 * Title : com.riozenc.cfs.common.ServerCallback.java
 *
**/
package org.fms.cfs.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ServerCallback {

}
