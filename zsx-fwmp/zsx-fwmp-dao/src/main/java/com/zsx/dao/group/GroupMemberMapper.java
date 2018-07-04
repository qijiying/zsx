package com.zsx.dao.group;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zsx.model.pojo.GroupMember;
import com.zsx.model.webdto.GroupMemberDto;

public interface GroupMemberMapper extends BaseMapper<GroupMember> {

	List<GroupMemberDto> getMemberByGroupId(@Param("groupId")Long groupId);

}
