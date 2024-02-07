/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.le.service.admin.login;

import com.le.common.constant.SsoConstants;
import com.le.common.constant.SsoPermissionConstants;
import com.le.common.enums.MenuTypeEnum;
import com.le.common.model.login.LoginResultVO;
import com.le.common.model.login.LoginUserVO;
import com.le.common.utils.ServletUtils;
import com.le.common.utils.StringUtils;
import com.le.common.utils.bean.BeanCopierUtil;
import com.le.common.utils.tree.ListToTreeUtil;
import com.le.dao.entity.SsoMenu;
import com.le.dao.mapper.SsoMenuMapper;
import com.le.dao.mapper.SsoRoleMapper;
import com.le.model.vo.login.LoginMenuVO;
import com.le.model.vo.login.LoginUserInfoVO;
import com.le.service.base.SsoTokenService;
import com.le.service.base.SysConfigService;
import com.le.framework.config.property.SysConfigProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * 用户权限处理
 *
 * @author Bruce Lu
 */
@Component
public class SysPermissionService {

	@Autowired
	private SsoTokenService ssoTokenService;
	@Resource
	private SsoMenuMapper ssoMenuMapper;
	@Resource
	private SsoRoleMapper ssoRoleMapper;
	@Resource
	private SysConfigProperty sysConfigProperty;
	@Resource
	private SysConfigService sysConfigService;

	/**
	 * 获取登录用户信息
	 *
	 * @return
	 * @param systemCode
	 */
	public LoginUserInfoVO getLoginUserInfo(String systemCode) {
		LoginResultVO loginResultVO = ssoTokenService.getLoginUser(ServletUtils.getRequest());
		LoginUserVO user = loginResultVO.getUser();
		user.setPassword("");

		//设定系统编码
		user.setSystemCode(systemCode);
		//菜单权限
		LoginUserInfoVO userInfo = this.getMenuPermission(user);
		//用户信息
		userInfo.setUser(user);
		//角色Key
		userInfo.setRoleKeyList(this.getRolePermission(user));
		return userInfo;
	}

	/**
	 * 获取角色数据权限
	 *
	 * @param user 用户信息
	 * @return 角色权限信息
	 */
	public Set<String> getRolePermission(LoginUserVO user) {
		Set<String> roles = new HashSet<>();
		// 管理员拥有所有权限
		if (sysConfigService.getSupperAdminUserId().equals(user.getUserId())) {
			roles.add("**");
		} else {
			roles.addAll(ssoRoleMapper.getRoleKeyBySysCodeAndUserId(sysConfigProperty.getAuthSsoSysCode(), user.getUserId()));
		}
		return roles;
	}

	/**
	 * 获取菜单数据权限
	 *
	 * @param user 用户信息
	 * @return 菜单权限信息
	 */
	public LoginUserInfoVO getMenuPermission(LoginUserVO user) {

		return this.getMenuPermissionByUser(getSysCode(user), user);
	}

	/**
	 * 默认使用管理中心编码
	 * 否则使用客户传递参数
	 * @param user
	 * @return
	 */
	private String getSysCode(LoginUserVO user) {
		if(StringUtils.isNotEmpty(user.getSystemCode())){
		  return user.getSystemCode();
		}

		return sysConfigProperty.getAuthSsoSysCode();
	}

	public LoginUserInfoVO getMenuPermissionByUser(String sysCode, LoginUserVO user) {
		Set<String> permissionList;
		List<SsoMenu> menuList;

		//管理员拥有所有权限
		if (sysConfigService.getSupperAdminUserId().equals(user.getUserId())) {
			permissionList = Collections.singleton(SsoPermissionConstants.ALL_PERMISSION);
			menuList = ssoMenuMapper.getEnableMenuListBySysCode(sysCode);
			//转换成树结构
			List<LoginMenuVO> treeNodeVOList = BeanCopierUtil.copyList(menuList, LoginMenuVO.class);
			ListToTreeUtil<LoginMenuVO> result = new ListToTreeUtil<>();
			return new LoginUserInfoVO(permissionList, result.getTreeListObject(treeNodeVOList));
		}

		//非管理员-根据角色查询权限
		menuList = ssoMenuMapper.getMenuListBySysCodeAndUserId(sysCode, user.getUserId());
		if (CollectionUtils.isEmpty(menuList)) {
			return LoginUserInfoVO.initDefault();
		}

		LoginMenuVO menuVO = null;
		List<LoginMenuVO> menuResList = new ArrayList<>(menuList.size());
		//权限标识集
		permissionList = new HashSet<>(menuList.size());
		for (SsoMenu menu : menuList) {
			if (StringUtils.isNotBlank(menu.getPermissions())) {
				permissionList.addAll(Arrays.asList(menu.getPermissions().split(SsoConstants.SPLIT_ESCAPE)));
			}
			//菜单
			if (MenuTypeEnum.isMenu(menu.getMenuType())) {
				menuVO = BeanCopierUtil.copy(menu, LoginMenuVO.class);
				menuResList.add(menuVO);
			}
		}
		//菜单转换成树结构
		List<LoginMenuVO> treeNodeVOList = BeanCopierUtil.copyList(menuResList, LoginMenuVO.class);
		ListToTreeUtil<LoginMenuVO> result = new ListToTreeUtil<>();
		return new LoginUserInfoVO(permissionList, result.getTreeListObject(treeNodeVOList));
	}


}
