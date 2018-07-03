package com.zsx.fwmp.web.controller.sensitive;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.plugins.Page;
import com.zsx.framework.designpattern.factory.ResultfulFactory;
import com.zsx.framework.exception.enmus.ResultEnum;
import com.zsx.fwmp.web.others.util.Assert;
import com.zsx.fwmp.web.service.sensitive.ISensitiveWordService;
import com.zsx.model.pojo.SensitiveWord;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/web/sensitive")
public class SensitiveController {

	@Autowired
	private ISensitiveWordService iSensitiveWordService;
	
	/**
	 * @Title sensitiveWord
	 * @description 敏感词列表
	 * @param current
	 * @param size
	 * @return
	 */
	@ApiOperation(
			value="敏感词列表",
			notes="敏感词列表"
			)
	@ApiImplicitParams({
		@ApiImplicitParam(name="current",value="当前页码",required=false,paramType="query",dataType="int"),
		@ApiImplicitParam(name="size",value="每页条数",required=false,paramType="query",dataType="int")
	})
	@GetMapping("/dataGrid")
	protected Object sensitiveWord(
			@RequestParam(value="current",required=false) Integer current,
			@RequestParam(value="size",required=false) Integer size
			){
		return ResultfulFactory
				.getInstance()
				.creator(ResultEnum.SUCCESS, iSensitiveWordService
						.sensitiveWord(new Page<>(current==null?1:current,size==null?10:size)));
	}
	
	
	/**
	 * @Title addSensitiveWord
	 * @description 新增敏感词
	 * @param sensitiveWord
	 * @return
	 */
	@ApiOperation(
			value="新增敏感词",
			notes="新增敏感词"
			)
	@ApiImplicitParam(name="sensitiveWord",value="敏感词实体类",required=true,paramType="path",dataType="SensitiveWord")
	@PostMapping("/add")
	protected Object addSensitiveWord(@RequestParam SensitiveWord sensitiveWord){
		Assert.isNull(sensitiveWord.getWord());
		return iSensitiveWordService.addSensitiveWord(sensitiveWord);
	}
	
	
	/**
	 * @Title updateSensitiveWord
	 * @description 修改敏感词
	 * @param sensitiveWord
	 * @return
	 */
	@ApiOperation(
			value="修改敏感词",
			notes="修改敏感词"
			)
	@ApiImplicitParam(name="sensitiveWord",value="敏感词实体类",required=true,paramType="path",dataType="SensitiveWord")
	@PostMapping("/update")
	protected Object updateSensitiveWord(@RequestParam SensitiveWord sensitiveWord){
		Assert.isNull(sensitiveWord.getId(),sensitiveWord.getWord());
		return iSensitiveWordService.updateSensitiveWord(sensitiveWord);
	}
	
	/**
	 * @Title deleteSensitiveWord
	 * @description 删除敏感词
	 * @param ids
	 * @return
	 */
	@ApiOperation(
			value="批量删除敏感词",
			notes="批量删除敏感词"
			)
	@ApiImplicitParam(name="ids",value="敏感词id数组",required=true,paramType="path",dataType="int[]")
	@PostMapping("/delete")
	protected Object deleteSensitiveWord(@RequestParam Integer[] ids){
		return iSensitiveWordService.deleteSensitiveWord(ids);
	}
	
	

    /**
     * @Title addUser
     * @description 
     * @param file
     * @return
     * @throws IOException
     */
	@ApiOperation(
			value="excel导入敏感词",
			notes="excel导入敏感词"
			)
	@ApiImplicitParam(name="file",value="excel文件",required=true,paramType="path",dataType="MultipartFile")
    @PostMapping("/add/excel")
    public Object addSensitiveWord(@RequestParam("file") MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        return  iSensitiveWordService.batchImport(fileName, file);
    }
 

	
}
