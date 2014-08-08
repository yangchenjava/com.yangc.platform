package com.yangc.system.service;

import java.util.List;

import com.yangc.system.bean.TSysPerson;

public interface PersonService {

	public void addOrUpdatePerson(Long personId, String name, Long sex, String phone, Long deptId, Long userId, String username, String roleIds) throws IllegalStateException;

	public String delPerson(Long personId);

	public TSysPerson getPersonByPersonId(Long personId);

	public List<TSysPerson> getPersonList();

	public List<TSysPerson> getPersonListByPersonNameAndDeptId_page(String personName, Long deptId);

	public Long getPersonListByPersonNameAndDeptId_count(String personName, Long deptId);

}
