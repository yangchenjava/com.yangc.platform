package com.yangc.system.service;

import java.io.IOException;
import java.io.InputStream;

public interface FileService {

	public void upload(InputStream in, String fileName, String test) throws IOException;

	public void download();

}
