package com.yangc.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yangc.dao.BaseDao;
import com.yangc.system.bean.oracle.TSysDepartment;
import com.yangc.system.service.DeptService;
import com.yangc.system.service.PersonService;
import com.yangc.utils.BeanUtils;

@Service
@SuppressWarnings("unchecked")
public class DeptServiceImpl implements DeptService {

	@Autowired
	private BaseDao baseDao;
	@Autowired
	private PersonService personService;

	@Override
	public void addOrUpdateDept(Long deptId, String deptName, Long serialNum) {
		TSysDepartment dept = (TSysDepartment) this.baseDao.get(TSysDepartment.class, deptId);
		if (dept == null) {
			dept = new TSysDepartment();
		}
		dept.setDeptName(deptName);
		dept.setSerialNum(serialNum);
		this.baseDao.save(dept);
	}

	@Override
	public void delDept(Long deptId) throws IllegalStateException {
		Long count = this.personService.getPersonListByPersonNameAndDeptId_count(null, deptId);
		if (count > 0) {
			throw new IllegalStateException("该部门下有员工存在");
		}
		this.baseDao.updateOrDelete("delete TSysDepartment where id = ?", new Object[] { deptId });
	}

	@Override
	public List<TSysDepartment> getDeptList() {
		return this.baseDao.findAll("from TSysDepartment order by serialNum", null);
	}

	@Override
	public List<TSysDepartment> getDeptList_page() {
		List<TSysDepartment> departments = this.baseDao.find("from TSysDepartment order by serialNum", null);
		return BeanUtils.fillingTime(departments);
	}

}
