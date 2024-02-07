/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.le.controller.admin;


import com.le.common.model.result.ResultPageModel;
import com.le.model.bo.login.LoginLogListPageBO;
import com.le.model.vo.login.LoginLogPageVO;
import com.le.service.admin.LoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 登录日志管理接口
 *
 * @author Bruce Lu
 */
@RestController
@RequestMapping("/login/log")
public class LoginLogController {

	@Autowired
	private LoginLogService loginLogService;

	/**
	 * 登录日志分页列表
	 *
	 * @param pageBO
	 */
	@PreAuthorize("@ss.hasPermission('login:log:listPage')")
	@RequestMapping("/listPage")
	public ResultPageModel<LoginLogPageVO> listPage(@Valid LoginLogListPageBO pageBO) {
		return loginLogService.listPageUser(pageBO);
	}

}
