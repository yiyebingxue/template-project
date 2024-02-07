/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.le.service.getway;

import com.le.common.annotation.OpenApi;
import com.le.model.bo.getway.ApplyAuthBO;
import com.le.model.vo.getway.ApplyAuthVO;

/**
 * 申请认证开放接口
 *
 * @author Bruce Lu
 */
public interface ApplyAuthService {

	/**
	 * 申请认证开放接口
	 *
	 * @param authBO
	 */
	@OpenApi(method = "com.le.applyAuth", desc = "申请认证")
	ApplyAuthVO applyAuth(String sysCode, ApplyAuthBO authBO);
}
