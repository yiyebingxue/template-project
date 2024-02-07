package com.le.service.admin;

import com.le.common.model.result.ResultPageModel;
import com.le.model.bo.role.*;
import com.le.model.vo.role.RoleDetailVO;
import com.le.model.vo.role.RoleOptionVO;
import com.le.model.vo.role.RolePageVO;

import java.util.List;

/**
 * 角色相关接口
 *
 * @author Bruce Lu
 */
public interface RoleService {

	/**
	 * 分页列表
	 *
	 * @param pageBO
	 * @return 角色列表
	 */
	ResultPageModel<RolePageVO> listPageRole(RoleListPageBO pageBO);

	/**
	 * 角色下拉选项列表
	 *
	 * @param optionBO
	 * @return 角色列表
	 */
	List<RoleOptionVO> listRoleOption(RoleListOptionBO optionBO);

	/**
	 * 角色详情
	 *
	 * @param roleId 角色ID
	 * @return 角色详情
	 * @author Bruce Lu
	 */
	RoleDetailVO getDeptDetail(Long roleId);

	/**
	 * 新增角色
	 *
	 * @param saveBO
	 * @author Bruce Lu
	 */
	void addRole(RoleSaveBO saveBO);

	/**
	 * 修改角色
	 *
	 * @param updateBO
	 * @author Bruce Lu
	 */
	void updateRole(RoleUpdateBO updateBO);

	/**
	 * 删除角色（逻辑删）
	 *
	 * @param deleteBO
	 * @author Bruce Lu
	 */
	void deleteRole(RoleDeleteBO deleteBO);

	/**
	 * 移除角色用户绑定关系
	 *
	 * @param removeUserBO
	 * @author Bruce Lu
	 */
	void removeUserRole(RoleRemoveUserBO removeUserBO);
}
