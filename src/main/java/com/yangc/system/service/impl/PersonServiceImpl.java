package com.yangc.system.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.yangc.dao.BaseDao;
import com.yangc.system.bean.TSysPerson;
import com.yangc.system.service.PersonService;
import com.yangc.utils.lang.PinyinUtils;

@Service
@SuppressWarnings("unchecked")
public class PersonServiceImpl implements PersonService {

	@Autowired
	private BaseDao baseDao;

	@Override
	public void addOrUpdatePerson(TSysPerson person, MultipartFile photo, String savePath, String urlPath) throws IOException {
		if (photo != null) {
			String original = photo.getOriginalFilename();
			String fileName = person.getUserId() + "_" + System.currentTimeMillis() + original.substring(original.indexOf("."));

			File dir = new File(savePath);
			if (!dir.exists() || !dir.isDirectory()) {
				dir.delete();
				dir.mkdirs();
			}
			FileUtils.copyInputStreamToFile(photo.getInputStream(), new File(dir, fileName));
			person.setPhoto(urlPath + fileName);
		}

		person.setSpell(PinyinUtils.getPinyin(person.getNickname()) + " " + PinyinUtils.getPinyinHead(person.getNickname()));
		this.baseDao.saveOrUpdate(person);
	}

	@Override
	public void delPersonByUserId(Long userId) {
		this.baseDao.updateOrDelete("delete TSysPerson where userId = ?", new Object[] { userId });
	}

	@Override
	public TSysPerson getPersonByUserId(Long userId) {
		return (TSysPerson) this.baseDao.get("from TSysPerson where userId = ?", new Object[] { userId });
	}

	@Override
	public List<TSysPerson> getPersonList(String condition) {
		if (StringUtils.isNotBlank(condition)) {
			String hql = "select new TSysPerson(nickname, spell) from TSysPerson where nickname like :condition or spell like :condition";
			Map<String, Object> paramMap = new HashMap<String, Object>(1);
			paramMap.put("condition", "%" + condition + "%");
			return this.baseDao.findAllByMap(hql, paramMap);
		}
		return null;
	}

	@Override
	public List<TSysPerson> getPersonListByNicknameAndDeptId_page(String nickname, Long deptId) {
		StringBuilder sb = new StringBuilder();
		sb.append("select new TSysPerson(p.id, p.nickname, p.sex, p.phone, p.spell, u.id as userId, u.username, p.deptId, d.deptName)");
		sb.append(" from TSysPerson p, TSysUser u, TSysDepartment d where p.userId = u.id and p.deptId = d.id");
		Map<String, Object> paramMap = new HashMap<String, Object>();

		if (StringUtils.isNotBlank(nickname)) {
			sb.append(" and p.nickname = :nickname");
			paramMap.put("nickname", nickname);
		}
		if (deptId != null && deptId.longValue() != 0) {
			sb.append(" and p.deptId = :deptId");
			paramMap.put("deptId", deptId);
		}
		sb.append(" order by p.id");

		return this.baseDao.findByMap(sb.toString(), paramMap);
	}

	@Override
	public Long getPersonListByNicknameAndDeptId_count(String nickname, Long deptId) {
		StringBuilder sb = new StringBuilder("select count(p) from TSysPerson p where 1 = 1");
		Map<String, Object> paramMap = new HashMap<String, Object>();

		if (StringUtils.isNotBlank(nickname)) {
			sb.append(" and p.nickname = :nickname");
			paramMap.put("nickname", nickname);
		}
		if (deptId != null && deptId.longValue() != 0) {
			sb.append(" and p.deptId = :deptId");
			paramMap.put("deptId", deptId);
		}

		Number count = (Number) this.baseDao.findAllByMap(sb.toString(), paramMap).get(0);
		return count.longValue();
	}

}
