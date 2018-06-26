package com.zsx.dao.group;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.zsx.model.dto.GroupDto;
import com.zsx.model.pojo.Group;

public interface GroupMapper extends BaseMapper<Group> {

	/**
	  * 
	  * @Title: getGroupDeatil 
	  * @Description: 获取单个群组详情以及成员信息
	  * @param @param groupId
	  * @param @return    设定文件 
	  * @return List<GroupDto>    返回类型 
	  * @throws
	 */
	GroupDto getGroupDeatil(@Param("groupId")Long groupId,@Param("hxId")String hxId);
	
	/**
	 * 
	  * @Title: getUserGroup 
	  * @Description: 获取用户所在的组
	  * @param @param userId
	  * @param @return    设定文件 
	  * @return List<GroupDto>    返回类型 
	  * @throws
	 */
	List<GroupDto> getUserGroup(@Param("userId")Long userId);

	/**
	  * 
	  * @Title: serchGroup 
	  * @Description: 群高级搜索
	  * @param @param id
	  * @param @param code
	  * @param @return    设定文件 
	  * @return List<Group>    返回类型 
	  * @throws
	 */
	LinkedList<Group> serchGroup(Map<String, Object> map,Pagination page);
	
}
