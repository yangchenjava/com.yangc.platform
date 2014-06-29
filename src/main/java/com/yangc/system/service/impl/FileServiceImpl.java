package com.yangc.system.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import com.yangc.system.service.FileService;

@Service
public class FileServiceImpl implements FileService {

	@Override
	public void upload(InputStream in, String fileName, String test) throws IOException {
		System.out.println("fileName = " + fileName + ", test=" + test);
		File f = new File("E:/upload/" + fileName);
		FileUtils.copyInputStreamToFile(in, f);
		System.out.println("fileSize = " + f.length());
	}

	@Override
	public void download() {

	}

}
