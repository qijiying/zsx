package com.zsx.fwmp.web.service.user.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.plugins.Page;
import com.zsx.dao.user.UserCollectionMapper;
import com.zsx.framework.exception.SystemException;
import com.zsx.framework.exception.enmus.ResultEnum;
import com.zsx.fwmp.web.others.enums.UserCollectionEnum;
import com.zsx.fwmp.web.others.util.FileUtil;
import com.zsx.fwmp.web.service.user.IUserCollectionService;
import com.zsx.model.webdto.FileDto;
import com.zsx.model.webdto.UserCollectionDto;

@Service
public class UserCollectionServiceImpl implements IUserCollectionService {

	@Autowired
	private UserCollectionMapper userCollectionMapper;
	
	/**
	  * (非 Javadoc) 
	  * <p>Title: getUserCollection</p> 
	  * <p>Description: 获取用户收藏列表</p> 
	  * @param type
	  * @param userId
	  * @param page
	  * @return 
	  * @see com.zsx.service.user.IUserCollectionService#getUserCollection(java.lang.Integer, java.lang.Long, com.baomidou.mybatisplus.plugins.Page)
	 */
	@Override
	public Page<UserCollectionDto> getUserCollection(Integer type, Long userId,
			Page<UserCollectionDto> page) {
		page.setRecords(userCollectionMapper.getUserCollectionList(type, userId, page.getCurrent(),page.getSize()));
		if(type ==UserCollectionEnum.POST.getKey()){
			if(null != page.getRecords() && page.getRecords().size() >0){
				 for (int i=0;i<page.getRecords().size();i++) {
					 if(null ==page.getRecords().get(i)){
						 throw new SystemException(ResultEnum.SERVER_DATA_ERROR);
					 }
					if(null !=page.getRecords().get(i).getFileManageList() && page.getRecords().get(i).getFileManageList().size() >0){
						if(null !=page.getRecords().get(i).getFileManageList().get(0).getFileList()){
							Map<Long, List<FileDto>> groupBy=FileUtil.fileGroup(page.getRecords().get(i).getFileManageList().get(0).getFileList());
							if(groupBy !=null){
								page.getRecords().get(i).getFileManageList().get(0).setFileList(null);
								page.getRecords().get(i).getFileManageList().get(0).setFile_map(groupBy);
							}
						 }
					}
				}
			 }
		}
		return page;
	}
	
}
