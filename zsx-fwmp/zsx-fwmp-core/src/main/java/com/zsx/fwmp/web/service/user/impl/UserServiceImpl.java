package com.zsx.fwmp.web.service.user.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.zsx.dao.feedback.UserCountFeedbackMapper;
import com.zsx.dao.feedback.UserTxtFeedbackMapper;
import com.zsx.dao.file.FileManageMapper;
import com.zsx.dao.group.GroupMemberMapper;
import com.zsx.dao.group.GroupMuteMapper;
import com.zsx.dao.message.MessageMapper;
import com.zsx.dao.news.NewsCommentMapper;
import com.zsx.dao.news.NewsReplyMapper;
import com.zsx.dao.post.PostCommentMapper;
import com.zsx.dao.post.PostMapper;
import com.zsx.dao.post.PostReplyMapper;
import com.zsx.dao.user.UserCardMapper;
import com.zsx.dao.user.UserCollectionMapper;
import com.zsx.dao.user.UserFriendMapper;
import com.zsx.dao.user.UserMapper;
import com.zsx.dao.user.UserThumbUpMapper;
import com.zsx.dao.user.UserTransmitMapper;
import com.zsx.framework.base.BaseAppClass;
import com.zsx.framework.exception.SystemException;
import com.zsx.framework.exception.enmus.ResultEnum;
import com.zsx.fwmp.web.others.base.ConstantClass;
import com.zsx.fwmp.web.others.base.Log;
import com.zsx.fwmp.web.others.base.ServerBase;
import com.zsx.fwmp.web.others.util.Assert;
import com.zsx.fwmp.web.others.util.UserUtil;
import com.zsx.fwmp.web.service.user.IUserService;
import com.zsx.model.pojo.FileManage;
import com.zsx.model.pojo.GroupMember;
import com.zsx.model.pojo.GroupMute;
import com.zsx.model.pojo.Message;
import com.zsx.model.pojo.NewsComment;
import com.zsx.model.pojo.NewsReply;
import com.zsx.model.pojo.Post;
import com.zsx.model.pojo.PostComment;
import com.zsx.model.pojo.PostReply;
import com.zsx.model.pojo.User;
import com.zsx.model.pojo.UserCard;
import com.zsx.model.pojo.UserCollection;
import com.zsx.model.pojo.UserCountFeedback;
import com.zsx.model.pojo.UserFriend;
import com.zsx.model.pojo.UserThumbUp;
import com.zsx.model.pojo.UserTransmit;
import com.zsx.model.pojo.UserTxtFeedback;
import com.zsx.utils.id.IdGen;

/**
 * @author lz
 * @description 用户服务实现类
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements IUserService {

	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private UserCountFeedbackMapper userCountFeedbackMapper;
	
	@Autowired
	private UserFriendMapper userFriendMapper;
	
	@Autowired
	private UserCardMapper userCardMapper;
	
	@Autowired
	private UserCollectionMapper userCollectionMapper;
	
	@Autowired
	private UserThumbUpMapper userThumUpMapper;
	
	@Autowired
	private UserTransmitMapper userTransmitMapper;
	
	@Autowired
	private UserTxtFeedbackMapper userTxtFeedbackMapper;
	
	@Autowired
	private GroupMemberMapper groupMemberMapper;
	
	@Autowired
	private GroupMuteMapper groupMuteMapper;
	
	@Autowired
	private MessageMapper messageMapper;
	
	@Autowired
	private NewsCommentMapper newsCommentMapper;
	
	@Autowired
	private NewsReplyMapper newsReplyMapper;
	
	@Autowired
	private PostMapper postMapper;
	
	@Autowired
	private PostReplyMapper postReplyMapper;
	
	@Autowired
    private PostCommentMapper postCommentMapper;
	
	@Autowired
	private FileManageMapper fileMapper;
	

	/** 
	 * @Title insertUser
	 * @see com.zsx.fwmp.service.user.IUserService#insertUser
	 * @description 添加用户
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public Object insertUser(User user) {
		User exisUser = selectUserByLoginName(user.getLoginUsername());
		Assert.isNotNull(exisUser,ResultEnum.SERVER_USER_EXISTS);
		String uuid = IdGen.uuid();
		user.setSalt(uuid);
		user.setLoginPassword(UserUtil.Md5Password(user.getLoginPassword(), uuid));
		//初始化用户id
		user.setId(BaseAppClass.giveRandomId());
		user.setTinkleId(BaseAppClass.giveTinkleId());
		return userMapper.insert(user);
	}

	/**
	 * @Title selectUserByLoginName
	 * @param loginUsername
	 * @description 根据注册的用户名查找用户
	 * @return
	 */
	private User selectUserByLoginName(String loginUsername) {
		User user = userMapper.selectOne(new User(loginUsername));
		//将访问图片的地址设置到filename里
		user.setHeadPortrait((new ServerBase().getServerPort()+user.getHeadPortrait()));
		user.setBgPortrait((new ServerBase().getServerPort()+user.getBgPortrait()));
		return user;
	}

	/**
	 * @Title
	 * @see com.zsx.fwmp.service.user.IUserService#deleteUserById
	 * @description 删除用户
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public Object deleteUserById(Long id) {
		// TODO 预留删除用户所有信息
		return userMapper.deleteById(id);
	}
	
	
	/**
	 * @Title selectUserByUserAreaTimeAndPage
	 * @see com.zsx.fwmp.web.service.user.IUserService#selectUserByUserAreaTimeAndPage
	 * @description 搜索用户
	 */
	@Override
	public Object selectUserByUserAreaTimeAndPage(String name, Integer areaCode,String source, Date startTime, Date endTime,
			Page<User> page) {
		Integer flag = userSource(source);
		List<User> list = userMapper.selectUserByUserAreaTimeAndPage(name,areaCode,flag,startTime,endTime,page);
		updateFileColumn(list);
		page.setRecords(list);
		int total = userMapper.selectTotalUserByUserAreaTimeAndPage(name,areaCode,flag,startTime,endTime,page);
		page.setTotal(total);
		return page;
	}

	/**
	 * @Title selectUser
	 * @see com.zsx.fwmp.service.user.IUserService#selectUser
	 * @description 查找用户(只根据用户来源)
	 */
	@Override
	public Page<User> selectUser(Map<String,Object> map) {
		Integer current=(Integer) map.get("current");
		Integer size=(Integer) map.get("size");
		String source = (String) map.get("source");
		Page<User> page=new Page<>(current,size);
		if(null==source){
			//查找用户总数量
			EntityWrapper<User> wrapper = new EntityWrapper<User>();
	 		int count=userMapper.selectCount(wrapper);
			if(count==0){
				return page;
			}
			List<User> list = userMapper.selectUserByPage(page);
			updateFileColumn(list);
			page.setRecords(list);
			page.setTotal(count);
		}else{
			Integer flag = 1;
            flag = userSource(source);
			page = getUserListBySource(flag,map);
		}
		return page;
	}
	
	
	/**
	 * @Title userSource
	 * @param source
	 * @description 用户来源
	 * @return
	 */
	private Integer userSource(String source) {
		
		if(null==source){
			return ConstantClass.USER_SOURCE_WEB;
		}
		
		//根据用户来源查找用户
		Integer flag = 1;
		switch(source){
		    case "web":
		    	flag = ConstantClass.USER_SOURCE_WEB;break;
		    case "android":
		    	flag = ConstantClass.USER_SOURCE_ANDROID;break;
		    case "ios":
		    	flag = ConstantClass.USER_SOURCE_IOS;break;
		    case "app":
		    	flag = ConstantClass.USER_SOURCE_APP;break;
		    default :
		    	flag = ConstantClass.USER_SOURCE_WEB;break;
		}
		return flag;
	}

	/**
	 * @Title getUserListBySource
	 * @param flag
	 * @description 根据用户来源查找用户
	 * @return
	 */
	private Page<User> getUserListBySource(Integer flag,Map<String,Object> map) {
		Integer current=(Integer) map.get("current");
		Integer size=(Integer) map.get("size");
		String limit = " limit "+current+","+size;
		Page<User> page=new Page<>();
		String sql = " app_soucre="+flag;
		//app用户标志  -3
		if(flag==-3) sql = " app_soucre="+ConstantClass.USER_SOURCE_IOS+" or app_soucre="+ConstantClass.USER_SOURCE_ANDROID;
		List<User> list = userMapper.selectList(new EntityWrapper<User>().where(sql).last(limit));
		int count=userMapper.selectCount(new EntityWrapper<User>().where(sql));
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
	public void updateFileColumn(List<User> list){
		list.forEach(item->{
					//将访问图片的地址设置到filename里
					item.setHeadPortrait((new ServerBase().getServerPort()+item.getHeadPortrait()));
					item.setBgPortrait((new ServerBase().getServerPort()+item.getBgPortrait()));
			});
	}

	/**
	 * @Title deleteByUserId
	 * @see com.zsx.fwmp.web.service.user.IUserService#deleteByUserId
	 * @description 删除用户
	 */
	@SuppressWarnings("null")
	@Override
	@Transactional
	public Object deleteByUserId(Long[] ids) {
		Map<String,Object> map = Maps.newHashMap();
		try {
		    Arrays.asList(ids).forEach(id->{
				map.put(ConstantClass.WHETHER_SUCCESS, deleteById(id));
				if((boolean) map.get(ConstantClass.WHETHER_SUCCESS)){
					//todo 删除用户相关信息，待优化
					//newsCommentMapper.delete(new EntityWrapper<NewsComment>().where("news_id={0}",item));
					//newsReplyMapper.delete(new EntityWrapper<NewsReply>().where("new_id={0}",item));
					userCardMapper.delete(new EntityWrapper<UserCard>().where("user_id={0}", id));
					userCollectionMapper.delete(new EntityWrapper<UserCollection>().where("user_id={0}", id));
					userFriendMapper.delete(new EntityWrapper<UserFriend>().where("friend_id={0}", id).or("user_id={0}", id));
					userCountFeedbackMapper.delete(new EntityWrapper<UserCountFeedback>().where("user_id={0}", id));
					userThumUpMapper.delete(new EntityWrapper<UserThumbUp>().where("user_id={0}", id));
					userTransmitMapper.delete(new EntityWrapper<UserTransmit>().where("user_id={0}", id));
					userTxtFeedbackMapper.delete(new EntityWrapper<UserTxtFeedback>().where("user_id={0}", id));
					groupMemberMapper.delete(new EntityWrapper<GroupMember>().where("user_id={0}", id));
					groupMuteMapper.delete(new EntityWrapper<GroupMute>().where("user_id", id));
					messageMapper.delete(new EntityWrapper<Message>().where("send_user_id={0} or receive_user_id={0}", id));
					newsCommentMapper.delete(new EntityWrapper<NewsComment>().where("comment_user_id={0}", id));
					newsReplyMapper.delete(new EntityWrapper<NewsReply>().where("comment_user_id={0} or reply_user_id={0}", id));
					postMapper.delete(new EntityWrapper<Post>().where("user_id={0}", id));
					postReplyMapper.delete(new EntityWrapper<PostReply>().where("comment_user_id={0} or reply_user_id={0}", id));
					postCommentMapper.delete(new EntityWrapper<PostComment>().where("comment_user_id={0}", id));
					fileMapper.delete(new EntityWrapper<FileManage>().where("file_source = 1 and source_id={0}",id));
				}
		    });
		} catch (Exception e) {
                map.remove(ConstantClass.WHETHER_SUCCESS);
                map.put("code", 0);
                //手动抛出一个异常
                Log.debug("----------------手动抛出异常，使事务回滚----------------------",UserServiceImpl.class);
                int[] a =null;
                a[1] = 999;
 		}
		return map;
	}

	/**
	 * @Title checkUserLogin
	 * @see com.zsx.fwmp.web.service.user.IUserService#checkUserLogin
	 * @description web后台登录
	 */
	@Override
	public Object checkUserLogin(User user,HttpServletRequest request) {
		Map<String,Object> map = Maps.newHashMap();
		map.put("login_username", user.getLoginUsername());
		map.put("app_soucre", ConstantClass.USER_SOURCE_WEB);
		List<User> checkUser = userMapper.selectByMap(map);
		if(checkUser.size()>0) 
			//查到的密码经过了加盐
			if(checkUser.get(0).getLoginPassword().equals(UserUtil.Md5Password(user.getLoginPassword(),checkUser.get(0).getSalt())))
			{
				request.getSession().setAttribute("user", user);
				//设置session超时时间
				request.getSession().setMaxInactiveInterval(ConstantClass.MAXINACTIVE_SESSION);
				Log.debug(user.getNickName()+"------登录成功",UserServiceImpl.class);
				return user;
		    }
			else{return ResultEnum.SERVER_USER_ERROR_USERNAME_PASSWORD;}
		else return ResultEnum.SERVER_USER_ERROR_USERNAME_PASSWORD;
	}

	/**
	 * @Title searchUserFriend
	 * @see com.zsx.fwmp.web.service.user.IUserService#searchUserFriend
	 * @description 根据用户id查找friend
	 */
	@Override
	public Page<User> searchUserFriend(Long userId,Map<String,Object> map) {
		Integer current = (Integer)map.get("current");
		Integer size = (Integer)map.get("size");
		Page<User> page = new Page<>(current,size);
		List<User> user = userFriendMapper.getHailFellow(userId,page);
		page.setRecords(user);
		return page;
	}

	/**
	 * @Title quitLogin
	 * @see com.zsx.fwmp.web.service.user.IUserService#quitLogin
	 * @description 退出登录
	 */
	@Override
	public Object quitLogin(Long userId,HttpServletRequest request) {
		if(null!=request.getSession().getAttribute("user"))
			request.getSession().removeAttribute("user");
		return true;
	}

	/**
	 * @Title insertUserByEntity
	 * @see com.zsx.fwmp.web.service.user.IUserService#insertUserByEntity
	 * @description 添加用户
	 */
	@Override
	public Object insertUserByEntity(User user) {
		Log.debug("-------密码加盐--------", UserServiceImpl.class);
		user.setSalt(UserUtil.Md5Password(user.getLoginPassword(), IdGen.uuid()));
		Log.debug("-------获得蹡蹡ID------", UserServiceImpl.class);
		user.setTinkleId(BaseAppClass.giveTinkleId());
		boolean flag = insert(user);
		if(flag){
			Log.debug("新建用户成功", UserServiceImpl.class);
		}
		return flag;
	}

	/**
	 * @Title dataAllUserNameAndId
	 * @see com.zsx.fwmp.web.service.user.IUserService#dataAllUserNameAndId()
	 * @description 查找所有用户
	 */
	@Override
	public Object dataAllUserNameAndId() {
		return selectList(new EntityWrapper<User>());
	}
	
	

	@Override
	public User getCacheUser(Long id) {
		User user=Optional.ofNullable(selectById(id)).orElseThrow(()->new SystemException(ResultEnum.CACHE_BASE_DATA_ERROR));
		System.out.println("=================begin==========================");
		System.out.println(user);
			Log.debug("获取缓存的用户ID:{"+id+"}，用户信息：{"+user.toString()+"}", UserServiceImpl.class);
		return user;
	}
	
}
