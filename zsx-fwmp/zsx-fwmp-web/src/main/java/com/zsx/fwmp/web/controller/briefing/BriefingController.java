package com.zsx.fwmp.web.controller.briefing;

import java.text.ParseException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;
import com.zsx.framework.designpattern.factory.ResultfulFactory;
import com.zsx.framework.exception.enmus.ResultEnum;
import com.zsx.fwmp.web.service.briefing.IBriefingService;
import com.zsx.model.pojo.Briefing;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * @ClassName BriefingController
 * @author lz
 * @description 简报控制层
 * @date 2018年6月26日16:21:59
 */
@RestController
@RequestMapping("/api/web/briefing")
public class BriefingController {

	@Autowired
	IBriefingService iBriefingService;
	
	/**
	 * @Title addBriefing
	 * @description 新增简报加推送
	 * @param briefing
	 * @return
	 * @throws ParseException 
	 */
	@ApiOperation(
			value="新增简报加推送",
			notes="新增简报加推送"
			)
	@ApiImplicitParam(name="addBriefing",value="简报实体类",required=true,paramType="path",dataType="Briefing")
	@PostMapping("/add")
	protected Object addBriefing(@RequestBody Briefing briefing) throws ParseException{
		return ResultfulFactory
				.getInstance()
				.creator(ResultEnum.SUCCESS, iBriefingService.addBriefing(briefing));
	} 
	
	
	/**
	 * @Title dataGridBriefing
	 * @description 简报列表
	 * @param current
	 * @param size
	 * @return
	 */
	@ApiOperation(
			value="简报初始化列表",
			notes="简报初始化列表"
			)
	@ApiImplicitParams({
		@ApiImplicitParam(name="current",value="当前页码",required=false,paramType="query",dataType="int"),
		@ApiImplicitParam(name="size",value="每页条数",required=false,paramType="")
	})
	@GetMapping("/dataGrid")
	protected Object dataGridBriefing(
			@RequestParam(value="current",required=false) Integer current,
			@RequestParam(value="size",required=false) Integer size
			){
		return ResultfulFactory
				.getInstance()
				.creator(ResultEnum.SUCCESS, iBriefingService
						.dataGridBriefing(current==null?1:current,size==null?10:size));
	}
	
	
	/**
	 * @Title deleteBriefing
	 * @param ids
	 * @description 批量删除简报
	 * @return
	 */
	@ApiOperation(
			value="批量删除简报",
			notes="批量删除简报"
			)
	@ApiImplicitParam(name="ids",value="简报id数组",required=true,paramType="path",dataType="int[]")
	@PostMapping("/delete")
	protected Object deleteBriefing(@RequestParam Integer[] ids){
		Map<String,Object> map = Maps.newHashMap();
		if(iBriefingService.deleteBriefing(ids)) map.put("code", 1);
		else map.put("code", 0);
		return map;
	}
	
	
	
	/**
	 * @Title updateBriefing
	 * @description 更新简报
	 * @param briefing
	 * @return
	 */
	@ApiOperation(
			value="更新简报",
			notes="更新简报"
			)
	@ApiImplicitParam(name="briefing",value="简报实体类",required=true,paramType="path",dataType="Briefing")
	@PostMapping("/update")
	protected Object updateBriefing(@RequestBody Briefing briefing){
		Map<String,Object> map = Maps.newHashMap();
		if(iBriefingService.updateById(briefing)) map.put("code", 1);
		else map.put("code", 0);
		return map;
	}
	
}
