/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.le.service.admin;

import com.le.model.bo.dept.DeptDeleteBO;
import com.le.model.bo.dept.DeptMgmtTreeBO;
import com.le.model.bo.dept.DeptSaveBO;
import com.le.model.bo.dept.DeptUpdateBO;
import com.le.model.vo.dept.DeptDetailVO;
import com.le.model.vo.dept.DeptMgmtTreeNodeVO;
import com.le.model.vo.dept.DeptOptionTreeNodeVO;

import java.util.List;

/**
 * 部门管理接口
 *
 * @author Bruce Lu
 */
public interface DeptService {

	/**
	 * 获取系统下的-部门树
	 *
	 * @param treeBO 查询参数
	 * @return 部门树
	 * @author Bruce Lu
	 */
	List<DeptMgmtTreeNodeVO> listDeptMgmtTree(DeptMgmtTreeBO treeBO);

	/**
	 * 部门下拉选项树
	 * 状态：仅正常+未删除
	 *
	 * @param sysCode 系统编码
	 * @return 部门树
	 * @author Bruce Lu
	 */
	List<DeptOptionTreeNodeVO> listDeptOptionTree(String sysCode);

	/**
	 * 部门详情
	 *
	 * @param deptId 部门ID
	 * @return 部门详情
	 * @author Bruce Lu
	 */
	DeptDetailVO getDeptDetail(Long deptId);

	/**
	 * 新增部门
	 *
	 * @param saveBO
	 * @author Bruce Lu
	 */
	void addDept(DeptSaveBO saveBO);

	/**
	 * 修改部门
	 *
	 * @param updateBO
	 * @author Bruce Lu
	 */
	void updateDept(DeptUpdateBO updateBO);

	/**
	 * 删除部门（部门逻辑删-用户部门关系物理删）
	 *
	 * @param deleteBO
	 * @author Bruce Lu
	 */
	void deleteDept(DeptDeleteBO deleteBO);


}
