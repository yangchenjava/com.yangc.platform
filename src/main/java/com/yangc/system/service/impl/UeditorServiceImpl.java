package com.yangc.system.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.yangc.bean.UeditorBean;
import com.yangc.system.service.UeditorService;

@Service
public class UeditorServiceImpl implements UeditorService {

	@Override
	public UeditorBean uploadImage(MultipartFile upfile, String savePath, String urlPath) throws IOException {
		String original = upfile.getOriginalFilename();
		String type = original.substring(original.indexOf("."));
		long size = upfile.getSize();

		String currentDate = DateFormatUtils.format(new Date(), "yyyyMMdd");
		String fileName = System.currentTimeMillis() + String.valueOf(Math.random()).substring(2, 8) + type;
		String url = urlPath + currentDate + "/" + fileName;

		new File(savePath + currentDate + "/").mkdirs();
		FileUtils.copyInputStreamToFile(upfile.getInputStream(), new File(savePath + currentDate + "/" + fileName));

		return new UeditorBean(UeditorBean.SUCCESS, original, original, type, String.valueOf(size), url);
	}

	@Override
	public UeditorBean uploadBase64(String upfile, String savePath, String urlPath) {
		return null;
	}

	@Override
	public UeditorBean uploadFile(MultipartFile upfile, String savePath, String urlPath) {
		return null;
	}

}
