package com.yangc.bean;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;

import com.yangc.utils.Constants;

public class SessionFactoryBean extends LocalSessionFactoryBean {

	@Override
	public void setMappingDirectoryLocations(Resource... mappingDirectoryLocations) {
		if (mappingDirectoryLocations != null) {
			List<Resource> resources = new ArrayList<Resource>(mappingDirectoryLocations.length);
			for (Resource mappingDirectoryLocation : mappingDirectoryLocations) {
				FileSystemResource resource = new FileSystemResource(((FileSystemResource) mappingDirectoryLocation).getPath() + "/" + Constants.DB_NAME);
				if (resource.exists()) resources.add(resource);
			}
			super.setMappingDirectoryLocations(resources.toArray(new Resource[0]));
		}
	}

}
