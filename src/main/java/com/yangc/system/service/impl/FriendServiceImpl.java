package com.yangc.system.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yangc.dao.JdbcDao;
import com.yangc.system.bean.TSysPerson;
import com.yangc.system.service.FriendService;

@Service
public class FriendServiceImpl implements FriendService {

	@Autowired
	private JdbcDao jdbcDao;

	@Override
	public void addFriend(Long userId, Long friendId) {
		String sql = JdbcDao.SQL_MAPPING.get("system.friend.addFriend");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userId", userId);
		paramMap.put("friendId", friendId);
		this.jdbcDao.saveOrUpdate(sql, paramMap);
	}

	@Override
	public void delFriend(Long userId, String friendIds) {
		String sql = JdbcDao.SQL_MAPPING.get("system.friend.delFriend");
		List<Object[]> paramList = new ArrayList<Object[]>();
		for (String friendId : friendIds.split(",")) {
			paramList.add(new Object[] { userId, friendId });
		}
		this.jdbcDao.batchExecute(sql, paramList);
	}

	@Override
	public List<TSysPerson> getFriendListByUserId(Long userId, String friendIds) {
		this.delFriend(userId, friendIds);

		String sql = JdbcDao.SQL_MAPPING.get("system.friend.getFriendListByUserId");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userId", userId);
		List<Map<String, Object>> mapList = this.jdbcDao.findAll(sql, paramMap);
		if (CollectionUtils.isEmpty(mapList)) return null;

		List<TSysPerson> persons = new ArrayList<TSysPerson>();
		for (Map<String, Object> map : mapList) {
			TSysPerson person = new TSysPerson();
			person.setId(MapUtils.getLong(map, "ID"));
			person.setNickname(MapUtils.getString(map, "NICKNAME"));
			person.setSex(MapUtils.getLong(map, "SEX"));
			person.setPhone(MapUtils.getString(map, "PHONE"));
			person.setSpell(MapUtils.getString(map, "SPELL"));
			person.setPhoto(MapUtils.getString(map, "PHOTO"));
			person.setSignature(MapUtils.getString(map, "SIGNATURE"));
			person.setUserId(MapUtils.getLong(map, "USER_ID"));
			person.setUsername(MapUtils.getString(map, "USERNAME"));
			persons.add(person);
		}
		return persons;
	}

}
