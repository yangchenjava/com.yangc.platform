package com.yangc.system.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yangc.dao.BaseDao;
import com.yangc.system.bean.TSysPerson;
import com.yangc.system.bean.TSysUser;
import com.yangc.system.service.PersonService;
import com.yangc.system.service.UserService;
import com.yangc.utils.lang.PinyinUtils;

@Service
@SuppressWarnings("unchecked")
public class PersonServiceImpl implements PersonService {

	@Autowired
	private BaseDao baseDao;
	@Autowired
	private UserService userService;

	@Override
	public void addOrUpdatePerson(Long personId, String name, Long sex, String phone, Long deptId, Long userId, String username, String roleIds) throws IllegalStateException {
		TSysUser user = this.userService.getUserByUsername(username);
		if (user != null) {
			if (userId == null) {
				throw new IllegalStateException("用户名已存在");
			} else if (userId.longValue() != user.getId().longValue()) {
				throw new IllegalStateException("用户名已存在");
			}
		}

		// 保存person
		TSysPerson person = (TSysPerson) this.baseDao.get(TSysPerson.class, personId);
		if (person == null) {
			person = new TSysPerson();
		}
		person.setName(name);
		person.setSex(sex);
		person.setPhone(phone);
		person.setSpell(PinyinUtils.getPinyin(name) + " " + PinyinUtils.getPinyinHead(name));
		person.setDeptId(deptId);
		this.baseDao.saveOrUpdate(person);

		// 保存user
		this.userService.addOrUpdateUser(userId, username, person.getId(), roleIds);
	}

	@Override
	public String delPerson(Long personId) {
		String username = null;
		TSysUser user = this.userService.getUserByPersonId(personId);
		if (user != null) {
			username = user.getUsername();
			this.userService.delUser(user.getId());
		}
		this.baseDao.updateOrDelete("delete TSysPerson where id = ?", new Object[] { personId });
		return username;
	}

	@Override
	public TSysPerson getPersonByPersonId(Long personId) {
		return (TSysPerson) this.baseDao.get("from TSysPerson where id = ?", new Object[] { personId });
	}

	@Override
	public List<TSysPerson> getPersonList() {
		return this.baseDao.findAll("select new TSysPerson(name, spell) from TSysPerson", null);
	}

	@Override
	public List<TSysPerson> getPersonListByPersonNameAndDeptId_page(String personName, Long deptId) {
		StringBuilder sb = new StringBuilder();
		sb.append("select new TSysPerson(p.id, p.name, p.sex, p.phone, p.deptId, d.deptName, p.spell, u.id as userId, u.username)");
		sb.append(" from TSysPerson p, TSysDepartment d, TSysUser u where p.deptId = d.id and p.id = u.personId");
		Map<String, Object> paramMap = new HashMap<String, Object>();

		if (StringUtils.isNotBlank(personName)) {
			sb.append(" and p.name = :personName");
			paramMap.put("personName", personName);
		}
		if (deptId != null && deptId.longValue() != 0) {
			sb.append(" and p.deptId = :deptId");
			paramMap.put("deptId", deptId);
		}
		sb.append(" order by p.id");

		return this.baseDao.findByMap(sb.toString(), paramMap);
	}

	@Override
	public Long getPersonListByPersonNameAndDeptId_count(String personName, Long deptId) {
		StringBuilder sb = new StringBuilder("select count(p) from TSysPerson p where 1 = 1");
		Map<String, Object> paramMap = new HashMap<String, Object>();

		if (StringUtils.isNotBlank(personName)) {
			sb.append(" and p.name = :personName");
			paramMap.put("personName", personName);
		}
		if (deptId != null && deptId.longValue() != 0) {
			sb.append(" and p.deptId = :deptId");
			paramMap.put("deptId", deptId);
		}

		Number count = (Number) this.baseDao.findAllByMap(sb.toString(), paramMap).get(0);
		return count.longValue();
	}

}
