/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.le.security.filter;

import com.le.common.model.login.LoginResultVO;
import com.le.common.utils.ObjectUtils;
import com.le.common.utils.SecurityUtils;
import com.le.service.base.SsoTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * token过滤器 验证token有效性
 *
 * @author Bruce Lu
 */
@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private SsoTokenService ssoTokenService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		LoginResultVO loginResultVO = ssoTokenService.getLoginUser(request);
		if (ObjectUtils.isNotNull(loginResultVO) && ObjectUtils.isNull(SecurityUtils.getAuthentication())) {
			ssoTokenService.renewTokenByAdminWeb(loginResultVO);
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginResultVO, null, loginResultVO.getAuthorities());
			authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		}
		chain.doFilter(request, response);
	}
}
