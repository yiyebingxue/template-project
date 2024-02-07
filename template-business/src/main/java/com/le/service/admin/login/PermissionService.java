/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.le.service.admin.login;

import com.le.common.constant.SsoPermissionConstants;
import com.le.common.model.login.LoginResultVO;
import com.le.common.utils.ObjectUtils;
import com.le.common.utils.ServletUtils;
import com.le.common.utils.StringUtils;
import com.le.service.base.SsoTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Set;

/**
 * 自定义权限实现，ss取自SpringSecurity首字母
 *
 * @author Bruce Lu
 */
@Service("ss")
public class PermissionService {

	@Autowired
	private SsoTokenService ssoTokenService;

	/**
	 * 验证用户是否具备某权限
	 *
	 * @param permission 权限字符串，示例：user:add
	 * @return 用户是否具备某权限
	 */
	public boolean hasPermission(String permission) {
		if (StringUtils.isEmpty(permission)) {
			return false;
		}
		LoginResultVO loginUser = ssoTokenService.getLoginUser(ServletUtils.getRequest());
		if (ObjectUtils.isNull(loginUser) || CollectionUtils.isEmpty(loginUser.getPermissionList())) {
			return false;
		}
		return hasPermissions(loginUser.getPermissionList(), permission);
	}

	/**
	 * 判断是否包含权限
	 *
	 * @param permissions 权限列表
	 * @param permission  权限字符串
	 * @return 用户是否具备某权限
	 */
	private boolean hasPermissions(Set<String> permissions, String permission) {
		return permissions.contains(SsoPermissionConstants.ALL_PERMISSION) || permissions.contains(StringUtils.trim(permission));
	}
}
