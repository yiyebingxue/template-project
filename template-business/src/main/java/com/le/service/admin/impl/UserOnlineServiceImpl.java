package com.le.service.admin.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.le.common.constant.CacheConstants;
import com.le.common.model.login.LoginResultVO;
import com.le.common.model.result.ResultPageModel;
import com.le.common.utils.ObjectUtils;
import com.le.common.utils.StringUtils;
import com.le.common.utils.bean.BeanCopierUtil;
import com.le.dao.dto.SsoOnlineUserDTO;
import com.le.dao.entity.SsoSystem;
import com.le.dao.mapper.SsoOnlineUserMapper;
import com.le.dao.mapper.SsoSystemMapper;
import com.le.dao.query.UserOnlineQuery;
import com.le.model.bo.user.UserOnlineListPageBO;
import com.le.model.vo.getway.SysLoginCacheVO;
import com.le.model.vo.user.UserOnlinePageVO;
import com.le.model.vo.user.UserOnlineSysVO;
import com.le.service.admin.UserOnlineService;
import com.le.service.base.SysConfigService;
import com.le.framework.redis.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 登录日志接口
 *
 * @author Bruce Lu
 */
@Slf4j
@Service
public class UserOnlineServiceImpl implements UserOnlineService {

	@Autowired
	private RedisCache redisCache;
	@Resource
	private SsoSystemMapper ssoSystemMapper;
	@Resource
	private SysConfigService sysConfigService;
	@Resource
	private SsoOnlineUserMapper ssoOnlineUserMapper;

	/**
	 * 在线用户分页列表
	 *
	 * @param pageBO
	 * @return 用户
	 */
	@Override
	public ResultPageModel<UserOnlinePageVO> listPageUser(UserOnlineListPageBO pageBO) {
		//构建参数
		UserOnlineQuery query = this.buildPageQuery(pageBO);

		//统计行数
		int total = ssoOnlineUserMapper.countByCondition(query);

		if (total <= 0) {
			return ResultPageModel.defaultValue();
		}

		//分页查询
		List<SsoOnlineUserDTO> onlineUserList = ssoOnlineUserMapper.listPageByCondition(query);
		if (CollectionUtils.isEmpty(onlineUserList)) {
			return ResultPageModel.defaultValue();
		}

		return ResultPageModel.success(this.buildOnlineUserPage(onlineUserList), total);
	}

	/**
	 * 查看用户已登录的子系统
	 *
	 * @param userId
	 * @return 已登录系统列表
	 */
	@Override
	public List<UserOnlineSysVO> listOnlineSys(Long userId) {
		if (null == userId) {
			return new ArrayList<>(0);
		}

		//登录缓存用户信息
		LoginResultVO loginUser =
				redisCache.get(CacheConstants.getLoginUserIdKey(userId), LoginResultVO.class);
		if (ObjectUtils.isNull(loginUser)) {
			return new ArrayList<>(0);
		}

		//登录缓存用户token信息
		Map<String, String> userCacheMap =
				redisCache.hgetAll(CacheConstants.getLoginUserTokenKey(loginUser.getToken()), String.class);
		if (ObjectUtils.isEmpty(userCacheMap)) {
			return new ArrayList<>(0);
		}

		//构建子系统集
		List<String> sysCodeList = new ArrayList<>();
		for (Map.Entry<String, String> userCache : userCacheMap.entrySet()) {
			if (userCache.getKey().equals(loginUser.getToken())) {
				continue;
			}
			sysCodeList.add(userCache.getKey());
		}

		return this.buildUserOnlineSysList(sysCodeList, userCacheMap);
	}

	/**
	 * 构建返回列表
	 *
	 * @param onlineUserList
	 * @return
	 */
	private List<UserOnlinePageVO> buildOnlineUserPage(List<SsoOnlineUserDTO> onlineUserList) {
		UserOnlinePageVO pageItem = null;
		Long supperAdminUserId = sysConfigService.getSupperAdminUserId();
		List<UserOnlinePageVO> resultList = new ArrayList<>();
		for (SsoOnlineUserDTO onlineUser : onlineUserList) {
			pageItem = BeanCopierUtil.copy(onlineUser, UserOnlinePageVO.class);
			pageItem.setSupperAdminFlag(onlineUser.getUserId().equals(supperAdminUserId));
			//登录时间
			if (ObjectUtils.isNotNull(onlineUser.getLoginTime()) && onlineUser.getLoginTime() > 0) {
				pageItem.setLoginTime(DateUtil.formatDateTime(new Date(onlineUser.getLoginTime())));
			}
			//主动退出时间
			if (ObjectUtils.isNotNull(onlineUser.getLoginOutTime()) && onlineUser.getLoginOutTime() > 0) {
				pageItem.setLoginOutTime(DateUtil.formatDateTime(new Date(onlineUser.getLoginOutTime())));
			}
			//失效时间
			if (ObjectUtils.isNotNull(onlineUser.getExpireTime()) && onlineUser.getExpireTime() > 0) {
				pageItem.setExpireTime(DateUtil.formatDateTime(new Date(onlineUser.getExpireTime())));
			}
			resultList.add(pageItem);
		}
		return resultList;
	}

	/**
	 * 构建查询参数
	 *
	 * @param pageBO
	 */
	private UserOnlineQuery buildPageQuery(UserOnlineListPageBO pageBO) {
		UserOnlineQuery query = new UserOnlineQuery()
				//效期大于当前时间
				.setExpireStartTime(System.currentTimeMillis())
				.setUsernameLike(pageBO.getUsernameLike());

		if (StringUtils.isNotBlank(pageBO.getLoginStartTime())) {
			query.setLoginStartTime(DateUtil.beginOfDay(DateUtil.parseDate(pageBO.getLoginStartTime())).getTime());
		}

		if (StringUtils.isNotBlank(pageBO.getLoginEndTime())) {
			query.setLoginEndTime(DateUtil.endOfDay(DateUtil.parseDate(pageBO.getLoginEndTime())).getTime());
		}
		query.setPage(pageBO.getPage());
		query.setPageSize(pageBO.getPageSize());
		return query;
	}

	/**
	 * 构建用户已经登录的子系统列表
	 *
	 * @param sysCodeList
	 * @param userCacheMap
	 */
	private List<UserOnlineSysVO> buildUserOnlineSysList(List<String> sysCodeList, Map<String, String> userCacheMap) {
		if (CollectionUtils.isEmpty(sysCodeList)) {
			return new ArrayList<>(0);
		}

		List<SsoSystem> systemList = ssoSystemMapper.getBySysCodeList(sysCodeList);
		if (CollectionUtils.isEmpty(systemList)) {
			return new ArrayList<>(0);
		}

		UserOnlineSysVO onlineSysVO = null;
		List<UserOnlineSysVO> resultList = new ArrayList<>(systemList.size());
		for (SsoSystem ssoSystem : systemList) {
			onlineSysVO = new UserOnlineSysVO();
			onlineSysVO.setSysCode(ssoSystem.getSysCode());
			onlineSysVO.setSysName(ssoSystem.getSysName());
			onlineSysVO.setLoginTime(this.getLoginSysTime(ssoSystem.getSysCode(), userCacheMap));
			resultList.add(onlineSysVO);
		}
		return resultList;
	}

	/**
	 * 从缓存中解析-登录子系统的时间
	 */
	private String getLoginSysTime(String sysCode, Map<String, String> userCacheMap) {
		String cacheValue = userCacheMap.getOrDefault(sysCode, "{}");
		if (StringUtils.isBlank(cacheValue)) {
			return "";
		}
		SysLoginCacheVO loginCache = JSON.parseObject(cacheValue, SysLoginCacheVO.class);
		if (ObjectUtils.isNull(loginCache) || ObjectUtils.isNull(loginCache.getLoginTime())) {
			return "";
		}
		return DateUtil.formatDateTime(new Date(loginCache.getLoginTime()));
	}
}
