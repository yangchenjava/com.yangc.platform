package com.yangc.dao.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.yangc.common.Pagination;
import com.yangc.common.PaginationThreadUtils;
import com.yangc.dao.JdbcDao;
import com.yangc.utils.Constants;

public class JdbcDaoImpl implements JdbcDao {

	/** 存储符合规范的文件 */
	private static final List<File> LIST = new ArrayList<File>();

	private NamedParameterJdbcTemplate npJdbcTemplate;
	private JdbcTemplate jdbcTemplate;

	static {
		List<File> fileList = getFileInfo(Constants.CLASSPATH + "config/multi/jdbc/");
		for (File file : fileList) {
			loadFileContents(file);
		}
		/** 释放内存 */
		LIST.clear();
	}

	public JdbcDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate, JdbcTemplate jdbcTemplate) {
		this.npJdbcTemplate = namedParameterJdbcTemplate;
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * 指定目录下探测符合命名规范的文件信息集合
	 */
	private static List<File> getFileInfo(String fileDir) {
		File file = new File(fileDir);
		if (file.exists()) {
			File[] files = file.listFiles();
			if (files == null || files.length == 0) {
				return LIST;
			}
			for (File f : files) {
				if (f.isFile() && matchNaming(f)) {
					LIST.add(f);
				} else if (f.isDirectory()) {
					getFileInfo(f.getPath());
				}
			}
		}
		return LIST;
	}

	/**
	 * 判断是否符合命名规范
	 */
	private static boolean matchNaming(File file) {
		boolean b = false;
		String fileName = file.getName();
		if (fileName.endsWith(".xml")) {
			String dbFileName = fileName.split("\\.")[0];
			if (dbFileName.endsWith("-sql") && dbFileName.contains(Constants.DB_NAME)) {
				b = true;
			}
		}
		return b;
	}

	/**
	 * 加载结果文件内容
	 */
	@SuppressWarnings("unchecked")
	private static void loadFileContents(File file) {
		try {
			SAXReader reader = new SAXReader();
			Document doc = reader.read(new FileInputStream(file));
			Element root = doc.getRootElement();
			List<Element> elements = root.elements();
			if (elements != null) {
				for (Element element : elements) {
					JdbcDao.SQL_MAPPING.put(element.attribute("name").getText(), element.getText().trim());
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int saveOrUpdate(String sql, Map<String, Object> paramMap) {
		return this.npJdbcTemplate.update(sql, paramMap);
	}

	@Override
	public int delete(String sql, Map<String, Object> paramMap) {
		return this.npJdbcTemplate.update(sql, paramMap);
	}

	@Override
	public int[] batchExecute(String sql, final List<Object[]> paramList) {
		return this.jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			// 返回更新的记录数
			@Override
			public int getBatchSize() {
				return paramList.size();
			}

			// 设置参数
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Object[] objs = paramList.get(i);
				for (int j = 0, length = objs.length; j < length; j++) {
					ps.setObject(j + 1, objs[j]);
				}
			}
		});
	}

	@Override
	public List<Map<String, Object>> find(String sql, Map<String, Object> paramMap) {
		/* 获取分页情况 */
		Pagination pagination = PaginationThreadUtils.get();
		if (pagination == null) {
			pagination = new Pagination();
			PaginationThreadUtils.set(pagination);
			pagination.setPageNow(1);
		}
		if (pagination.getTotalCount() == 0) {
			String countSql = "SELECT COUNT(1) FROM (" + sql + ") TEMP_TABLE_";
			pagination.setTotalCount(this.getCount(countSql, paramMap));
		}
		int firstResult = (pagination.getPageNow() - 1) * pagination.getPageSize();
		/* 校验分页情况 */
		if (firstResult >= pagination.getTotalCount() || firstResult < 0) {
			pagination.setPageNow(1);
		}
		/* 如果总数返回0, 直接返回空 */
		if (pagination.getTotalCount() == 0) {
			return null;
		}
		if (Constants.DB_NAME.equals("oracle")) {
			return this.queryForOracle(sql, paramMap);
		} else if (Constants.DB_NAME.equals("mysql")) {
			return this.queryForMysql(sql, paramMap);
		}
		return null;
	}

	private List<Map<String, Object>> queryForOracle(String sql, Map<String, Object> paramMap) {
		Pagination pagination = PaginationThreadUtils.get();
		int firstResult = (pagination.getPageNow() - 1) * pagination.getPageSize();
		int maxResults = pagination.getPageSize();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * FROM (");
		sb.append("SELECT TEMP_TABLE_.*, ROWNUM ROWNUM_ FROM (").append(sql).append(") TEMP_TABLE_");
		sb.append(" WHERE ROWNUM <= ").append(firstResult + maxResults).append(")");
		sb.append(" WHERE ROWNUM_ > ").append(firstResult);
		return this.findAll(sb.toString(), paramMap);
	}

	private List<Map<String, Object>> queryForMysql(String sql, Map<String, Object> paramMap) {
		Pagination pagination = PaginationThreadUtils.get();
		int firstResult = (pagination.getPageNow() - 1) * pagination.getPageSize();
		int maxResults = pagination.getPageSize();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * FROM (").append(sql).append(") TEMP_TABLE_");
		sb.append(" LIMIT ").append(firstResult).append(", ").append(maxResults);
		return this.findAll(sb.toString(), paramMap);
	}

	@Override
	public List<Map<String, Object>> findAll(String sql, Map<String, Object> paramMap) {
		return this.npJdbcTemplate.query(sql, paramMap, new RowMapper<Map<String, Object>>() {
			@Override
			public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
				Map<String, Object> map = new HashMap<String, Object>();
				ResultSetMetaData meta = rs.getMetaData();
				for (int i = 1, count = meta.getColumnCount(); i <= count; i++) {
					Object obj = rs.getObject(i);
					String columnLabel = meta.getColumnLabel(i).toUpperCase();
					if (obj instanceof java.sql.Clob) {
						java.sql.Clob clob = rs.getClob(i);
						map.put(columnLabel, clob.getSubString((long) 1, (int) clob.length()));
					} else if (obj instanceof java.sql.Date || obj instanceof java.sql.Timestamp) {
						java.sql.Timestamp timestamp = rs.getTimestamp(i);
						map.put(columnLabel, timestamp);
					} else {
						map.put(columnLabel, obj);
					}
				}
				return map;
			}
		});
	}

	@Override
	public int getCount(String sql, Map<String, Object> paramMap) {
		return this.npJdbcTemplate.queryForObject(sql, paramMap, Integer.class);
	}

	@Override
	public Connection getConn() {
		try {
			return this.jdbcTemplate.getDataSource().getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
