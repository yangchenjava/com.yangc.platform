package com.yangc.system.bean.oracle;

public class Permission {

	public static final long VALUE_ALL = 15; // 全部
	public static final long VALUE_NONE = 0; // 无

	public static final int ALL = 4; // 全部
	public static final int SEL = 3; // 查询
	public static final int ADD = 2; // 增
	public static final int UPD = 1; // 改
	public static final int DEL = 0; // 删

	public static final int ACL_NO = 0; // 无权限
	public static final int ACL_YES = 1; // 有权限

	/**
	 * @功能: 判断操作是否有权限
	 * @作者: yangc
	 * @创建日期: 2012-9-23 上午01:16:54
	 * @param operateStatus
	 * @param permission
	 * @return
	 */
	public static int getPermission(long operateStatus, int permission) {
		int temp = 1;
		temp = temp << permission;
		temp &= operateStatus;
		if (temp == 0) {
			return ACL_NO;
		}
		return ACL_YES;
	}

	/**
	 * @功能: 判断操作是否有权限
	 * @作者: yangc
	 * @创建日期: 2012-9-23 上午01:16:54
	 * @param operateStatus
	 * @param permission
	 * @return
	 */
	public static boolean isPermission(long operateStatus, int permission) {
		int temp = 1;
		temp = temp << permission;
		temp &= operateStatus;
		if (temp == 0) {
			return false;
		}
		return true;
	}

}
