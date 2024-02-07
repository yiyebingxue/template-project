package com.le.service.admin;


import com.le.common.model.result.ResultPageModel;
import com.le.model.bo.platform.SystemMgmtListPageBO;
import com.le.model.bo.platform.SystemMgmtRemoveBO;
import com.le.model.bo.platform.SystemMgmtSaveBO;
import com.le.model.bo.platform.SystemMgmtStatusBO;
import com.le.model.vo.platform.SystemMgmtPageVO;

/**
 * 系统管理-数据权限相关接口
 *
 * @author Bruce Lu
 */
public interface SystemMgmtService {

	/**
	 * 系统管理员-用户分页列表
	 *
	 * @param pageBO
	 * @return 用户
	 */
	ResultPageModel<SystemMgmtPageVO> listPageSysMgmtUser(SystemMgmtListPageBO pageBO);

	/**
	 * 新增-管理员权限
	 *
	 * @param saveBO
	 * @author Bruce Lu
	 */
	void addSystemMgmt(SystemMgmtSaveBO saveBO);

	/**
	 * 修改-管理员权限账号状态
	 *
	 * @param saveBO
	 * @author Bruce Lu
	 */
	void updateStatus(SystemMgmtStatusBO saveBO);

	/**
	 * 移除-用户管理员权限
	 *
	 * @param removeBO
	 * @author Bruce Lu
	 */
	void removeUserSystemMgmt(SystemMgmtRemoveBO removeBO);
}
