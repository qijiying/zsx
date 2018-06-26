package com.zsx.fwmp.web.service.group;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.zsx.model.dto.GroupDto;
import com.zsx.model.pojo.Group;

/**
 * @ClassName IGroupService
 * @author lz
 * @description 群组服务层接口
 * @date 2018年6月25日10:21:26
 */
public interface IGroupService extends IService<Group> {

	Page<Group> getGroupList(Page<Group> page);

	Object createGroup(GroupDto groupDto);

	Object getGroupDeatil(Long id, Long userId);

	Object getGroupByHxId(String string, Long userId);

	Object editGroup(Group group);

	Object delGroup(GroupDto groupDto);

	Object assignmentGroup(GroupDto groupDto);

	/**
	 * @Title
	 * @param parameter
	 * @param code
	 * @param page
	 * @param type
	 * @description 群组高级搜索
	 * @return
	 */
	Page<Group> serchGroup(String parameter, Integer code, Page<Group> page, Integer type);

}
