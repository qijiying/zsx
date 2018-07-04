package com.zsx.fwmp.web.service.group.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zsx.dao.group.GroupMemberMapper;
import com.zsx.framework.designpattern.factory.ResultfulFactory;
import com.zsx.framework.exception.SystemException;
import com.zsx.framework.exception.enmus.ResultEnum;
import com.zsx.fwmp.web.others.util.GroupUtil;
import com.zsx.fwmp.web.others.util.UserUtil;
import com.zsx.fwmp.web.service.group.IGroupMemberService;
import com.zsx.fwmp.web.service.group.IGroupService;
import com.zsx.fwmp.web.service.message.IMessageService;
import com.zsx.fwmp.web.service.user.IUserService;
import com.zsx.model.pojo.Group;
import com.zsx.model.pojo.GroupMember;
import com.zsx.model.pojo.Message;
import com.zsx.model.pojo.User;
import com.zsx.model.webdto.GroupDto;
import com.zsx.model.webdto.GroupMemberDto;
import com.zsx.model.webdto.GroupMembersDto;
import com.zsx.thirdparty.jpush.AndroidJpushService;
import com.zsx.thirdparty.jpush.IosJpushService;
import com.zsx.thirdparty.ringletter.IRingletterGroupService;

@Service
public class GroupMemberServiceImpl extends ServiceImpl<BaseMapper<GroupMember>, GroupMember> implements IGroupMemberService {

	@Autowired
	GroupMemberMapper groupMemberMapper;
	
	@Autowired
	IGroupService  iGroupService;
	
	@Autowired
	IRingletterGroupService iRingletterGroupService;  //环信群组API
	
	@Autowired
	IMessageService iMessageService;
	
	@Autowired
	IUserService iUserService;
	
	@Autowired
	IosJpushService iosJpushService;
	
	@Autowired
	AndroidJpushService androidJpushService;
	
	/**
	  * (非 Javadoc) 
	  * <p>Title: groupAddMember</p> 
	  * <p>Description: 添加群成员</p> 
	  * @param groupId
	  * @param member
	  * @return 
	  * @see com.zsx.service.group.IGroupService#groupAddMember(java.lang.String, java.lang.String)
	 */
	@Override
	public Object groupAddMember(GroupDto groupDto) {
		synchronized (this) {
			Group group=iGroupService.selectById(groupDto.getId());
			if(groupDto.getSelectGroupType() == 3){  //如果是群成员角色的话，需要判断该群是否允许成员邀请进群
				if(group.getGroupAllowinvites() ==1){
					return ResultfulFactory.getInstance().creator(ResultEnum.SERVER_GROUP_ADD_ROLE_ERROR);
				}
			}
			
			List<GroupMember> member_list=groupMemberMapper.selectList(new EntityWrapper<GroupMember>().where("group_id={0}", groupDto.getId()).in("user_id={0}", groupDto.getMembers()));
			if(null == member_list || member_list.size() == 0){
				String[] member=groupDto.getMembers().split(",");
				if(member.length < 2){
					if(!iRingletterGroupService.addGroupMember(groupDto.getHxId(), member[0])){
						throw new SystemException(ResultEnum.SERVER_GROUP_ADDMEMBER_ERROR);
					}
					User user=iUserService.getCacheUser(Long.valueOf(member[0]));
					if(null != user.getAppSoucre()){
						UserUtil.jpush(user.getAppSoucre().equals(1)?iosJpushService:androidJpushService,member[0], "群通知", "你已经加入["+group.getGroupName()+"]群");
					}
					
					groupMemberMapper.insert(new GroupMember(groupDto.getId(), groupDto.getHxId(),Long.parseLong(member[0]), 0, 0));
				}else{
					 if(!iRingletterGroupService.addGroupMemberList(groupDto.getHxId(), GroupUtil.memberArraySplitToList(member))){
						 throw new SystemException(ResultEnum.SERVER_GROUP_ADDMEMBER_ERROR);
					 }
					 for (String userId : member) {
						 User user=iUserService.getCacheUser(Long.valueOf(userId));
						 if(null != user.getAppSoucre()){
							 UserUtil.jpush(user.getAppSoucre().equals(1)?iosJpushService:androidJpushService,user.getId().toString(), "群通知", "你已经加入["+group.getGroupName()+"]群");
						 }
						groupMemberMapper.insert(new GroupMember(groupDto.getId(), groupDto.getHxId(),Long.parseLong(userId), 0, 0));
					}
				}
				
			}else{
				return ResultfulFactory.getInstance().creator(ResultEnum.SERVER_GROUP_ADDMEMBER_EXISTS_ERROR);
			}
		}
		return ResultfulFactory.getInstance().creator(ResultEnum.SUCCESS);
	}
	
	
	/**
	  * (非 Javadoc) 
	  * <p>Title: delGroupMember</p> 
	  * <p>Description:移除群成员 </p> 
	  * @param groupDto
	  * @return 
	  * @see com.zsx.service.group.IGroupMemberService#delGroupMember(com.zsx.model.dto.GroupDto)
	 */
	@Override
	public Object delGroupMember(GroupDto groupDto) {
		synchronized (this) {
			String[] member=groupDto.getMembers().split(",");
			if(member.length < 2){
				if(!iRingletterGroupService.delGroupMember(groupDto.getHxId(), member[0])){
					throw new SystemException(ResultEnum.SERVER_GROUP_DEL_MEMBER_ERROR);
				}
				groupMemberMapper.delete(new EntityWrapper<GroupMember>().where("group_id={0}", groupDto.getId()).and("user_id={0}", member[0]));
				iMessageService.delete(new EntityWrapper<Message>().where("send_user_id={0}", member[0]).and("event_id={0}", groupDto.getId()));  //退群或者删除成员的时候，需要删除对应的消息表
			}else{
				 if(!iRingletterGroupService.delGourpMemberList(groupDto.getHxId(), GroupUtil.memberArraySplitToList(member))){
					 throw new SystemException(ResultEnum.SERVER_GROUP_DEL_MEMBER_ERROR);
				 }
				groupMemberMapper.delete(new EntityWrapper<GroupMember>().where("group_id={0}", groupDto.getId()).in("user_id", member));
				iMessageService.delete(new EntityWrapper<Message>().where("event_id={0}", groupDto.getId()).in("send_user_id", member));
			}
			
		}
		return ResultfulFactory.getInstance().creator(ResultEnum.SUCCESS);
	}
	
	/**
	 * (非 Javadoc) 
	  * <p>Title: getMemberByGroupId</p> 
	  * <p>Description: 获取群成员列表</p> 
	  * @param groupId
	  * @return 
	  * @see com.zsx.service.group.IGroupMemberService#getMemberByGroupId(java.lang.Long, java.lang.Integer)
	 */
	@Override
	public GroupMembersDto getMemberByGroupId(Long groupId,Integer selectType) {
		List<GroupMemberDto> member_list=groupMemberMapper.getMemberByGroupId(groupId);
		return GroupUtil.filterMember(member_list, null==selectType?0:selectType);
	}
	
	
	/**
	  * (非 Javadoc) 
	  * <p>Title: addAdminMember</p> 
	  * <p>Description: 设置群管理</p> 
	  * @param groupDto
	  * @return 
	  * @see com.zsx.service.group.IGroupMemberService#addAdminMember(com.zsx.model.dto.GroupDto)
	 */
	@Override
	public Object addAdminMember(GroupDto groupDto) {
		String[] member=groupDto.getMembers().split(",");
		if(member.length < 2){
			GroupMember groupMember= groupMemberMapper.selectOne(new GroupMember(groupDto.getId(), groupDto.getHxId(), Long.parseLong(member[0])));
			if(!iRingletterGroupService.addGroupOwner(groupDto.getHxId(), member[0])){
				throw new SystemException(ResultEnum.SERVER_GROUP_DEL_MEMBER_ERROR);
			}
			if(null == groupMember){  
				throw new SystemException(ResultEnum.SERVER_GROUP_SET_ADMIN_USER_ERROR);
			}else{
				groupMember.setIsAdmin(1);
				groupMemberMapper.updateById(groupMember);
			}
			
		}else{
			
			for (String string : GroupUtil.memberArraySplitToList(member)) {
				if(!iRingletterGroupService.addGroupOwner(groupDto.getHxId(), string)){
					 throw new SystemException(ResultEnum.SERVER_GROUP_DEL_MEMBER_ERROR);
				 }
				groupMemberMapper.update(new GroupMember(groupDto.getId(), groupDto.getHxId(), Long.parseLong(string),1,0), new EntityWrapper<GroupMember>().where("group_id={0}", groupDto.getId()).and("user_id={0}", string));
			}
		}
		return ResultfulFactory.getInstance().creator(ResultEnum.SUCCESS);
	}
	
	
	/**
	  * (非 Javadoc) 
	  * <p>Title: delAdminMember</p> 
	  * <p>Description: 移除群管理 </p> 
	  * @param groupDto
	  * @return 
	  * @see com.zsx.service.group.IGroupMemberService#delAdminMember(com.zsx.model.dto.GroupDto)
	 */
	@Override
	public Object delAdminMember(GroupDto groupDto) {
		String[] member=groupDto.getMembers().split(",");
		if(member.length < 2){
			if(!iRingletterGroupService.delGroupOwner(groupDto.getHxId(), member[0])){
				throw new SystemException(ResultEnum.SERVER_GROUP_DEL_MEMBER_ERROR);
			}
			groupMemberMapper.update(new GroupMember(groupDto.getId(), groupDto.getHxId(), Long.parseLong(member[0]),0,0), new EntityWrapper<GroupMember>().where("group_id={0}", groupDto.getId()).and("user_id={0}", member[0]));
		}else{
			
			for (String string : GroupUtil.memberArraySplitToList(member)) {
				if(!iRingletterGroupService.delGroupOwner(groupDto.getHxId(), string)){
					 throw new SystemException(ResultEnum.SERVER_GROUP_DEL_MEMBER_ERROR);
				 }
				groupMemberMapper.update(new GroupMember(groupDto.getId(), groupDto.getHxId(), Long.parseLong(string),0,0), new EntityWrapper<GroupMember>().where("group_id={0}", groupDto.getId()).and("user_id={0}", string));
			}
			
		}
		return ResultfulFactory.getInstance().creator(ResultEnum.SUCCESS);
	}
	
}
