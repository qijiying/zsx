package com.zsx.fwmp.web.service.group.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.zsx.dao.group.GroupMapper;
import com.zsx.framework.designpattern.factory.ResultfulFactory;
import com.zsx.framework.exception.SystemException;
import com.zsx.framework.exception.enmus.ResultEnum;
import com.zsx.fwmp.web.others.base.Log;
import com.zsx.fwmp.web.others.util.GroupUtil;
import com.zsx.fwmp.web.others.util.UserUtil;
import com.zsx.fwmp.web.service.area.IAreasService;
import com.zsx.fwmp.web.service.area.ICityService;
import com.zsx.fwmp.web.service.area.IProvincesService;
import com.zsx.fwmp.web.service.file.IFileManageService;
import com.zsx.fwmp.web.service.group.IGroupMemberService;
import com.zsx.fwmp.web.service.group.IGroupService;
import com.zsx.model.pojo.City;
import com.zsx.model.pojo.FileManage;
import com.zsx.model.pojo.Group;
import com.zsx.model.pojo.GroupMember;
import com.zsx.model.webdto.GroupDto;
import com.zsx.thirdparty.base.dto.RingLetterGroupDto;
import com.zsx.thirdparty.ringletter.IRingletterGroupService;
import com.zsx.utils.bean.ObjectUtils;
import com.zsx.utils.string.StringUtils;

/**
 * @ClassName GroupServiceImpl
 * @author lz
 * @description 群组业务实现类
 * @date 2018年6月22日15:48:24
 */
@Service
public class GroupServiceImpl extends ServiceImpl<BaseMapper<Group>, Group> implements IGroupService {

	@Autowired
	private GroupMapper groupMapper;
	
	@Autowired
	IRingletterGroupService iRingletterGroupService;  //环信群组API
	
	@Autowired
	IFileManageService iFileManageService;
	
	@Autowired
	ICityService iCityService;  //城市基础服务
	
	@Autowired
	IProvincesService iProvincesService;  //省份基础服务
	
	@Autowired
	IAreasService iAreasService;
	
	@Autowired
	IGroupMemberService iGroupMemberService;
	
	/**
	 * @Title getGroupList
	 * @see com.zsx.fwmp.web.service.group.IGroupService#getGroupList(com.baomidou.mybatisplus.plugins.Page)
	 * @description 获得群组列表
	 */
	@Override
	public Page<Group> getGroupList(Page<Group> page) {
		page.setRecords(groupMapper.selectList(new EntityWrapper<Group>().orderBy("create_time", false)));
		page.setTotal(selectCount(new EntityWrapper<Group>()));
		return page;
	}
	
	
	/**
	  * (非 Javadoc) 
	  * <p>Title: editGroup</p> 
	  * <p>Description: 编辑群资料</p> 
	  * @param groupDto
	  * @return 
	  * @see com.zsx.service.group.INormalGroupService#editGroup(com.zsx.model.dto.GroupDto)
	 */
	@Override
	public Object editGroup(Group group) {
		//TODO 修改群信息不走环信
		/*if(StringUtils.isNotBlank(group.getGroupName()) || StringUtils.isNotBlank(group.getGroupDesc())){  //如果群名称改变则更新环信
			if(!iRingletterGroupService.editGroup(group.getHxId(), group.getGroupName(), group.getGroupDesc(), null)){
				 throw new SystemException(ResultEnum.SERVER_GROUP_EDIT_ERROR);
			}
		}*/
		groupMapper.updateById(group);
		return ResultfulFactory.getInstance().creator(ResultEnum.SUCCESS);
	}
	
	/**
	  * (非 Javadoc) 
	  * <p>Title: delGroup</p> 
	  * <p>Description: 删除群/解散群</p> 
	  * @param groupDto
	  * @return 
	  * @see com.zsx.service.group.INormalGroupService#delGroup(com.zsx.model.dto.GroupDto)
	 */
	@Transactional(rollbackFor=SystemException.class)
	@Override
	public Object delGroup(GroupDto groupDto) {
		if(!iRingletterGroupService.delGroup(groupDto.getHxId())){
			throw new SystemException(ResultEnum.SERVER_GROUP_DEL_ERROR);
		}
		groupMapper.deleteById(groupDto.getId()); //删除群信息
		iFileManageService.delete(new EntityWrapper<FileManage>().where("source_id={0}", groupDto.getId())); //删除群头像
		iGroupMemberService.delete(new EntityWrapper<GroupMember>().where("group_id={0}", groupDto.getId()));  //删除群成员
		return ResultfulFactory.getInstance().creator(ResultEnum.SUCCESS);
	}
	
	
	/**
	  * (非 Javadoc) 
	  * <p>Title: createGroup</p> 
	  * <p>Description: </p> 
	  * @param group
	  * @param owner
	  * @return 
	  * @see com.zsx.service.group.INormalGroupService#createGroup(com.zsx.model.pojo.Group, java.lang.Long)
	 */
	@Transactional(rollbackFor=SystemException.class)
	@Override
	public Object createGroup(GroupDto groupDto) {
		//TODO 目前不显示创建多少个群
		RingLetterGroupDto dto=new RingLetterGroupDto();
		GroupMember member=new GroupMember();
		Group group=(Group) ObjectUtils.convertObject(new Group(), groupDto);
		
		dto.setGroupname(groupDto.getGroupName());
		dto.setDesc(groupDto.getGroupDesc()); 
		dto.setOwner(groupDto.getOwnerId().toString());
		/*dto.setPublic(groupDto.getGroupIsPublic()==1?true:false);
		dto.setAllowinvites(groupDto.getGroupAllowinvites()==1?true:false);
		dto.setMembers_only(groupDto.getGroupMembersOnly()==1?true:false);*/
		
		String xhId=iRingletterGroupService.createGroup(dto);
		if(StringUtils.isBlank(xhId) || null == xhId){
			  throw new SystemException(ResultEnum.SERVER_GROUP_CREATE_ERROR);
		}
		group.setHxId(xhId);
		groupMapper.insert(group);
		member.setGroupId(group.getId());
		member.setIsAdmin(0);
		member.setIsOwner(1);
		member.setUserId(groupDto.getOwnerId());
		member.setXhId(xhId);
		
		iGroupMemberService.insert(member);
		return ResultfulFactory.getInstance().creator(ResultEnum.SUCCESS,iGroupMemberService.selectById(member.getId()));
	}

	/**
	  * (非 Javadoc) 
	  * <p>Title: getGroupDeatil</p> 
	  * <p>Description: 获取单个群信息，以及成员信息</p> 
	  * @param groupId
	  * @return 
	  * @see com.zsx.service.group.INormalGroupService#getGroupDeatil(java.lang.Long)
	 */
	@Override
	public Object getGroupDeatil(Long groupId,Long userId) {
		GroupDto dto=groupMapper.getGroupDeatil(groupId,null);
		if(dto == null){
			//throw new SystemException(ResultEnum.SERVER_SELECT_ID_BY_DATA_NULL);
			Log.debug("当前群信息已经找不到了，可能是再解散群的时候， 客户端自动调用接口导致!具体请排查....",GroupServiceImpl.class);
		}
		List<GroupMember> member_list= iGroupMemberService.selectList(new EntityWrapper<GroupMember>().where("group_id={0}", groupId).and("user_id={0}",userId));
		dto.setSelectGroupType(GroupUtil.checkUserOfGroupRelation(member_list)==null?5:GroupUtil.checkUserOfGroupRelation(member_list));
		
		if(dto.getGroupAreaCode()!=0){  //不是默认值的话
			if(dto.getGroupCityCode() == 0){
				City city=iAreasService.getCityNameByAreasCode(dto.getGroupAreaCode().toString());
				dto.setGroupCityCode(city.getCityId());
				dto.setGroupProvinceCode(city.getProvinceId());
			}
			dto.setAreasCodeName(iAreasService.getCodeByAreasName(dto.getGroupCityCode().toString(), dto.getGroupAreaCode().toString()));
			dto.setCityCodeName(iCityService.getCodeByCityName(dto.getGroupProvinceCode().toString(), dto.getGroupCityCode().toString()));
			dto.setProvincesCodeName(iProvincesService.getCodeByProvincesName(dto.getGroupProvinceCode().toString()));
		}
		return dto;
	}
	
	
	/**
	  * (非 Javadoc) 
	  * <p>Title: getGroupByHxId</p> 
	  * <p>Description:根据环信ID查询群详情 </p> 
	  * @param hxid
	  * @return 
	  * @see com.zsx.service.group.INormalGroupService#getGroupByHxId(java.lang.String)
	 */
	@Override
	public Object getGroupByHxId(String hxid,Long userId) {
		GroupDto dto= groupMapper.getGroupDeatil(null,hxid);
		if(null == dto){
			throw new SystemException(ResultEnum.SERVER_SELECT_ID_BY_DATA_NULL).set("hxid", hxid);
		}
		EntityWrapper<GroupMember> entityWrapper=new EntityWrapper<>();
		entityWrapper.where("xh_id={0}", hxid);
		if(null != userId){
			entityWrapper.and("user_id={0}",userId);
		}
		List<GroupMember> member_list= iGroupMemberService.selectList(entityWrapper);
		dto.setSelectGroupType(GroupUtil.checkUserOfGroupRelation(member_list));
		if(dto.getGroupAreaCode()!=0){  //不是默认值的话
			if(dto.getGroupCityCode() == 0){
				City city=iAreasService.getCityNameByAreasCode(dto.getGroupAreaCode().toString());
				dto.setGroupCityCode(city.getCityId());
				dto.setGroupProvinceCode(city.getProvinceId());
			}
			dto.setAreasCodeName(iAreasService.getCodeByAreasName(dto.getGroupCityCode().toString(), dto.getGroupAreaCode().toString()));
			dto.setCityCodeName(iCityService.getCodeByCityName(dto.getGroupProvinceCode().toString(), dto.getGroupCityCode().toString()));
			dto.setProvincesCodeName(iProvincesService.getCodeByProvincesName(dto.getGroupProvinceCode().toString()));
		}
		return dto;
	}


	/**
	  * (非 Javadoc) 
	  * <p>Title: assignmentGroup</p> 
	  * <p>Description:转让群 </p> 
	  * @param xhId
	  * @param groupId
	  * @param owner
	  * @return 
	  * @see com.zsx.service.group.INormalGroupService#assignmentGroup(java.lang.String, java.lang.Long, java.lang.Long)
	 */
	@Transactional(rollbackFor=SystemException.class)
	@Override
	public Object assignmentGroup(GroupDto groupDto) {
		if(!iRingletterGroupService.assignmentGroup(groupDto.getHxId(), groupDto.getAssigOwnerId().toString())){
			throw new SystemException(ResultEnum.SERVER_GROUP_ASSIGNMENT_ERROR);
		}
		iGroupMemberService.update(new GroupMember(groupDto.getId(), groupDto.getHxId(), groupDto.getOwnerId(), 0, 0),new EntityWrapper<GroupMember>()
				.where("group_id={0}", groupDto.getId())
				.and("user_id={0}",groupDto.getOwnerId()));  //将原有群拥有者变成普通成员
		iGroupMemberService.update(new GroupMember(groupDto.getId(), groupDto.getHxId(), groupDto.getAssigOwnerId(), 0, 1),new EntityWrapper<GroupMember>()
				.where("group_id={0}", groupDto.getId())
				.and("user_id={0}",groupDto.getAssigOwnerId()));  //将被转让的成员变成群主
		return ResultfulFactory.getInstance().creator(ResultEnum.SUCCESS);
	}
	
	
	/**
	  * (非 Javadoc) 
	  * <p>Title: serchGroup</p> 
	  * <p>Description: </p> 
	  * @param p
	  * @param code
	  * @return 
	  * @see com.zsx.service.group.INormalGroupService#serchGroup(java.lang.String, java.lang.Integer)
	 */
	@Override
	public Page<Group> serchGroup(String parameter,Integer code,Page<Group> page,Integer type) {
		Map<String, Object> map=Maps.newHashMap();
		try {
			if(StringUtils.isBlank(parameter) && null != parameter){
				if(UserUtil.isInteger(parameter)){
					map.put("groupNumber", parameter);
				}else{
					map.put("groupName", parameter);
				}
			}
			map.put("code", UserUtil.getCityCode(code));
			map.put("type", type);
			page.setRecords(groupMapper.serchGroup(map,page));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return page;
	}
	
}
