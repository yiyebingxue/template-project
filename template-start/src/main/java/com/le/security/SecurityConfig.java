/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.le.security;

import com.le.security.filter.TokenAuthenticationFilter;
import com.le.security.handle.AuthenticationEntryPointImpl;
import com.le.security.handle.LogoutSuccessHandlerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.filter.CorsFilter;

import javax.annotation.Resource;

/**
 * spring security配置
 *
 * @author Bruce Lu
 */
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	/**
	 * 自定义用户认证逻辑
	 */
	@Resource
	private UserDetailsService userDetailsService;
	/**
	 * 认证失败处理类
	 */
	@Autowired
	private AuthenticationEntryPointImpl unauthorizedHandler;
	/**
	 * 退出处理类
	 */
	@Autowired
	private LogoutSuccessHandlerImpl logoutSuccessHandler;
	/**
	 * token认证过滤器
	 */
	@Autowired
	private TokenAuthenticationFilter tokenAuthenticationFilter;
	/**
	 * 跨域过滤器
	 */
	@Autowired
	private CorsFilter corsFilter;

	/**
	 * 解决 无法直接注入 AuthenticationManager
	 *
	 * @return
	 * @throws Exception
	 */
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	/**
	 * Security 配置
	 * 规则如下：
	 * anyRequest          ->   匹配所有请求路径
	 * access              ->   SpringEl表达式结果为true时可以访问
	 * anonymous           ->   匿名可以访问
	 * denyAll             ->   用户不能访问
	 * fullyAuthenticated  ->   用户完全认证可以访问（非remember-me下自动登录）
	 * hasAnyAuthority     ->   如果有参数，参数表示权限，则其中任何一个权限可以访问
	 * hasAnyRole          ->   如果有参数，参数表示角色，则其中任何一个角色可以访问
	 * hasAuthority        ->   如果有参数，参数表示权限，则其权限可以访问
	 * hasIpAddress        ->   如果有参数，参数表示IP地址，如果用户IP和参数匹配，则可以访问
	 * hasRole             ->   如果有参数，参数表示角色，则其角色可以访问
	 * permitAll           ->   用户可以任意访问
	 * rememberMe          ->   允许通过remember-me登录的用户访问
	 * authenticated       ->   用户登录后可访问
	 */
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
				// 不使用session 禁用 CSRF
				.csrf().disable()
				// 认证失败处理
				.exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
				// 基于token，所以不需要session
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				// 过滤请求
				.authorizeRequests()
				// 对于登录login 验证码captchaImage 允许匿名访问
				.antMatchers("/**/login", "/**/captchaImage").anonymous()
				.antMatchers(
						HttpMethod.GET,
						"/*.html",
						"/**/*.html",
						"/**/*.css",
						"/**/*.js",
						"/**/*.ico"
				).permitAll()
				.antMatchers("/profile/**").anonymous()
				.antMatchers("/**/webjars/**").anonymous()
				.antMatchers("/**/static/**").anonymous()
				.antMatchers("/**/druid/**").anonymous()
				//开放接口不拦截-内部有验签
				.antMatchers("/open/gateway").anonymous()
				// 除上面外的所有请求全部需要鉴权认证
				.anyRequest().authenticated()
				.and()
				.headers().frameOptions().disable();
		// 退出登录处理器
		httpSecurity.logout().logoutUrl("/**/logout").logoutSuccessHandler(logoutSuccessHandler);
		// 添加账号校验 filter
		httpSecurity.addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		// 添加CORS filter
		httpSecurity.addFilterBefore(corsFilter, TokenAuthenticationFilter.class);
		// 登出
		httpSecurity.addFilterBefore(corsFilter, LogoutFilter.class);
	}


	/**
	 * 强散列哈希加密实现
	 */
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * 身份-认证接口
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
	}
}
