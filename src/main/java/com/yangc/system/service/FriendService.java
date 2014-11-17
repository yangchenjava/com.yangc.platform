package com.yangc.system.service;

import java.util.List;

import com.yangc.system.bean.TSysPerson;

public interface FriendService {

	public void addFriend(Long userId, Long friendId);

	public void delFriend(Long userId, String friendIds);

	public List<TSysPerson> getFriendListByUserId(Long userId);

}
