package com.yangc.system.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.yangc.bean.UeditorBean;

public interface UeditorService {

	public UeditorBean uploadFile(MultipartFile upfile, String savePath, String urlPath) throws IOException;

	public UeditorBean uploadBase64(String upfile, String savePath, String urlPath) throws IOException;

}
