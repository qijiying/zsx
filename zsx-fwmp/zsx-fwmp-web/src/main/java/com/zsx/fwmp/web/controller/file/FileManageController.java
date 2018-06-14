package com.zsx.fwmp.web.controller.file;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.zsx.framework.designpattern.factory.ResultfulFactory;
import com.zsx.framework.exception.SystemException;
import com.zsx.framework.exception.enmus.ResultEnum;
import com.zsx.fwmp.web.service.file.IFileManageService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 
  * @ClassName: FileManageController 
  * @Description:文件管理
  * @author xiayy 
  * @date 2018年1月26日 下午5:04:24 
  *
 */
@RestController
@RequestMapping("/api/file")
public class FileManageController {
	
	private static Logger logger=LoggerFactory.getLogger(FileManageController.class);
	
	@Autowired
	IFileManageService iFileManageService;
	
	
	/**
	  * 
	  * @Title: upload 
	  * @Description: 上传文件
	  * @param @param file  文件
	  * @param @param id  ID
	  * @param @param fileSource 上传类型
	  * @param @param usePotion  上传位置
	  * @param @return    设定文件 
	  * @return Object    返回类型 
	  * @throws
	 */
	@ApiOperation(
			value="文件上传", 
			notes="文件上传")
	@ApiImplicitParams({
		@ApiImplicitParam(name="sId",value="来源ID",required=true,paramType="query",dataType="int"),
		@ApiImplicitParam(name="userId",value="上传者ID",required=true,paramType="path",dataType="int"),
		@ApiImplicitParam(name="s",value="文件来源",required=true,paramType="query",dataType="int"),
		@ApiImplicitParam(name="p",value="使用位置",required=true,paramType="query",dataType="int")
	})
	@CrossOrigin(origins="*",maxAge=3000)
	@PostMapping("/{userId}")
	public Object upload(
			HttpServletRequest request,
			@RequestParam(value="sId",required=false)Long sourceId,
			@PathVariable(value="userId")Long userId,
			@RequestParam(value="s",required=false)Integer fileSource,
			@RequestParam(value="p",required=false)Integer usePotion,
			@RequestParam(value="file",required=false)MultipartFile[] multipartFile){
		if(logger.isDebugEnabled()){
			logger.debug("====================文件上传======================");
		}
		Map<String, String> fileMap=null;
		if(multipartFile.length == 0){
			throw new SystemException(ResultEnum.SERVER_UPLOAD_FILE_ERROR);
		}
		try {
			fileMap=iFileManageService.uploadFile(multipartFile,sourceId,userId,fileSource,usePotion);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultfulFactory.getInstance().creator(ResultEnum.SERVER_FILE_UOLPAD_ERROR);
		}
		return ResultfulFactory.getInstance().creator(ResultEnum.SUCCESS,fileMap);
	}
	
}

