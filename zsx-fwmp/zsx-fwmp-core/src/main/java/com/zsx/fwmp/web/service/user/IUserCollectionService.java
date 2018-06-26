package com.zsx.fwmp.web.service.user;

import com.baomidou.mybatisplus.plugins.Page;
import com.zsx.model.dto.UserCollectionDto;

public interface IUserCollectionService {

	Page<UserCollectionDto> getUserCollection(Integer type, Long userId, Page<UserCollectionDto> page);

}
