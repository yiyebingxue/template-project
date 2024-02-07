/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.le.controller.admin;


import com.le.common.model.result.ResultModel;
import com.le.model.bo.login.LoginBO;
import com.le.model.vo.login.LoginTokenVO;
import com.le.model.vo.login.LoginUserInfoVO;
import com.le.service.admin.login.SsoLoginService;
import com.le.service.admin.login.SysPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 登录验证
 *
 * @author Bruce Lu
 */
@RestController
public class LoginController {

	@Autowired
	private SsoLoginService ssoLoginService;

	@Autowired
	private SysPermissionService permissionService;

	/**
	 * 登录方法
	 *
	 * @param loginBO 登录信息
	 * @return 结果
	 */
	@PostMapping("/login")
	public ResultModel<LoginTokenVO> login(@Valid @RequestBody LoginBO loginBO) {
		return ResultModel.success(ssoLoginService.login(loginBO));
	}

	/**
	 * 获取用户信息
	 *
	 * @return 用户信息
	 */
	@GetMapping("getUserInfo")
	public ResultModel<?> getInfo(@RequestParam(required = false) String systemCode) {
		LoginUserInfoVO userInfo = permissionService.getLoginUserInfo(systemCode);
		return ResultModel.success(userInfo);
	}
}
