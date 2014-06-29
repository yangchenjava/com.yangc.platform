package com.yangc.shiro.cache;

import org.apache.shiro.cache.AbstractCacheManager;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;

public class RedisCacheManager extends AbstractCacheManager {

	@Override
	protected Cache<Object, Object> createCache(String name) throws CacheException {
		return new RedisCache<Object, Object>(name);
	}

}
