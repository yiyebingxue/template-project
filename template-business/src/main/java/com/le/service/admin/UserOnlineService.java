package com.le.service.admin;

import com.le.common.model.result.ResultPageModel;
import com.le.model.bo.user.UserOnlineListPageBO;
import com.le.model.vo.user.UserOnlinePageVO;
import com.le.model.vo.user.UserOnlineSysVO;

import java.util.List;

/**
 * 在线用户接口
 *
 * @author Bruce Lu
 */
public interface UserOnlineService {

	/**
	 * 在线用户分页列表
	 *
	 * @param pageBO
	 * @return 用户
	 */
	ResultPageModel<UserOnlinePageVO> listPageUser(UserOnlineListPageBO pageBO);

	/**
	 * 查看用户已登录的子系统
	 *
	 * @param userId
	 * @return 已登录系统列表
	 */
	List<UserOnlineSysVO> listOnlineSys(Long userId);


}
