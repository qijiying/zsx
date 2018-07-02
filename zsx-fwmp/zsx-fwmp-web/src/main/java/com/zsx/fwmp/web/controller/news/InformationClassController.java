package com.zsx.fwmp.web.controller.news;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zsx.framework.designpattern.factory.ResultfulFactory;
import com.zsx.framework.exception.enmus.ResultEnum;
import com.zsx.fwmp.web.others.util.Assert;
import com.zsx.fwmp.web.service.news.IInformationClassService;
import com.zsx.model.pojo.InformationClass;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * @ClassName InformationClassController
 * @author lz
 * @description 资讯类型控制层接口
 * @date 2018年6月26日20:08:54
 */
@RestController
@RequestMapping("/api/web/news/information/class")
public class InformationClassController {

	@Autowired
	IInformationClassService iInformationClassService;
	
	/**
	 * @Title addInCl
	 * @param informationClass
	 * @description 添加资讯类型
	 * @return
	 */
	@ApiOperation(
			value="添加资讯类型",
			notes="添加资讯类型"
			)
	@ApiImplicitParam(name="informationClass",value="资讯类型实体类",required=true,paramType="path",dataType="InformationClass")
	@PostMapping("/add")
	protected Object addInCl(@RequestBody InformationClass informationClass){
		Assert.isNull(informationClass.getName());
		return iInformationClassService.insert(informationClass);
	}
	
	
	/**
	 * @Title dataFridInCl
	 * @param current
	 * @param size
	 * @description 资讯初始化列表
	 * @return
	 */
	@ApiOperation(
			value="资讯初始化列表",
			notes="资讯初始化列表"
			)
	@ApiImplicitParams({
		@ApiImplicitParam(name="current",value="当前页码",required=false,paramType="query",dataType="int"),
		@ApiImplicitParam(name="size",value="每页条数",required=false,paramType="query",dataType="int")
	})
	@GetMapping("/dataGrid")
	protected Object dataGridInCl(
			@RequestParam(value="current",required=false) Integer current,
			@RequestParam(value="size",required=false) Integer size
			){
		return ResultfulFactory
				.getInstance()
				.creator(ResultEnum.SUCCESS, iInformationClassService
						.dataGridInCl(current==null?1:current,size==null?10:size));
	}
	
	/**
	 * @Title updateInCl
	 * @description 更新资讯的分类表
	 * @param informationClass
	 * @return
	 */
	@ApiOperation(
			value="更新资讯分类表",
			notes="更新资讯分类表"
			)
	@ApiImplicitParam(name="informationClass",value="资讯类型实体类",required=true,paramType="path",dataType="InformationClass")
	@PostMapping("/update")
	protected Object updateInCl(@RequestBody InformationClass informationClass){
		Assert.isNull(informationClass.getId(),informationClass.getName());
		return iInformationClassService.updateById(informationClass);
	}
	
	
	/**
	 * @Title deleteInCl
	 * @param ids
	 * @description 批量删除资讯类型
	 * @return
	 */
	@ApiOperation(
			value="批量删除资讯类型",
			notes="批量删除资讯类型"
			)
	@ApiImplicitParam(name="ids",value="资讯类型id数组",required=true,paramType="path",dataType="int[]")
	@PostMapping("/delete")
	protected Object deleteInCl(@RequestParam Integer[] ids){
		return iInformationClassService.deleteInCl(ids);
	}
}
