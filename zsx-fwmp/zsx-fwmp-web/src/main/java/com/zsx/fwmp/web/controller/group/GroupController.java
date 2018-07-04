package com.zsx.fwmp.web.controller.group;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.plugins.Page;
import com.zsx.framework.designpattern.factory.ResultfulFactory;
import com.zsx.framework.exception.enmus.ResultEnum;
import com.zsx.fwmp.web.others.listener.PropertiesListenerConfig;
import com.zsx.fwmp.web.others.util.Assert;
import com.zsx.fwmp.web.service.group.IGroupMemberService;
import com.zsx.fwmp.web.service.group.IGroupService;
import com.zsx.model.pojo.Group;
import com.zsx.model.webdto.GroupDto;
import com.zsx.utils.string.StringUtils;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @ClassName GroupController
 * @author lz
 * @description 群组控制类
 * @date 2018年6月22日15:16:16
 */
@RestController
@RequestMapping("/api/web/group")
public class GroupController {

	@Autowired
	private IGroupService iGroupService;
	
	@Autowired
	private IGroupMemberService iGroupMemberService;

	
	/**
	 * @Title getGroupList
	 * @description 获得环信群组列表
	 * @return
	 */
	@ApiOperation(
			value="群组列表", 
			notes="群组列表")
	@GetMapping("/dataGrid")
	protected Object getGroupList(
			@RequestParam(value="current",required=false) Integer current,
			@RequestParam(value="size",required=false) Integer size
			){
	     return ResultfulFactory
	    		 .getInstance()
	    		 .creator(ResultEnum.SUCCESS, iGroupService
	    				 .getGroupList(new Page<Group>(current==null?1:current,size==null?10:size)));	
	}
	
	
	/**
	  * 
	  * @Title: createGroup 
	  * @Description: 创建群组 
	  * @param @param group
	  * @param @param owner
	  * @param @return    设定文件 
	  * @return Object    返回类型 
	  * @throws
	 */
	@ApiOperation(
			value="创建群组", 
			notes="创建群组")
	@PostMapping("/add")
	public Object createGroup(@ApiParam @RequestBody GroupDto groupDto){
		Assert.isNull(groupDto.getId(),groupDto.getGroupName());
		groupDto.setGroupDesc(StringUtils.isBlank(groupDto.getGroupDesc())?PropertiesListenerConfig.getProperty("default.group.desc"):groupDto.getGroupDesc());
		return iGroupService.createGroup(groupDto);
	}
	
	
	
	/**
	  * 
	  * @Title: assignmentGroup 
	  * @Description: 转让群组 
	  * @param @return    设定文件 
	  * @return Object    返回类型 
	  * @throws
	 */
	@ApiOperation(
			value="转让群", 
			notes="转让群")
	@PutMapping("/assignment")
	public Object assignmentGroup(@ApiParam @RequestBody GroupDto groupDto){
		Assert.isNull(groupDto.getOwnerId(),groupDto.getAssigOwnerId(),groupDto.getId(),groupDto.getHxId());
		return iGroupService.assignmentGroup(groupDto);
	}
	
	/**
	  * 
	  * @Title: delGroup 
	  * @Description: 解散群(删除群) 是否需要通知群成员？
	  * @param @param groupId
	  * @param @return    设定文件 
	  * @return Object    返回类型 
	  * @throws
	 */
	@ApiOperation(
			value="解散群组", 
			notes="解散群组")
	@DeleteMapping("/delGroup")
	public Object delGroup(@ApiParam @RequestBody GroupDto groupDto){
		//TODO 是否校验只有群主才能解散
		Assert.isNull(groupDto.getId(),groupDto.getHxId());
		return iGroupService.delGroup(groupDto); 
	}
	
	
	/**
	  * 
	  * @Title: editGroup 
	  * @Description: 编辑群资料 
	  * @param @param groupDto
	  * @param @return    设定文件 
	  * @return Object    返回类型 
	  * @throws
	 */
	@ApiOperation(
			value="编辑群资料", 
			notes="编辑群资料")
	@PostMapping("/update")
	public Object editGroup(@ApiParam @RequestBody Group group){
		Assert.isNull(group.getId(),group.getHxId());
		return iGroupService.editGroup(group);
	}
	
	
	/**
	  *  
	  * @Title: getGroupDeatil 
	  * @Description: 获取单个群信息 
	  * @param @param groupId
	  * @param @return    设定文件 
	  * @return Object    返回类型 
	  * @throws
	 */
	@ApiOperation(
			value="获取群详情", 
			notes="获取群详情")
	@ApiImplicitParams({
		@ApiImplicitParam(name="id",value="ID,如果Type=1，则是群组ID，否则视为环信ID",required=true,paramType="path",dataType="int"),
		@ApiImplicitParam(name="userId",value="当前查看人ID",required=true,paramType="path",dataType="int"),
		@ApiImplicitParam(name="type",value="ID类型",required=true,paramType="query",dataType="int")
	})
	@GetMapping("/get/{id}/{userId}")
	public Object getGroupDeatil(@PathVariable Long id,
			@PathVariable Long userId,
			@RequestParam(value="t",required=true) Integer type){
		Assert.isNull(type);
		GroupDto dto=null;
		if(type ==1){
			dto=(GroupDto) iGroupService.getGroupDeatil(id,userId);
		}else{
			dto=(GroupDto) iGroupService.getGroupByHxId(id.toString(),userId);
		}
		Assert.isNull(dto, ResultEnum.SERVER_SELECT_ID_BY_DATA_NULL);
		return ResultfulFactory.getInstance().creator(ResultEnum.SUCCESS,dto);
	}
	
	
	/**
	  * 
	  * @Title: getMemberByGroupId 
	  * @Description: 查询群成员
	  * @param @param groupId
	  * @param @return    设定文件 
	  * @return Object    返回类型 
	  * @throws
	 */
	@ApiOperation(
			value="查询群成员", 
			notes="查询群成员")
	@ApiImplicitParams({
		@ApiImplicitParam(name="groupId",value="群组ID",required=true,paramType="path",dataType="int"),
		@ApiImplicitParam(name="t",value="当前成员与群的关系",required=true,paramType="query",dataType="int")
	})
	@GetMapping("/{groupId}")
	public Object getMemberByGroupId(@PathVariable Long groupId
			,@RequestParam(value="t",required=false) Integer selectType){
		Assert.isNull(groupId);
		return ResultfulFactory.getInstance().creator(ResultEnum.SUCCESS,iGroupMemberService.getMemberByGroupId(groupId,selectType));
	}
	
	
	/**
	  * 
	  * @Title: serchGroup 
	  * @Description: 群组高级搜索 
	  * @param @return    设定文件 
	  * @return Object    返回类型 
	  * @throws
	 */
	@ApiOperation(
			value="群高级搜索", 
			notes="群高级搜索")
	@ApiImplicitParams({
		@ApiImplicitParam(name="code",value="区域CODE",required=false,paramType="path",dataType="int"),
		@ApiImplicitParam(name="p",value="名称或者群号",required=false,paramType="query"),
		@ApiImplicitParam(name="t",value="查区域群还是非区域群 [0:区域群],[1:非区域群]",required=false,paramType="query",dataType="int"),
		@ApiImplicitParam(name="c",value="当前页码",required=false,paramType="query",dataType="int"),
		@ApiImplicitParam(name="s",value="数据个数",required=false,paramType="query",dataType="int")
	})
	@GetMapping("/serch/{code}")
	public Object serchGroup(@RequestParam(value="p",required=false) String parameter,
			@PathVariable(value="code",required=false) Integer code,
			@RequestParam(value="t",required=false)Integer type,
			@RequestParam(value="c") Integer current,
			@RequestParam(value="s") Integer size){
		
		return ResultfulFactory.getInstance().creator(ResultEnum.SUCCESS,iGroupService.serchGroup(parameter,code,new Page<>(current, size),type));
	}
}