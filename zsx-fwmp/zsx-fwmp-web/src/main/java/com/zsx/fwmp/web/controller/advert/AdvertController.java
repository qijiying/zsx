package com.zsx.fwmp.web.controller.advert;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zsx.framework.designpattern.factory.ResultfulFactory;
import com.zsx.framework.exception.enmus.ResultEnum;
import com.zsx.fwmp.web.service.advert.IAdvertService;
import com.zsx.model.pojo.Advert;

/**
 * @author lz
 * @description 广告相关的处理类
 * @date 2018年5月28日12:40:06
 */
@RestController
@RequestMapping("/api/advert")
public class AdvertController {
	
	@Autowired
	private IAdvertService advertService;
	

	/**
	 * @Title insertAdvert
	 * @param advert
	 * @return Object
	 * @description 插入广告
	 */
	@PostMapping("/add")
	protected Object insertAdvert(@RequestBody Advert advert){
		return ResultfulFactory.getInstance().creator(ResultEnum.SUCCESS,advertService.insert(advert));
	}
	
	
	/**
	 * @Title selectAdvertByPage
	 * @param current
	 * @param size
	 * @description 获取广告
	 * @return
	 */
	@PostMapping("/dataGrid")
	protected Object selectAdvertByPage(
			 @RequestParam(value="current",required=false) Integer current,
			 @RequestParam(value="size",required=false) Integer size
			){
		return ResultfulFactory.getInstance().creator(ResultEnum.SUCCESS,advertService.selectAdByPage(current==null?1:current,size==null?10:size));
	}
	
	
	/**
	 * @Title getAdvert
	 * @param id
	 * @description 获取单个广告dto
	 * @return
	 */
/*	@PostMapping("/{id}")
	protected Object getAdvert(@PathVariable Long id){
		return ResultfulFactory.getInstance().creator(ResultEnum.SUCCESS,advertService.get(id));
	}*/
	
	
	/**
	 * @Title selectAdvertByAreaAndPositionPage
	 * @param postion
	 * @param areasCode
	 * @param areasType
	 * @param current
	 * @param size
	 * @description 搜索广告带分页
	 * @return
	 */
	@PostMapping("/dataSearch")
	protected Object selectAdvertByAreaAndStatusAndPage(
			 @RequestParam(value="title",required=false) String title,
			 @RequestParam(value="p",required=false) Integer postion,
			 @RequestParam(value="ls",required=false) Integer lockStatus,
			 @RequestParam(value="c",required=false) Integer areasCode,
			 @RequestParam(value="t",required=false) Integer areasType,
			 @RequestParam(value="current",required=false) Integer current,
			 @RequestParam(value="size",required=false) Integer size,
			 @RequestParam(value="st",required=false) Date startTime,
			 @RequestParam(value="et",required=false) Date endTime
			){
		return ResultfulFactory.getInstance().creator(ResultEnum.SUCCESS,advertService.selectAdvertByAreaAndStatusAndPage(new Advert(title,postion,lockStatus,areasCode,areasType,startTime,endTime),current==null?1:current, size==null?10:size));				
	}
	
	
	
	
	
	/**
	 * @Title updateAdvertById
	 * @param advert
	 * @return
	 * @description 修改广告
	 */
	@PostMapping("/update")
	protected Object updateAdvertById(@RequestBody Advert advert){
		return ResultfulFactory.getInstance().creator(ResultEnum.SUCCESS,advertService.updateById(advert));
	}
	
	
	/**
	 * @Title disAdvert
	 * @return
	 * @description 禁用广告
	 */
	@PostMapping("/forbidden")
	protected Object disAdvertById(@RequestParam long id){
		return ResultfulFactory.getInstance().creator(ResultEnum.SUCCESS,advertService.disAdvertById(id));
	}
	
	/**
	 * @param id
	 * @return
	 * @description 根据id删除广告
	 */
	@PostMapping("/delete")
	protected Object deleteAdvertById(@RequestParam Long id){
		return ResultfulFactory.getInstance().creator(ResultEnum.SUCCESS,advertService.deleteById(id));
	}
	
}
