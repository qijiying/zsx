package com.zsx.fwmp.web.service.advert.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.zsx.dao.advert.AdvertMapper;
import com.zsx.fwmp.web.others.base.ServerBase;
import com.zsx.fwmp.web.service.advert.IAdvertService;
import com.zsx.model.pojo.Advert;
import com.zsx.model.webdto.AdvertDto;
import com.zsx.model.webdto.FileDto;
import com.zsx.model.webdto.FileManageDto;

/**
 * @Title IAdvertServiceImpl
 * @author lz
 * @description 广告实现类
 * @date 2018年5月31日17:08:42
 */
@Service
public class IAdvertServiceImpl extends ServiceImpl<AdvertMapper,Advert> implements IAdvertService {

	@Autowired
	AdvertMapper advertMapper;
	

	@Override
	public Page<Advert> selectAdByAreadAndPostionPage(Advert advert, Page<Advert> page) {
		return advertMapper.selectAdByAreadAndPostionPage(advert, page);
	}


	/**
	 * @Title selectAaByPage
	 * @see com.zsx.fwmp.service.advert.IAdvertService#selectAaByPage
	 * @description 广告页面初始化列表
	 */
	@Override
	public List<Advert> selectAaByPage(Advert advert, Page<Advert> page) {
		return advertMapper.selectAaByPage(advert,page);
	}

	/**
	 * @Title selectAdvertByAreaAndStatusAndPage
	 * @see com.zsx.fwmp.service.advert.IAdvertService#selectAdvertByAreaAndStatusAndPage
	 * @description 搜索广告
	 */
	@Override
	public Page<AdvertDto> selectAdvertByAreaAndStatusAndPage(Advert advert,Integer current,Integer size) {
		List<AdvertDto> list = advertMapper.selectAdvertByAreaAndStatusAndPage(advert,current,size);
		Page<AdvertDto> page=new Page<>(current,size);
 		int count=advertMapper.getSearchAdvertPageOfCount(advert);
		if(count==0){
			return page;
		}
		updateFileColumn(list);
		page.setRecords(list);
		page.setTotal(count);
		return page;
	}
	
	
	/**
	 * @Title updateFileColumn
	 * @param list
	 * @description 修改图片地址
	 */
	public void updateFileColumn(List<AdvertDto> list){
		list.forEach(item->{
			List<FileManageDto> fList = item.getFileManageList();
			fList.forEach(itemF->{
					//将访问图片的地址设置到filename里
					//items.setFileName(new ServerBase().getServerPort()+itemF.getFilePath()+"/"+items.getFileName());
					//先根据文件样式排序，再根据fileGroupIds排序，此处排序顺序不能更改
					Collections.sort(itemF.getFileList(),(FileDto file1,FileDto file2)->file1.getFileStyle().compareTo(file2.getFileStyle()));
					Collections.sort(itemF.getFileList(),(FileDto file1,FileDto file2)->file1.getFileGroupIds().compareTo(file2.getFileGroupIds()));
					
					String groupId = "";				
					for (int i = 0; i < itemF.getFileList().size(); i++) {
						//防止数据出错，groupId为空则直接跳过
						if(null==itemF.getFileList().get(i).getFileGroupIds()){
							continue;
						}
						//相同的fileGroupId只保留第一个，其它的remove
						String str = itemF.getFileList().get(i).getFileGroupIds().toString();
						if(groupId.equals(str)){
							itemF.getFileList().remove(i);
							//list操作remove后长度减一
							i--;
						}
						else{
							new ServerBase();
							itemF.getFileList().get(i).setFileName(itemF.getFileList().get(i).getFileName());
							groupId = itemF.getFileList().get(i).getFileGroupIds().toString();
						}
					}
			});
		});
	}
	

	/**
	 * @Title disAdvertById
	 * @see com.zsx.fwmp.service.advert.IAdvertService#disAdvertById(long)
	 * @description 禁用广告
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public Object disAdvertById(long id) {
		Advert advert = advertMapper.selectById(id);
		int flag = (advert.getLockStatus()==1?2:1);
		return advertMapper.disAdvertById(id,flag)>0?true:false;
	}

	@Override
	public AdvertDto get(Long id) {
		AdvertDto advert = advertMapper.getAdvertDto(id);
		// 将相同组ID的文件进行归类
		/*if (null != advert) {
			if (null != advert.getFileManageList()
					&& advert.getFileManageList().size() > 0) {
				Map<Long, List<FileDto>> groupBy = FileUtil.fileGroup(
						advert.getFileManageList().get(0).getFileList());
				if (groupBy != null) {
					advert.getFileManageList().get(0).setFileList(null);
					advert.getFileManageList().get(0).setFile_map(groupBy);
				}
			}

		}*/
		return advert;
	}

	/** 
	 * @Title selectAdByPage
	 * @see com.zsx.fwmp.service.advert.IAdvertService#selectAdByPage
	 * @description 广告列表带相关图片
	 */
	@Override
	public Page<AdvertDto> selectAdByPage(Integer current,Integer size) {
		List<AdvertDto> list = advertMapper.selectAdByPage(current,size);
		Page<AdvertDto> page=new Page<>(current,size);
 		int count=advertMapper.getAdvertPageOfCount();
		if(count==0){
			return page;
		}
		updateFileColumn(list);
		page.setRecords(list);
		page.setTotal(count);
		return page;
	}


	/**
	 * @Title deleteAdverts
	 * @description 批量删除广告
	 * @see com.zsx.fwmp.web.service.advert.IAdvertService#deleteAdverts(java.lang.Long[])
	 */
	@Override
	public Object deleteAdverts(Long[] ids) {
		List<Long> list = Arrays.asList(ids);
		Integer count = advertMapper.deleteBatchIds(list);
		Map<String,Object> map = Maps.newHashMap();
		if(count>0){
			map.put("code", 1);
		}else{
			map.put("code", 0);
		}
		map.put("result", count);
		return map;
	}

}
