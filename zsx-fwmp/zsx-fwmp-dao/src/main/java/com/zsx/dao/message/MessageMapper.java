package com.zsx.dao.message;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.zsx.model.dto.MessageDto;
import com.zsx.model.pojo.Message;

public interface MessageMapper extends BaseMapper<Message> {

	List<MessageDto> getMessagePage(@Param("userId")Long userId,@Param("messageType")Integer messageType,Pagination page);

}
