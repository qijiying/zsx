package com.zsx.fwmp.web.others.callback;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import com.zsx.fwmp.web.others.util.ImageUtil;
import com.zsx.fwmp.web.others.util.VideoUtil;
import com.zsx.fwmp.web.service.file.IFileManageService;
import com.zsx.fwmp.web.service.file.impl.FileManageServiceImpl;
import com.zsx.model.pojo.FileManage;
import com.zsx.model.webdto.FileDto;

/**
  * 
  * @ClassName: FFServer 
  * @Description: 回调线程类
  * @author xiayy 
  * @date 2018年2月9日 下午4:25:06 
  *
 */
//@Component
public class FileFFmepgHandleThread  {
	
	private static final Logger logger=LoggerFactory.getLogger(FileFFmepgHandleThread.class);
	
	@Autowired
	IFileManageService iFileManageService;
	
	private FileManageServiceImpl _fileManageServiceImpl;
	private FileUploadHandle _uploadServer;
	private FileManage _fileManage;
	private MultipartFile _file;
	private byte[] bytes;
	private String fileSize;
	
	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	public FileManageServiceImpl get_fileManageServiceImpl() {
		return _fileManageServiceImpl;
	}

	public FileUploadHandle get_uploadServer() {
		return _uploadServer;
	}

	public FileManage get_fileManage() {
		return _fileManage;
	}

	public MultipartFile get_file() {
		return _file;
	}


	public void set_fileManageServiceImpl(
			FileManageServiceImpl _fileManageServiceImpl) {
		this._fileManageServiceImpl = _fileManageServiceImpl;
	}

	public void set_uploadServer(FileUploadHandle _uploadServer) {
		this._uploadServer = _uploadServer;
	}

	public void set_fileManage(FileManage _fileManage) {
		this._fileManage = _fileManage;
	}

	public void set_file(MultipartFile _file) {
		this._file = _file;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	//@Async
	public void run(Integer type) throws IOException, InterruptedException, ExecutionException {
		VideoUtil videoUtil=new VideoUtil();
		ImageUtil imageUtil=new ImageUtil();
		try {
			if (type == 1) {
				System.out.println(fileSize);
				imageUtil.handleImage(getBytes(), get_fileManage(),get_fileManageServiceImpl());
			} else {
				FileDto[] fileDtos=new FileDto[1];
				fileDtos[0]=videoUtil.handle(get_file().getOriginalFilename(),getBytes(),get_file().getSize(), get_fileManage(),get_fileManageServiceImpl());
				_uploadServer.UMsg(_fileManageServiceImpl, fileDtos);
			}
			
		} catch (Exception e) {
			logger.warn("上传文件失败");
			e.printStackTrace();
		}
	}
}
