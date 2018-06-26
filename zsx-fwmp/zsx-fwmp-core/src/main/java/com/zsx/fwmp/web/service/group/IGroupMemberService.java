package com.zsx.fwmp.web.service.group;

import com.baomidou.mybatisplus.service.IService;
import com.zsx.model.dto.GroupDto;
import com.zsx.model.dto.GroupMembersDto;
import com.zsx.model.pojo.GroupMember;

public interface IGroupMemberService extends IService<GroupMember> {

	Object groupAddMember(GroupDto groupDto);

	Object delGroupMember(GroupDto groupDto);

	GroupMembersDto getMemberByGroupId(Long groupId, Integer selectType);

	Object addAdminMember(GroupDto groupDto);

	Object delAdminMember(GroupDto groupDto);

}
