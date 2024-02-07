/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.le.service.admin;

import com.le.common.model.result.ResultPageModel;
import com.le.model.bo.login.LoginLogListPageBO;
import com.le.model.vo.login.LoginLogPageVO;

/**
 * 登录日志接口
 *
 * @author Bruce Lu
 */
public interface LoginLogService {

	/**
	 * 登录日志分页列表
	 *
	 * @param pageBO
	 * @return 登录日志列表
	 */
	ResultPageModel<LoginLogPageVO> listPageUser(LoginLogListPageBO pageBO);

}
