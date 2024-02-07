package com.le.service.base;

import com.le.common.constant.SsoConstants;
import com.le.common.utils.StringUtils;
import com.le.framework.config.property.SysConfigProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 获取配置参数公共接口
 *
 * @author Bruce Lu
 */
@Slf4j
@Component
public class SysConfigService {

	@Resource
	private SysConfigProperty sysConfigProperty;

	/**
	 * 获取系统管理员用户ID
	 */
	public Long getSupperAdminUserId() {
		String supperAdminUser = sysConfigProperty.getSupperAdminUser();
		if (StringUtils.isBlank(supperAdminUser)) {
			return null;
		}
		String[] splitUser = supperAdminUser.split(SsoConstants.SPLIT_ESCAPE);
		if (splitUser.length < 1) {
			return null;
		}
		return Long.parseLong(splitUser[0]);
	}
}
