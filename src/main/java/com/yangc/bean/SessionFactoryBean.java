package com.yangc.bean;

import org.apache.log4j.Logger;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;

import com.yangc.utils.prop.PropertiesUtils;

public class SessionFactoryBean extends LocalSessionFactoryBean {

	private static final Logger logger = Logger.getLogger(SessionFactoryBean.class);

	private static String DB = "DB_NAME";

	public SessionFactoryBean() {
		String driverClassName = PropertiesUtils.getProperty("/jdbc.properties", "database.driverClassName");
		if ("com.microsoft.sqlserver.jdbc.SQLServerDriver".equals(driverClassName)) {
			DB = "sqlserver";
		} else if ("oracle.jdbc.driver.OracleDriver".equals(driverClassName)) {
			DB = "oracle";
		} else if ("com.mysql.jdbc.Driver".equals(driverClassName)) {
			DB = "mysql";
		}
		logger.info("SessionFactoryBean - DB_NAME=" + DB);
	}

	@Override
	public void setMappingDirectoryLocations(Resource... mappingDirectoryLocations) {
		if (mappingDirectoryLocations != null) {
			Resource[] resource = new Resource[mappingDirectoryLocations.length];
			for (int i = 0; i < mappingDirectoryLocations.length; i++) {
				resource[i] = new FileSystemResource(((FileSystemResource) mappingDirectoryLocations[i]).getPath() + "/" + DB);
			}
			super.setMappingDirectoryLocations(resource);
		}
	}

}
