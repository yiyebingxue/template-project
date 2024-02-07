package com.le.model.bo.role;

import com.le.common.model.page.BasePageModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 角色-下拉选项查询bo
 *
 * @author Bruce Lu
 */
@Data
public class RoleListOptionBO extends BasePageModel {

	private static final long serialVersionUID = 8961034980910026149L;

	/**
	 * 系统编码
	 */
	@NotBlank(message = "系统编码不能为空")
	private String sysCode;

	/**
	 * 角色名称右模糊
	 */
	private String roleNameLike;


}
