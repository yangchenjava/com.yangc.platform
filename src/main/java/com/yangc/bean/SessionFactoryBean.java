package com.yangc.bean;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;

import com.yangc.utils.Constants;

public class SessionFactoryBean extends LocalSessionFactoryBean {

	@Override
	public void setMappingDirectoryLocations(Resource... mappingDirectoryLocations) {
		if (mappingDirectoryLocations != null) {
			Resource[] resource = new Resource[mappingDirectoryLocations.length];
			for (int i = 0; i < mappingDirectoryLocations.length; i++) {
				resource[i] = new FileSystemResource(((FileSystemResource) mappingDirectoryLocations[i]).getPath() + "/" + Constants.DB_NAME);
			}
			super.setMappingDirectoryLocations(resource);
		}
	}

}
