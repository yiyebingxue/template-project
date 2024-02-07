package com.le.model.vo.role;

import lombok.Data;

import java.io.Serializable;

/**
 * 角色-下拉选项列表返回对象
 *
 * @author Bruce Lu
 */
@Data
public class RoleOptionVO implements Serializable {

	private static final long serialVersionUID = 6861288921505856266L;

	/**
	 * 角色ID
	 */
	private Long roleId;

	/**
	 * 角色名称
	 */
	private String roleName;

	private Integer status;

}
