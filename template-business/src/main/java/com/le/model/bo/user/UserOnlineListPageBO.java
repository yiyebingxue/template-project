package com.le.model.bo.user;

import com.le.common.model.page.BasePageModel;
import lombok.Data;

/**
 * 在线用户分页查询bo
 *
 * @author Bruce Lu
 */
@Data
public class UserOnlineListPageBO extends BasePageModel {

	private static final long serialVersionUID = 4278605423671971482L;

	/**
	 * 用户登录名
	 */
	private String usernameLike;

	/**
	 * 登录开始时间
	 */
	private String loginStartTime;

	/**
	 * 登录截止时间
	 */
	private String loginEndTime;

}
