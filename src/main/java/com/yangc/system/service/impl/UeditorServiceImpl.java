package com.yangc.system.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.yangc.bean.UeditorBean;
import com.yangc.system.service.UeditorService;

@Service
public class UeditorServiceImpl implements UeditorService {

	@Override
	public UeditorBean uploadImage(MultipartFile upfile, String savePath) {
		return null;
	}

	@Override
	public UeditorBean uploadBase64(String upfile, String savePath) {
		return null;
	}

	@Override
	public UeditorBean uploadFile(MultipartFile upfile, String savePath) {
		return null;
	}

}
