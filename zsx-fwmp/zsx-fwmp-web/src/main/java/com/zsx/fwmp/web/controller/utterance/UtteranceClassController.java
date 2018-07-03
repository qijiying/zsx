package com.zsx.fwmp.web.controller.utterance;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Maps;
import com.zsx.framework.designpattern.factory.ResultfulFactory;
import com.zsx.framework.exception.enmus.ResultEnum;
import com.zsx.fwmp.web.others.base.ResultWebEnum;
import com.zsx.fwmp.web.others.util.Assert;
import com.zsx.fwmp.web.service.utterance.IUtteranceClassService;
import com.zsx.model.pojo.UtteranceClass;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * @ClassName UtteranceClassController
 * @author lz
 * @description 话语类别控制层
 * @date 2018年7月2日15:15:45
 */
@RestController
@RequestMapping("/api/web/utterance/class")
public class UtteranceClassController {

	@Autowired
	private IUtteranceClassService iUtteranceClassService;
	
	/**
	 * @Title addUtteranceClass
	 * @param utteranceClass
	 * @description 新增话语类别
	 * @return
	 */
	@ApiOperation(
			value="新增话语类别",
			notes="新增话语类别"
			)
	@ApiImplicitParam(name="utteranceClass",value="话语类别实体类",required=true,paramType="path",dataType="UtteranceClass")
	@PostMapping("/add")
	protected Object addUtteranceClass(@RequestBody UtteranceClass utteranceClass){
		Assert.isNull(utteranceClass.getName(),ResultWebEnum.PARAM_IS_NULL);
		Map<String,Object> map = Maps.newHashMap();
		if(utteranceClass.getName().length()<=50&&iUtteranceClassService.insert(utteranceClass)){
			map.put("code", 1);
		}else{map.put("code", 0);}	 
		return map;
	}
	
	
	/**
	 * @Title dataGridUtteranceClass
	 * @Description 初始化 话语类别列表
	 * @param current
	 * @param size
	 * @return
	 */
	@ApiOperation(
			value="初始化话语类别列表",
			notes="初始化话语类别列表"
			)
	@ApiImplicitParams({
		@ApiImplicitParam(name="current",value="当前页码",required=false,paramType="query",dataType="int"),
		@ApiImplicitParam(name="size",value="每页条数",required=false,paramType="query",dataType="int")
	})
	@GetMapping("/dataGrid")
	protected Object dataGridUtteranceClass(
			@RequestParam(value="current",required=false) Integer current,
			@RequestParam(value="size",required=false) Integer size
			){
		return ResultfulFactory
				.getInstance()
				.creator(ResultEnum.SUCCESS, iUtteranceClassService
						.dataGrid(new Page<>(current==null?1:current,size==null?10:size)));
	}
	
	
	/**
	 * @Title updateUtteranceClass
	 * @description 修改话语类别名称
	 * @param utteranceClass
	 * @return
	 */
	@ApiOperation(
			value="修改话语类别名称",
			notes="修改话语类别名称"
			)
	@ApiImplicitParam(name="utteranceClass",value="修改话语类别名称",required=true,paramType="path",dataType="UtteranceClass")
	@PostMapping("/update")
	protected Object updateUtteranceClass(@RequestParam UtteranceClass utteranceClass){
		Assert.isNull(utteranceClass.getId(), utteranceClass.getName());
		return iUtteranceClassService.updateUtteranceClass(utteranceClass);
	}
	
	
	/**
	 * @Title deleteUtteranceClass
	 * @description 批量删除话语类别
	 * @param ids
	 * @return
	 */
	@ApiOperation(
			value="批量删除话语类别",
			notes="批量删除话语类别"
			)
	@ApiImplicitParam(name="ids",value="话语类别id数组",required=true,paramType="path",dataType="int[]")
	@PostMapping("/delete")
	protected Object deleteUtteranceClass(@RequestParam Integer[] ids){
		return iUtteranceClassService.deleteUtteranceClass(ids);
	}
	
}
