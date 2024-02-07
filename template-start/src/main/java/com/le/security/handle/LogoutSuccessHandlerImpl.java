/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.le.security.handle;

import com.alibaba.fastjson.JSON;
import com.le.common.enums.exception.SysResStatusEnum;
import com.le.common.model.login.LoginResultVO;
import com.le.common.model.result.ResultModel;
import com.le.common.utils.ServletUtils;
import com.le.service.base.SsoTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义退出处理类 返回成功
 *
 * @author Bruce Lu
 */
@Configuration
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {

	@Autowired
	private SsoTokenService ssoTokenService;

	/**
	 * 用户退出处理
	 */
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		LoginResultVO loginResultVO = ssoTokenService.getLoginUser(request);
		if (!ObjectUtils.isEmpty(loginResultVO)) {
			//用户退出处理
			ssoTokenService.logoOutUser(loginResultVO);
		}
		ServletUtils.renderString(response, JSON.toJSONString(ResultModel.error(SysResStatusEnum.SUCCESS.getCode(), "退出成功")));
	}
}
