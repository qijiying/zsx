package com.zsx.fwmp.web.service.post.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zsx.dao.post.PostCommentMapper;
import com.zsx.dao.post.PostMapper;
import com.zsx.dao.post.PostReplyMapper;
import com.zsx.framework.base.BaseAppClass;
import com.zsx.fwmp.web.others.base.ConstantClass;
import com.zsx.fwmp.web.others.base.Log;
import com.zsx.fwmp.web.others.base.ServerBase;
import com.zsx.fwmp.web.service.post.IPostService;
import com.zsx.model.dto.FileDto;
import com.zsx.model.dto.FileManageDto;
import com.zsx.model.dto.PostDto;
import com.zsx.model.pojo.Post;
import com.zsx.model.pojo.PostComment;
import com.zsx.model.pojo.PostReply;

/**
 * @author lz
 * @description 帖子业务实现类
 */
@Service
public class PostServiceImpl extends ServiceImpl<PostMapper,Post> implements IPostService {

	@Autowired
	private PostMapper postMapper;
	
	@Autowired
	private PostReplyMapper postReplyMapper;
	
	@Autowired
	private PostCommentMapper postCommentMapper;
	 
	
	/**
	 * @Title selectPostByKewordAndPage
	 * @see com.zsx.fwmp.service.post.IPostService#selectPostByKewordAndPage
	 * @description 搜索帖子列表
	 */
	@Override
	public Object selectPostByKewordAndPage(Long id, String key, String groupName, Integer status, Date startTime,
			Date endTime, Long userId, Map<String,Object> map) {
		Integer current=(Integer) map.get("current");
		Integer size=(Integer) map.get("size");
		Page<PostDto> page=new Page<>(current,size);
 		int count=postMapper.getSearchPostPageOfCount(id,key,groupName,status,startTime,endTime,userId);
		if(count==0){
			return page;
		}
		List<PostDto> list = postMapper.selectPostByKewordAndPage(id,key,groupName,status,startTime,endTime,userId,current,size);
		System.out.println(list);
		updateFileColumn(list,ConstantClass.ONE_FLAG);
		page.setRecords(list);
		page.setTotal(count);
		return page;
	}
	
	/**
	 * @Title updateFileColumn
	 * @param list
	 * @description 修改图片地址
	 */
	public void updateFileColumn(List<PostDto> list,Integer flag){
		list.forEach(item->{
			//帖子用户头像
			item.setUserheadPortrait(ServerBase.getServer()+item.getUserheadPortrait());
			List<FileManageDto> fList = item.getFileManageList();
			//处理帖子图片，根据groupIds分组返回一张图
			fList.forEach(itemF->{
				List<FileDto> fileDtoList = itemF.getFileList();
/*				itemF.getFileList().forEach(items->{
					//将访问图片的地址设置到filename里
					items.setFileName(new ServerBase().getServerPort()+itemF.getFilePath()+"/"+items.getFileName());
				});*/
				//先根据文件样式排序，再根据fileGroupIds排序，此处排序顺序不能更改
				Collections.sort(fileDtoList,(FileDto file1,FileDto file2)->file1.getFileStyle().compareTo(file2.getFileStyle()));
				Collections.sort(fileDtoList,(FileDto file1,FileDto file2)->file1.getFileGroupIds().compareTo(file2.getFileGroupIds()));
				String groupId = "";				
				for (int i = 0; i < itemF.getFileList().size(); i++) {
					//相同的fileGroupId只保留第一个，其它的remove
					String str = fileDtoList.get(i).getFileGroupIds().toString();
					if(groupId.equals(str)&&flag==ConstantClass.ONE_FLAG){
						fileDtoList.remove(i);
						//list操作remove后长度减一
						i--;
					}else{
						//如果是视频，则返回fileVideoImage字段
						if(fileDtoList.get(i).getFileType()==2){
							fileDtoList.get(i).setFileVideoImage(fileDtoList.get(i).getFileVideoImage());
						}
						//因为当i=0时，i不能--，所以额外处理i等于1的数据
						if(i==0&&flag==ConstantClass.TWO_FLAG&&itemF.getFileList().size()>1&&itemF.getFileList().get(1).getFileStyle()==2)
							fileDtoList.get(1).setFileName(fileDtoList.get(1).getFileName());
						   
						if(flag==ConstantClass.TWO_FLAG&&fileDtoList.size()>1&&fileDtoList.get(i).getFileStyle()!=2){
							fileDtoList.remove(i);
							//list操作remove后长度减一,如果i=0，则减一会出异常，需额外处理
							if(i!=0)i--;
						}else fileDtoList.get(i).setFileName(fileDtoList.get(i).getFileName());
						
						groupId = fileDtoList.get(i).getFileGroupIds().toString();
					}
				}
			});
		});
	}

	/** (non-Javadoc)
	 * @see com.zsx.fwmp.service.post.IPostService#getList(java.util.Map)
	 * @description 获得帖子列表
	 */
	@Override
	public Page<PostDto> getList(Map<String, Object> map) {
		Integer current=(Integer) map.get("current");
		Integer size=(Integer) map.get("size");
		Page<PostDto> page=new Page<>(current,size);
 		int count=postMapper.getWebPostPageOfCount();
		if(count==0){
			return page;
		}
		List<PostDto> list = postMapper.getWebPostPage(current,size);
		 
		updateFileColumn(list,ConstantClass.ONE_FLAG);
		page.setRecords(list);
		page.setTotal(count);
		
		//todo 对文件id切分时会出现bad string异常
		
		/*		List<PostDto> postDtos=page.getRecords();
		
		if(postDtos !=null && postDtos.size() >0){
			postDtos.stream().forEach((f)->{
				if(f.getFileManageList()!=null && f.getFileManageList().size() >0){
					f.getFileManageList().stream().forEach((n)->{
						if(n.getFileList() !=null && n.getFileList().size() > 0){
							//n.getFileList().sort((f1,f2)->f1.getSort() > f2.getSort() ? 1 :-1);
							Map<Long, List<FileDto>> groupBy=FileUtil.fileGroup(n.getFileList());
							if(groupBy !=null){
								n.setFileList(null);
								n.setFile_map(groupBy);
								//f.setFileManageList(f.getFileManageList().stream().sorted(Ripper::comparator).collect(Collectors.toList()));
								//System.out.println(JSONObject.toJSONString(f.getFileManageList()));
							}
						}
					});
				}
			});
		}*/
		return page;
	}

	/**
	 * @Title insertPost
	 * @see com.zsx.fwmp.web.service.post.IPostService#insertPost
	 * @description 添加post
	 */
	@Override
	public Object insertPost(Post post) {
		try {
			if(null==post.getId()) post.setId(BaseAppClass.giveRandomId());
		} catch (Exception e) {
			post.setId(BaseAppClass.giveRandomId());
		}
		return postMapper.insert(post);
	}

	
	/**
	 * @Title searchPostFromUser
	 * @see com.zsx.fwmp.web.service.post.IPostService#searchPostFromUser
	 * @description 根据用户查找帖子
	 */
	@Override
	public Page<PostDto> searchPostFromUser(Map<String, Object> map) {
	    Integer current =(Integer) map.get("current");
	    Integer size =(Integer) map.get("size");
	    Long userId =(Long) map.get("userId");
	    Page<PostDto> page = new Page<>(current,size);
	    //PostDto列表
		List<PostDto> list = postMapper.searchPostFromUser(userId,current,size);
		updateFileColumn(list,ConstantClass.TWO_FLAG);
		page.setRecords(list);
		//total
		int total = postMapper.searchTotalPostFromUser(userId,current,size);
		page.setTotal(total);
		return page;
	}

	/**
	 * @Title deletePostById
	 * @see com.zsx.fwmp.web.service.post.IPostService#deletePostById
	 * @description 批量删除帖子业务实现
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public Object deletePostById(Long[] ids) {
		List<Long> list = Arrays.asList(ids);
		//删除帖子
		boolean flag = deleteBatchIds(list);
		//拼接sql
		String sql = " post_id in ("+list.get(0);
		for (int i = 1; i < list.size()-2; i++) {
			sql += (","+list.get(i));
		}
		if(list.size()>1) sql += (","+list.get(list.size()-1)+")");
		else sql += ")";
		Log.debug("需要执行的sql："+sql, PostServiceImpl.class);
		//删除评论的回复
		postReplyMapper.delete(new EntityWrapper<PostReply>().where(sql));
		//删除评论
		postCommentMapper.delete(new EntityWrapper<PostComment>().where(sql));
		return flag;
	}
}
