package com.yangc.system.service;

import java.util.List;

import com.yangc.system.bean.TSysPerson;

public interface PersonService {

	public void addOrUpdatePerson(TSysPerson person);

	public void delPersonByUserId(Long userId);

	public TSysPerson getPersonByUserId(Long userId);

	public List<TSysPerson> getPersonList(String condition);

	public List<TSysPerson> getPersonListByPersonNameAndDeptId_page(String personName, Long deptId);

	public Long getPersonListByPersonNameAndDeptId_count(String personName, Long deptId);

}
