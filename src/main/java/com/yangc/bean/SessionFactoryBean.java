package com.yangc.bean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
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

	@Override
	public void setMappingJarLocations(Resource... mappingJarLocations) {
		if (mappingJarLocations != null) {
			List<Resource> resources = new ArrayList<Resource>();
			for (Resource mappingJarLocation : mappingJarLocations) {
				JarFile jarFile = null;
				try {
					jarFile = new JarFile(mappingJarLocation.getFile());
					Enumeration<JarEntry> jarEntries = jarFile.entries();
					while (jarEntries.hasMoreElements()) {
						JarEntry jarEntry = jarEntries.nextElement();
						if (jarEntry.getName().endsWith(".hbm.xml") && jarEntry.getName().contains(Constants.DB_NAME)) {
							InputStreamResource resource = new InputStreamResource(jarFile.getInputStream(jarEntry));
							if (resource.exists()) resources.add(resource);
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						if (jarFile != null) {
							jarFile.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			super.setMappingLocations(resources.toArray(new Resource[0]));
		}
	}

}
