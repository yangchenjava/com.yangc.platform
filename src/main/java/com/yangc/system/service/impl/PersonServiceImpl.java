package com.yangc.system.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.coverity.security.Escape;
import com.yangc.dao.BaseDao;
import com.yangc.system.bean.TSysPerson;
import com.yangc.system.service.PersonService;
import com.yangc.utils.image.ImageUtils;
import com.yangc.utils.lang.PinyinUtils;

@Service
public class PersonServiceImpl implements PersonService {

	@Autowired
	private BaseDao baseDao;

	@Override
	public void addOrUpdatePerson(TSysPerson person, MultipartFile photo, String savePath, String urlPath) throws IOException {
		if (photo != null && !photo.isEmpty()) {
			long currentTimeMillis = System.currentTimeMillis();
			String fileType = FilenameUtils.getExtension(photo.getOriginalFilename());
			String thumbnailName = person.getUserId() + "_" + currentTimeMillis + fileType;
			String photoName = person.getUserId() + "_" + currentTimeMillis + "_original" + fileType;

			File dir = new File(savePath);
			if (!dir.exists() || !dir.isDirectory()) {
				dir.delete();
				dir.mkdirs();
			}
			FileUtils.copyInputStreamToFile(photo.getInputStream(), new File(dir, photoName));
			ImageUtils.process(savePath + photoName, 256, 256, true, 0, null, null, 0, savePath + thumbnailName);
			person.setPhoto(urlPath + thumbnailName);
		}

		person.setSpell(PinyinUtils.getPinyin(person.getNickname()) + " " + PinyinUtils.getPinyinFirst(person.getNickname()));
		this.baseDao.saveOrUpdate(person);
	}

	@Override
	public void delPersonByUserId(Long userId) {
		this.baseDao.updateOrDelete("delete TSysPerson where userId = ?", new Object[] { userId });
	}

	@Override
	public TSysPerson getPersonById(Long id) {
		return this.baseDao.get(TSysPerson.class, id);
	}

	@Override
	public TSysPerson getPersonByUserId(Long userId) {
		return this.baseDao.get("from TSysPerson where userId = ?", new Object[] { userId });
	}

	@Override
	public List<TSysPerson> getPersonList(String condition) {
		if (StringUtils.isNotBlank(condition)) {
			String hql = "select new TSysPerson(nickname, spell) from TSysPerson where nickname like :condition escape :escape or spell like :condition escape :escape";
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("condition", "%" + Escape.sqlLikeClause(condition) + "%");
			paramMap.put("escape", "@");
			return this.baseDao.findAllByMap(hql, paramMap);
		}
		return null;
	}

	@Override
	public List<TSysPerson> getPersonListByNicknameAndDeptId_page(String nickname, Long deptId) {
		StringBuilder sb = new StringBuilder();
		sb.append("select new TSysPerson(p.id, p.nickname, p.sex, p.phone, p.spell, p.userId, u.username, p.deptId, d.deptName)");
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

		return (long) this.baseDao.getCount(sb.toString(), paramMap);
	}

}
