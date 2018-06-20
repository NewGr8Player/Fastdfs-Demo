package com.xavier.api;

import com.xavier.service.FastDFSClientWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequestMapping(value = "/dfs")
@RestController
public class IndexController {

	@Autowired
	private FastDFSClientWrapper fileClient;

	@RequestMapping(value = "/test")
	public String index() {
		return "index";
	}

	/**
	 * <p>上传文件</p>
	 *
	 * @param file
	 * @return
	 * @throws IOException
	 */
	@PostMapping(value = "/upload", produces = "application/json")
	public String upload(@RequestParam MultipartFile file) throws IOException {
		return this.fileClient.uploadFile(file);
	}
}
