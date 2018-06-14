package com.zsx.fwmp.web.controller.area;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zsx.framework.designpattern.factory.ResultfulFactory;
import com.zsx.framework.exception.enmus.ResultEnum;
import com.zsx.fwmp.web.service.area.IAreasService;

/**
 * @Title AreaController
 * @author lz
 * @description 处理 区/市/省 的controller
 */
@RestController
@RequestMapping("/api/area")
public class AreaController {
	
	 @Autowired
     private IAreasService iAreasService;
     
/*	 @Autowired
     private ICityService iCityService;
     
	 @Autowired
     private IProvincesService iProvincesService;*/
	 

	/**
	 * @Title getAreasGrid
	 * @description 获得区域列表
	 * @return
	 */
	@PostMapping("/areas")
	 protected Object getAreasGrid(){
		 return ResultfulFactory.getInstance().creator(ResultEnum.SUCCESS,iAreasService.selectAreasList());
	 }
	 
/*	 @PostMapping("/city")
	 protected Object getCityGrid(){
		 return ResultfulFactory.getInstance().creator(ResultEnum.SUCCESS,iCityService.selectCityList());
	 }
	 
	 @PostMapping("/provinces")
	 protected Object getProvincesGrid(){
		 return ResultfulFactory.getInstance().creator(ResultEnum.SUCCESS,iProvincesService.selectProvincesList());
	 }*/

}
