package com.yangc.system.aop;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

import com.google.gson.reflect.TypeToken;
import com.yangc.system.bean.TSysMenu;
import com.yangc.utils.Constants;
import com.yangc.utils.cache.RedisUtils;
import com.yangc.utils.json.JsonUtils;

public class MenuAop {

	/**
	 * @功能: 清空菜单缓存
	 * @作者: yangc
	 * @创建日期: 2014年7月5日 下午9:42:01
	 * @param point
	 */
	public void cleanMenuCacheAfterMethod(JoinPoint point) {
		RedisUtils.getInstance().del(Constants.MENU);
	}

	/**
	 * @功能: top菜单缓存
	 * @作者: yangc
	 * @创建日期: 2014年7月5日 下午10:28:15
	 * @param point
	 * @return
	 * @throws Throwable
	 */
	public Object topMenuCacheAroundMethod(ProceedingJoinPoint point) throws Throwable {
		Object[] args = point.getArgs();

		// 查询redis缓存
		String field = Constants.MENU_TOP + "_" + args[0] + "_" + args[1];
		RedisUtils cache = RedisUtils.getInstance();
		List<String> values = cache.getHashMap(Constants.MENU, field);
		if (values != null && !values.isEmpty() && values.get(0) != null) {
			return JsonUtils.fromJson(values.get(0), new TypeToken<List<TSysMenu>>() {
			});
		}

		Object menus = point.proceed();

		// 设置redis缓存
		Map<String, String> map = new HashMap<String, String>();
		map.put(field, JsonUtils.toJson(menus));
		cache.putHashMap(Constants.MENU, map);
		return menus;
	}

	/**
	 * @功能: main菜单缓存
	 * @作者: yangc
	 * @创建日期: 2014年7月5日 下午10:28:15
	 * @param point
	 * @return
	 * @throws Throwable
	 */
	public Object mainMenuCacheAroundMethod(ProceedingJoinPoint point) throws Throwable {
		Object[] args = point.getArgs();

		// 查询redis缓存
		String field = Constants.MENU_MAIN + "_" + args[0] + "_" + args[1];
		RedisUtils cache = RedisUtils.getInstance();
		List<String> values = cache.getHashMap(Constants.MENU, field);
		if (values != null && !values.isEmpty() && values.get(0) != null) {
			return JsonUtils.fromJson(values.get(0), new TypeToken<List<TSysMenu>>() {
			});
		}

		Object menus = point.proceed();

		// 设置redis缓存
		Map<String, String> map = new HashMap<String, String>();
		map.put(field, JsonUtils.toJson(menus));
		cache.putHashMap(Constants.MENU, map);
		return menus;
	}

}
