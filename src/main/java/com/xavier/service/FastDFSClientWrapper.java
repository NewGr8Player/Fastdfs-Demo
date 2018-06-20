package com.xavier.service;

import com.github.tobato.fastdfs.proto.storage.DownloadFileWriter;
import com.github.tobato.fastdfs.service.AppendFileStorageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class FastDFSClientWrapper {

	@Autowired
	private AppendFileStorageClient appendFileStorageClient;

	/**
	 * <p>上传文件</p>
	 *
	 * @param file 文件对象
	 * @return 文件访问地址
	 * @throws IOException
	 */
	public String uploadFile(MultipartFile file) throws IOException {
		return uploadFile(file, "group1");
	}

	/**
	 * <p>上传文件</p>
	 *
	 * @param file      文件对象
	 * @param groupName 分组名称
	 * @return 文件访问地址
	 * @throws IOException
	 */
	public String uploadFile(MultipartFile file, String groupName) throws IOException {
		//TODO 自动获得文件后缀名
		return this.appendFileStorageClient.uploadFile(groupName, file.getInputStream(), file.getSize(), "jpg").getFullPath();
	}
}
