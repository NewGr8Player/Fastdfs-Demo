package com.xavier.api;

import com.xavier.common.FastDFSClient;
import com.xavier.common.util.FileCheck;
import com.xavier.config.ErrorCode;
import com.xavier.config.FastDFSException;
import com.xavier.config.FileResponseData;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

@RestController
@RequestMapping(value = "/")
public class FileController {

	@Autowired
	private FastDFSClient fastDFSClient;

	@Value("${fastdfs.file_server_addr}")
	private String fileServerAddr;/* 文件服务器地址 */

	@Value("${fastdfs.http_secret_key}")
	private String fastDFSHttpSecretKey;/* FastDFS秘钥 */

	@ApiOperation(value = "测试接口")
	@GetMapping(value = "/test")
	public FileResponseData test() {
		return new FileResponseData(true);
	}

	@ApiOperation(value = "上传文件通用，只上传文件到服务器，不会保存记录到数据库")
	@PostMapping(value = "/upload/file/sample")
	public FileResponseData uploadFileSample(
			@ApiParam(name = "file", value = "文件", required = true)
			@RequestParam MultipartFile file,
			HttpServletRequest request) {
		return uploadSample(file, request);
	}

	@ApiOperation(value = "只能上传图片，只上传文件到服务器，不会保存记录到数据库. <br>会检查文件格式是否正确，默认只能上传 ['png', 'gif', 'jpeg', 'jpg'] 几种类型.")
	@PostMapping(value = "/upload/image/sample")
	public FileResponseData uploadImageSample(
			@ApiParam(name = "file", value = "图片文件", required = true)
			@RequestParam MultipartFile file,
			HttpServletRequest request) {
		if (!FileCheck.checkImage(file.getOriginalFilename())) {
			FileResponseData responseData = new FileResponseData(false);
			responseData.setCode(ErrorCode.FILE_TYPE_ERROR_IMAGE.CODE);
			responseData.setMessage(ErrorCode.FILE_TYPE_ERROR_IMAGE.MESSAGE);
			return responseData;
		}

		return uploadSample(file, request);
	}

	@ApiOperation(value = "只能上传文档，只上传文件到服务器，不会保存记录到数据库. <br> 会检查文件格式是否正确，默认只能上传 ['pdf', 'ppt', 'xls', 'xlsx', 'pptx', 'doc', 'docx'] 几种类型.")
	@PostMapping(value = "/upload/doc/sample")
	public FileResponseData uploadDocSample(
			@ApiParam(name = "file", value = "文档文件", required = true)
			@RequestParam MultipartFile file,
			HttpServletRequest request) {
		if (!FileCheck.checkDoc(file.getOriginalFilename())) {
			FileResponseData responseData = new FileResponseData(false);
			responseData.setCode(ErrorCode.FILE_TYPE_ERROR_DOC.CODE);
			responseData.setMessage(ErrorCode.FILE_TYPE_ERROR_DOC.MESSAGE);
			return responseData;
		}

		return uploadSample(file, request);
	}

	@ApiOperation(value = "以附件形式下载文件")
	@GetMapping(value = "/download/file")
	public void downloadFile(
			@ApiParam(name = "filePath", value = "文件路径", required = true) String filePath,
			HttpServletResponse response) throws FastDFSException {
		try {
			this.fastDFSClient.downloadFile(filePath, response);
		} catch (FastDFSException e) {
			e.printStackTrace();
			throw e;
		}
	}

	@ApiOperation(value = "获取图片 使用输出流输出字节码，可以使用< img>标签显示图片")
	@GetMapping(value = "/download/image")
	public void downloadImage(
			@ApiParam(name = "filePath", value = "文件路径", required = true) String filePath,
			HttpServletResponse response) throws FastDFSException {
		try {
			this.fastDFSClient.downloadFile(filePath, response.getOutputStream());
		} catch (FastDFSException e) {
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@ApiOperation(value = "根据指定的路径删除服务器文件，适用于没有保存数据库记录的文件")
	@DeleteMapping(value = "/delete/file")
	public FileResponseData deleteFile(
			@ApiParam(name = "filePath", value = "文件路径", required = true) String filePath,
			Locale locale) {
		FileResponseData responseData = new FileResponseData();
		try {
			fastDFSClient.deleteFile(filePath);
		} catch (FastDFSException e) {
			e.printStackTrace();
			responseData.setSuccess(false);
			responseData.setCode(e.getCode());
			responseData.setMessage(e.getMessage());
		}
		return responseData;
	}

	@ApiOperation(value = "获取访问文件的token", notes = "该方法暂时只是示例")
	@GetMapping("/get/token")
	public FileResponseData getToken(String filePath) {
		FileResponseData responseData = new FileResponseData();
		// 设置访文件的Http地址. 有时效性.
		String token = FastDFSClient.getToken(filePath, fastDFSHttpSecretKey);
		responseData.setToken(token);
		responseData.setHttpUrl(fileServerAddr + "/" + filePath + "?" + token);
		return responseData;
	}

	/**
	 * <p>
	 * 上传通用方法，其它方法调用该方法。<br />
	 * 暂不使用，仅作为公共方法放置于此
	 * </p>
	 *
	 * @param file    MultipartFile
	 * @param request HttpServletRequest
	 * @return
	 */
	private FileResponseData uploadSample(MultipartFile file, HttpServletRequest request) {
		FileResponseData responseData = new FileResponseData();
		try {
			// 上传到服务器
			String filepath = fastDFSClient.uploadFileWithMultipart(file);

			responseData.setFileName(file.getOriginalFilename());
			responseData.setFilePath(filepath);
			responseData.setFileType(FastDFSClient.getFilenameSuffix(file.getOriginalFilename()));
			// 设置访文件的Http地址. 有时效性.
			String token = FastDFSClient.getToken(filepath, fastDFSHttpSecretKey);
			responseData.setToken(token);
			responseData.setHttpUrl(fileServerAddr + "/" + filepath + "?" + token);
		} catch (FastDFSException e) {
			responseData.setSuccess(false);
			responseData.setCode(e.getCode());
			responseData.setMessage(e.getMessage());
		}

		return responseData;
	}
}
