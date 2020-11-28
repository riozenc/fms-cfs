/**
 * Author : chizf
 * Date : 2020年5月16日 上午9:39:34
 * Title : com.riozenc.cfs.webapp.mrm.config.FixedParametersConfig.java
 *
**/
package org.fms.cfs.common.config;

public class FixedParametersConfig {

	/**
	 * 装表
	 */
	public static final String OPERATE_TYPE_INSTALL = "01";
	/**
	 * 拆表
	 */
	public static final String OPERATE_TYPE_DISMANTLE = "02";

	public static final int EQUIPMENT_TYPE_1 = 1;
	public static final int EQUIPMENT_TYPE_2 = 2;

	/**
	 * 计费表
	 */
	public static final int METER_TYPE_1 = 1;

	/**
	 * 考核表
	 */
	public static final int METER_TYPE_2 = 2;

	/**
	 * 有功表
	 */
	public static final int FUNCTION_CODE_1 = 1;
	/**
	 * 无功表
	 */
	public static final int FUNCTION_CODE_2 = 2;

	/**
	 * 虚拟表
	 */
	public static final int FUNCTION_CODE_3 = 3;

	/**
	 * 高供高计
	 */
	public static final int MS_MODE_1 = 1;
	/**
	 * 高供低计
	 */
	public static final int MS_MODE_2 = 2;
	/**
	 * 低供低计
	 */
	public static final int MS_MODE_3 = 3;
	/**
	 * 变损分摊方式:不分摊
	 */
	public static final int TRANS_SHARE_FLAG_0 = 0;
	/**
	 * 变损分摊方式:按合同容量分摊
	 */
	public static final int TRANS_SHARE_FLAG_1 = 1;
	/**
	 * 按用电量分摊
	 */
	public static final int TRANS_SHARE_FLAG_2 = 2;
	/**
	 * 固定变损
	 */
	public static final int TRANS_SHARE_FLAG_3 = 3;
	/**
	 * 参与分摊但不计算变损
	 */
	public static final int TRANS_SHARE_FLAG_4 = 4;
	/**
	 * 普通
	 */
	public static final int TS_METER_FLAG_0 = 0;

	/**
	 * 分时
	 */
	public static final int TS_METER_FLAG_1 = 1;

	/**
	 * 分时：总
	 */
	public static final int TIME_SEG_0 = 0;
	/**
	 * 分时：峰
	 */
	public static final int TIME_SEG_1 = 1;
	/**
	 * 分时：平
	 */
	public static final int TIME_SEG_2 = 2;
	/**
	 * 分时：谷
	 */
	public static final int TIME_SEG_3 = 3;
	/**
	 * 分时：尖
	 */
	public static final int TIME_SEG_4 = 4;
}
