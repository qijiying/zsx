package com.zsx.dao.file;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zsx.model.pojo.FileManage;

/**
  * 
  * @ClassName: FileManageMapper 
  * @Description: 文件管理dao 
  * @author xiayy 
  * @date 2018年1月26日 下午4:57:22 
  *
 */
public interface FileManageMapper extends BaseMapper<FileManage> {

	public List<FileManage> getUserManage(@Param("userId")Long userId,@Param("s")Integer s,@Param("p")Integer p);
}