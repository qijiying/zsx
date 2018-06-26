package com.zsx.fwmp.web.others.util;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.zsx.framework.exception.SystemException;
import com.zsx.framework.exception.enmus.ResultEnum;
import com.zsx.model.dto.GroupMemberDto;
import com.zsx.model.dto.GroupMembersDto;
import com.zsx.model.pojo.GroupMember;

/**
  * 
  * @ClassName: GroupUtil 
  * @Description: 群工具类 
  * @author xiayy 
  * @date 2018年3月23日 上午9:59:18 
  *
 */
public class GroupUtil {
	
	/**
	  * 
	  * @Title: filterMember 
	  * @Description: 通过查询方式，筛选成员 
	  * @param @param selectType
	  * @param @return    设定文件 
	  * @return List<GroupMemberDto>    返回类型 
	  * @throws
	 */
	public static GroupMembersDto filterMember(List<GroupMemberDto> list,Integer selectType){
		List<GroupMemberDto> member_list=null;
		
		Predicate<GroupMemberDto> admin_yes = (n) -> n.getIsAdmin() == 1;
		Predicate<GroupMemberDto> admin_no = (n) -> n.getIsAdmin() == 0;
		
		Predicate<GroupMemberDto> owner_yes = (n) -> n.getIsOwner() == 1;
		Predicate<GroupMemberDto> owner_no = (n) -> n.getIsOwner() == 0;
		
		
		GroupMembersDto dto=new GroupMembersDto();
		dto.setSum(list.size());
		dto.setAmdinCount(list.stream().filter(admin_yes.or(owner_yes)).collect(Collectors.toList()).size());
	
		switch (selectType) {
			case 1 :  //普通成员
				member_list=list.stream().filter(admin_no.and(owner_no)).collect(Collectors.toList());
				break;
			case 2 :  //管理员
				member_list=list.stream().filter(admin_yes).collect(Collectors.toList());
				break;
			case 3 : //管理员和群主
				member_list=list.stream().filter(admin_yes.or(owner_yes)).collect(Collectors.toList());
				break;
			case 4 : //管理员和成员
				member_list=list.stream().filter(admin_yes.or(owner_no)).collect(Collectors.toList());
				break;
			case 5 : //群主
				member_list=list.stream().filter(owner_yes).collect(Collectors.toList());
				break;
			case 6 : //群主和成员
				member_list=list.stream().filter(owner_yes.or(admin_no)).collect(Collectors.toList());
				break;
			default :
				dto.setGroupMemberDtos(list);
				return dto;
		}
		dto.setGroupMemberDtos(member_list);
		return dto;
	}
	
	/**
	  * 
	  * @Title: memberArraySplitToList 
	  * @Description: 数据分割，并返回list 
	  * @param @param members    设定文件 
	  * @return void    返回类型 
	  * @throws
	 */
	public static List<String> memberArraySplitToList(String[] members){
		
		if(null == members){
			throw new SystemException(ResultEnum.CHECK_PARAMETER_NULL_ERROR);
		}
		if(members.length == 0){
			return null;
		}
		if(members.length > 1){
			return Arrays.asList(members);
		}
		
		return null;
	}
	
	/**
	  * 
	  * @Title: checkUserOfGroupRelation 
	  * @Description: 判断当前成员与群属于什么关系
	  * 1:群主  2:管理员  3:群成员 4:非群成员
	  * @param @param member_list
	  * @param @return    设定文件 
	  * @return Integer    返回类型 
	  * @throws
	 */
	public static Integer checkUserOfGroupRelation(List<GroupMember> member_list){
		if(null !=member_list && member_list.size() >2){
			throw new SystemException(ResultEnum.SERVER_GROUP_SELECT_MEMBER_ERROR);
		}
		if(null == member_list || member_list.size() == 0){
			return 4;
		}
		GroupMember groupMember=member_list.get(0);
		if(groupMember.getIsOwner() == 1){
			return 1;
		}else if(groupMember.getIsAdmin() ==1){
			return 2;
		}else if(groupMember.getIsOwner() ==0 && groupMember.getIsAdmin()==0){
			return 3;
		}
		return null;
	}
}
