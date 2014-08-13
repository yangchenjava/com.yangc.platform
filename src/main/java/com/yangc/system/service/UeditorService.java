package com.yangc.system.service;

import org.springframework.web.multipart.MultipartFile;

import com.yangc.bean.UeditorBean;

public interface UeditorService {

	public UeditorBean uploadImage(MultipartFile upfile, String savePath);

	public UeditorBean uploadBase64(String upfile, String savePath);

	public UeditorBean uploadFile(MultipartFile upfile, String savePath);

}
