package com.zsx.fwmp.web.controller.utterance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.plugins.Page;
import com.zsx.framework.designpattern.factory.ResultfulFactory;
import com.zsx.framework.exception.enmus.ResultEnum;
import com.zsx.fwmp.web.others.base.ResultWebEnum;
import com.zsx.fwmp.web.others.util.Assert;
import com.zsx.fwmp.web.service.utterance.IUtteranceService;
import com.zsx.model.pojo.Utterance;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * @ClassName UtteranceController
 * @description 话语控制层
 * @author lz
 * @date 2018年7月2日15:37:59
 */
@RestController
@RequestMapping("/api/web/utterance")
public class UtteranceController {

	@Autowired
	private IUtteranceService iUtteranceService;

	
	/**
	 * @Title addUtterance
	 * @param utterance
	 * @description 新增话语
	 * @return
	 */
	@ApiOperation(
			value="新增话语",
			notes="新增话语"
			)
	@ApiImplicitParam(name="utterance",value="话语实体类",required=true,paramType="path",dataType="Utterance")
	@PostMapping("/add")
	protected Object addUtterance(@RequestBody Utterance utterance){
		Assert.isNull(utterance.getClassId(),utterance.getContent(),ResultEnum.CHECK_DATA_IS_NULL_ERROR);
		return iUtteranceService.addUtterance(utterance);
	}
	
	/**
	 * @Title dataGrid
	 * @param current
	 * @param size
	 * @description 初始化话语列表
	 * @return
	 */
	@ApiOperation(
			value="初始化话语列表",
			notes="初始化话语列表"
			)
	@ApiImplicitParams({
		@ApiImplicitParam(name="current",value="当前页码",required=false,paramType="query",dataType="int"),
		@ApiImplicitParam(name="size",value="每页条数",required=false,paramType="query",dataType="int")
	})
	@GetMapping("/dataGrid")
	protected Object dataGrid(
			@RequestParam(value="current",required=false) Integer current,
			@RequestParam(value="size",required=false) Integer size
			){
		return ResultfulFactory
				.getInstance()
				.creator(ResultEnum.SUCCESS, iUtteranceService
						.dataGrid(new Page<>(current==null?1:current,size==null?10:size)));
	}
	
	
	/**
	 * @Title updateUtterance
	 * @description 修改话语
	 * @param utterance
	 * @return
	 */
	@ApiOperation(
			value="修改话语",
			notes="修改话语"
			)
	@ApiImplicitParam(name="utterance",value="话语实体类",required=true,paramType="path",dataType="Utterance")
	@PostMapping("/update")
	protected Object updateUtterance(@RequestBody Utterance utterance){
		Assert.isNull(utterance.getId(),ResultWebEnum.ID_IS_NULL);
		return iUtteranceService.updateUtterance(utterance);
	}
	
	
	/**
	 * @Title deleteUtterance
	 * @description 批量删除话语
	 * @param ids
	 * @return
	 */
	@ApiOperation(
			value="批量删除话语",
			notes="批量删除话语"
			)
	@ApiImplicitParam(name="ids",value="话语id数组",required=true,paramType="path",dataType="int[]")
	@PostMapping("/delete")
	protected Object deleteUtterance(@RequestParam Integer[] ids){
		return iUtteranceService.deleteUtterance(ids);
	}
	
	
	/**
	 * @Title getRandomByClass
	 * @description 根据话语类别获得随机的话语
	 * @param id
	 * @return
	 */
	@ApiOperation(
			value="根据话语类别获得随机话语",
			notes="根据话语类别获得随机话语"
			)
	@ApiImplicitParam(name="id",value="类型id",required=true,paramType="path",dataType="int")
	@GetMapping("/class/{id}")
	protected Object getRandomByClass(@PathVariable Integer id){
		return iUtteranceService.getRandomByClass(id);
	}
	
}
