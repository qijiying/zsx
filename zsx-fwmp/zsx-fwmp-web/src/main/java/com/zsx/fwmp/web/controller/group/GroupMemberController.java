package com.zsx.fwmp.web.controller.group;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zsx.framework.exception.enmus.ResultEnum;
import com.zsx.fwmp.web.others.util.Assert;
import com.zsx.fwmp.web.service.group.IGroupMemberService;
import com.zsx.model.webdto.GroupDto;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/api/web/group/member")
public class GroupMemberController {

	
	@Autowired
	IGroupMemberService iGroupMemberService;
	
	 /**
	  * 
	  * @Title: groupAddMember 
	  * @Description: 添加群成员
	  * @param @param groupDto
	  * @param @return    设定文件 
	  * @return Object    返回类型 
	  * @throws
	 */
	@ApiOperation(
			value="添加群成员", 
			notes="添加群成员")
	@PostMapping("/add")
	public Object groupAddMember(@ApiParam @RequestBody GroupDto groupDto){
		Assert.isNull(groupDto.getSelectGroupType(), ResultEnum.CHECK_GROUP_ROLE_ERROR);
		Assert.isNull(groupDto.getId());
		Assert.isNull(groupDto.getMembers(),ResultEnum.CHECK_ADD_MEMBER_GROUP_ERROR);
		return iGroupMemberService.groupAddMember(groupDto);
	}
	
	
	/**
	  * 
	  * @Title: delGroupMember 
	  * @Description: 移除群成员
	  * @param @param groupDto
	  * @param @return    设定文件 
	  * @return Object    返回类型 
	  * @throws
	 */
	@ApiOperation(
			value="移除群成员", 
			notes="移除群成员")
	@DeleteMapping("/delMember")
	public Object delGroupMember(@ApiParam @RequestBody GroupDto groupDto){
		return iGroupMemberService.delGroupMember(groupDto);
	}
	
	/**
	  * 
	  * @Title: setAdminMember 
	  * @Description: 设置群管理员 
	  * @param @param groupDto
	  * @param @return    设定文件 
	  * @return Object    返回类型 
	  * @throws
	 */
	@ApiOperation(
			value="设置群管理员", 
			notes="设置群管理员")
	@PostMapping("/addAdmin")
	public Object addAdminMember(@ApiParam @RequestBody GroupDto groupDto){
		return iGroupMemberService.addAdminMember(groupDto);
	}
	
	
	/**
	  * 
	  * @Title: delAdminMember 
	  * @Description: 移除群管理员 
	  * @param @param groupDto
	  * @param @return    设定文件 
	  * @return Object    返回类型 
	  * @throws
	 */
	@ApiOperation(
			value="移除群管理员 ", 
			notes="移除群管理员 ")
	@DeleteMapping("/delAdmin")
	public Object delAdminMember(@ApiParam @RequestBody GroupDto groupDto){
		return iGroupMemberService.delAdminMember(groupDto);
	}
}
