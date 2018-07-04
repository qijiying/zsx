package com.zsx.fwmp.web.service.user;

import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.zsx.model.webdto.UserThumbUpDto;

public interface IUserThumbUpService {

	Page<UserThumbUpDto> getThumbUpByUser(Map<String, Object> map);

}
