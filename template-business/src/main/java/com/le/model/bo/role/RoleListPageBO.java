package com.le.model.bo.role;

import com.le.common.model.page.BasePageModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 角色分页查询bo
 *
 * @author Bruce Lu
 */
@Data
public class RoleListPageBO extends BasePageModel {

	private static final long serialVersionUID = -8206942361131504869L;

	/**
	 * 系统编码
	 */
	@NotBlank(message = "系统编码不能为空")
	private String sysCode;

	/**
	 * 角色名称 右模糊
	 */
	private String roleNameLike;

	/**
	 * 角色key 右模糊
	 */
	private String roleKeyLike;

	/**
	 * 状态 0-正常;1-停用
	 */
	private Integer status;


}
