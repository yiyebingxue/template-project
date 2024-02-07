package com.le.service.admin.impl;

import com.le.common.constant.SsoConstants;
import com.le.common.constant.SsoPermissionConstants;
import com.le.common.enums.DelFlagEnum;
import com.le.common.enums.SystemManagerStatusEnum;
import com.le.common.enums.SystemStatusEnum;
import com.le.common.enums.exception.SysResStatusEnum;
import com.le.common.enums.exception.SystemResStatusEnum;
import com.le.common.exception.BusinessException;
import com.le.common.model.BaseOperateBO;
import com.le.common.model.login.LoginResultVO;
import com.le.common.model.login.LoginUserVO;
import com.le.common.utils.SecurityUtils;
import com.le.common.utils.StringUtils;
import com.le.common.utils.bean.BeanCopierUtil;
import com.le.dao.entity.SsoSystem;
import com.le.dao.entity.SsoSystemManager;
import com.le.dao.entity.SsoUserSystem;
import com.le.dao.mapper.*;
import com.le.model.bo.platform.*;
import com.le.model.vo.platform.SystemDetailVO;
import com.le.model.vo.platform.SystemListVO;
import com.le.service.admin.SystemService;
import com.le.service.base.SysConfigService;
import com.le.service.base.SystemCacheService;
import com.le.framework.config.property.SysConfigProperty;
import com.le.service.admin.login.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 平台管理相关接口
 *
 * @author Bruce Lu
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SystemServiceImpl implements SystemService {

	@Resource
	private PermissionService permissionService;
	@Resource
	private SsoSystemMapper ssoSystemMapper;
	@Resource
	private SsoDeptMapper ssoDeptMapper;
	@Resource
	private SsoMenuMapper ssoMenuMapper;
	@Resource
	private SsoRoleMapper ssoRoleMapper;
	@Resource
	private SsoUserSystemMapper ssoUserSystemMapper;
	@Resource
	private SsoSystemManagerMapper ssoSystemManagerMapper;
	@Resource
	private SysConfigProperty sysConfigProperty;
	@Resource
	private SysConfigService sysConfigService;
	@Resource
	private SystemCacheService systemCacheService;

	/**
	 * 我的平台列表
	 *
	 * @return 系统列表
	 */
	@Override
	public List<SystemListVO> listMySystem() {
		//当前登录用户
		LoginUserVO loginUser = SecurityUtils.getLoginUser();

		List<SsoSystem> systemList = null;
		//管理员拥有所有权限
		if (sysConfigService.getSupperAdminUserId().equals(loginUser.getUserId())) {
			systemList = ssoSystemMapper.listMySystemByAdmin(loginUser.getUserId(), SystemStatusEnum.getEnableStatusList());
		} else {
			systemList = ssoSystemMapper.listMySystemByUserId(loginUser.getUserId());
		}

		if (CollectionUtils.isEmpty(systemList)) {
			return new ArrayList<>(0);
		}

		SystemListVO item = null;
		List<SystemListVO> systemResList = new ArrayList<>();
		for (SsoSystem ssoSystem : systemList) {
			//认证中心系统-不加入跳转
			if (ssoSystem.getSysCode().equals(sysConfigProperty.getAuthSsoSysCode())) {
				continue;
			}
			item = BeanCopierUtil.copy(ssoSystem, SystemListVO.class);
			systemResList.add(item);
		}
		return systemResList;
	}

	/**
	 * 我的平台排序
	 *
	 * @param systemSortBO
	 */
	@Override
	public void sortMySystem(SystemSortBO systemSortBO) {
		//当前登录用户
		LoginResultVO loginUser = SecurityUtils.getLoginUserResult();

		int i = 0;
		SsoUserSystem operateSystem = null;
		for (String sysCode : systemSortBO.getSysCodeList()) {
			i++;
			operateSystem = new SsoUserSystem();
			operateSystem.setCreateBy(loginUser.getOperateName());
			operateSystem.setUpdateBy(loginUser.getOperateName());
			operateSystem.setSortNum(i);
			SsoUserSystem ssoUserSystem = ssoUserSystemMapper.getByUserIdAndSysCode(loginUser.getUser().getUserId(), sysCode);
			if (null != ssoUserSystem) {
				operateSystem.setId(ssoUserSystem.getId());
				//更新排序值
				ssoUserSystemMapper.updateByIdSelective(operateSystem);
			} else {
				//新增用户平台关系
				operateSystem.setUserId(loginUser.getUser().getUserId());
				operateSystem.setSysCode(sysCode);
				ssoUserSystemMapper.insertSelective(operateSystem);
			}
		}
	}

	/**
	 * 平台管理列表
	 *
	 * @return 系统列表
	 */
	@Override
	public List<SystemListVO> listMyMgmtSystem() {
		//当前登录用户
		LoginResultVO loginUser = SecurityUtils.getLoginUserResult();

		List<SsoSystem> systemList = null;
		//管理员拥有所有权限
		if (sysConfigService.getSupperAdminUserId().equals(loginUser.getUser().getUserId())) {
			systemList = ssoSystemMapper.listMySystemByAdmin(loginUser.getUser().getUserId(), SystemStatusEnum.getAllStatusList());
		}
		//认证中心管理员-角色拥有所有平台管理权限
		else if (loginUser.getRoleKeyList().contains(SsoPermissionConstants.ADMIN_ROLE_KEY)) {
			systemList = ssoSystemMapper.listMySystemByAdmin(loginUser.getUser().getUserId(), SystemStatusEnum.getAllStatusList());
		} else {
			//查询用户平台管理权限
			systemList = ssoSystemMapper.listSystemMgmtByUserId(loginUser.getUser().getUserId());
		}

		if (CollectionUtils.isEmpty(systemList)) {
			return new ArrayList<>(0);
		}

		return BeanCopierUtil.copyList(systemList, SystemListVO.class);
	}

	/**
	 * 根据系统平台编码查询
	 *
	 * @param sysCode
	 * @return 系统详情
	 */
	@Override
	public SystemDetailVO getDetailBySysCode(String sysCode) {
		SsoSystem ssoSystem = ssoSystemMapper.getBySysCode(sysCode);
		//系统不存在
		if (null == ssoSystem) {
			throw new BusinessException(SysResStatusEnum.SYS_NOT_FOUND);
		}
		//当前用户无修改秘钥权限-为了安全则隐藏秘钥值
		if (!permissionService.hasPermission(SsoConstants.UPDATE_SECRET_KEY)) {
			ssoSystem.setPublicKey("");
		}
		return BeanCopierUtil.copy(ssoSystem, SystemDetailVO.class);
	}

	/**
	 * 新增-目标平台系统
	 *
	 * @param saveBO
	 * @author Bruce Lu
	 */
	@Override
	public void addSystem(SystemSaveBO saveBO) {
		//获取登录用户
		LoginUserVO loginUser = SecurityUtils.getLoginUser();

		//校验系统编码唯一性
		this.checkSysCodeUniq(saveBO.getSysCode());

		//构建ssoSystem
		SsoSystem ssoSystem = this.buildSsoSystem(saveBO);
		//默认正常状态
		ssoSystem.setStatus(SystemStatusEnum.ENABLE.getStatus());

		//新增
		ssoSystemMapper.insertSelective(ssoSystem);

		//管理员拥有所有权限
		if (sysConfigService.getSupperAdminUserId().equals(loginUser.getUserId())) {
			log.info("[ 平台新增完成 ] >> {}", saveBO.getLogValue());
			return;
		}

		//若非管理员-为新增该平台用户-添加用户与当前系统关系
		this.addUserSystemRelation(saveBO.getSysCode(), loginUser.getUserId(), saveBO);
		//为新增该平台用户-添加系统管理权限
		this.addSystemMgmt(saveBO.getSysCode(), loginUser.getUserId(), saveBO);
		log.info("[ 平台新增完成 ] 非管理员-已添加管理权限 >> {}", saveBO.getLogValue());
	}

	/**
	 * 修改-目标平台系统
	 *
	 * @param updateBO
	 * @author Bruce Lu
	 */
	@Override
	public void updateSystem(SystemUpdateBO updateBO) {
		//查询旧系统信息
		SsoSystem oldSsoSystem = ssoSystemMapper.getBySysId(updateBO.getSysId());
		if (null == oldSsoSystem) {
			throw new BusinessException(SystemResStatusEnum.SYS_NOT_EXISTS);
		}
		//修改系统编码
		if (!updateBO.getSysCode().equals(oldSsoSystem.getSysCode())) {
			//校验-旧系统编码是否已经引用
			this.checkSysCodeIsUse(oldSsoSystem.getSysCode());
		}

		//校验系统编码唯一性
		this.checkSysCodeUniq(updateBO.getSysCode(), updateBO.getSysId());

		//构建ssoSystem
		SsoSystem ssoSystem = this.buildSsoSystem(updateBO);
		ssoSystem.setSysId(updateBO.getSysId());
		ssoSystem.setStatus(updateBO.getStatus());

		//修改平台
		ssoSystemMapper.updateBySysId(ssoSystem);

		//移除缓存
		systemCacheService.removeSystemCache(updateBO.getSysCode());

		log.info("[ 平台修改完成 ] >> {}", updateBO.getLogValue());

	}

	/**
	 * 修改-秘钥
	 *
	 * @param updateBO
	 * @author Bruce Lu
	 */
	@Override
	public void updateSystemSecret(SystemUpdateSecretBO updateBO) {
		SsoSystem ssoSystem = new SsoSystem();
		ssoSystem.setSysId(updateBO.getSysId());
		ssoSystem.setSignType(updateBO.getSignType());
		ssoSystem.setPublicKey(StringUtils.dealBlankDefault(updateBO.getPublicKey(), ""));
		ssoSystem.setUpdateBy(updateBO.getOperateBy());
		//修改平台
		ssoSystemMapper.updateSecretBySysId(ssoSystem);
		//移除缓存
		systemCacheService.removeSystemCache(updateBO.getSysCode());
		log.info("[ 平台修改秘钥完成 ] >> {} ", updateBO.getLogValue());

	}

	/**
	 * 删除-目标平台系统（逻辑删）
	 *
	 * @param deleteBO
	 * @author Bruce Lu
	 */
	@Override
	public void deleteSystem(SystemDeleteBO deleteBO) {
		SsoSystem ssoSystem = ssoSystemMapper.getBySysId(deleteBO.getSysId());
		if (null == ssoSystem) {
			throw new BusinessException(SystemResStatusEnum.SYS_NOT_EXISTS);
		}

		if (DelFlagEnum.DELETED.getStatus().equals(ssoSystem.getDelFlag())) {
			throw new BusinessException(SystemResStatusEnum.ALREADY_DELETE, ssoSystem.getSysName());
		}

		//校验是否已经引用
		this.checkSysCodeIsUse(ssoSystem.getSysCode());

		//标记为删除态
		SsoSystem deleteSystem = new SsoSystem();
		deleteSystem.setSysId(deleteBO.getSysId());
		deleteSystem.setDelFlag(DelFlagEnum.DELETED.getStatus());
		deleteSystem.setUpdateBy(deleteBO.getOperateBy());
		ssoSystemMapper.updateBySysIdSelective(deleteSystem);

		//移除缓存
		systemCacheService.removeSystemCache(deleteSystem.getSysCode());
		log.info("[ 平台删除完成 ] >> sysId:{} , operateBy:{}", deleteBO.getSysId(), deleteBO.getOperateBy());
	}

	/**
	 * 校验 系统编码 唯一性
	 *
	 * @param sysCode      系统编码
	 * @param excludeSysId 需要排除的sysId（修改时用）
	 */
	private void checkSysCodeUniq(String sysCode, Long... excludeSysId) {
		if (StringUtils.isEmpty(sysCode)) {
			return;
		}
		SsoSystem ssoSystem = ssoSystemMapper.getBySysCode(sysCode);
		if (null == ssoSystem) {
			return;
		}
		if (!ObjectUtils.isEmpty(excludeSysId) && ssoSystem.getSysId().equals(excludeSysId[0])) {
			return;
		}

		log.error("[ 系统编码已经存在 ] >> sysCode:{}", sysCode);
		//系统编码已经存在
		throw new BusinessException(SystemResStatusEnum.SYS_CODE_EXISTS, sysCode);
	}

	/**
	 * 校验系统是否已经在使用
	 */
	private void checkSysCodeIsUse(String sysCode) {
		if (StringUtils.isBlank(sysCode)) {
			return;
		}

		//用户
		if (ssoUserSystemMapper.countBySysCode(sysCode) > 0) {
			throw new BusinessException(SystemResStatusEnum.ALREADY_BIND_USER);
		}

		//菜单
		if (ssoMenuMapper.countBySysCode(sysCode) > 0) {
			throw new BusinessException(SystemResStatusEnum.ALREADY_BIND_MENU);
		}

		//角色
		if (ssoRoleMapper.countBySysCode(sysCode) > 0) {
			throw new BusinessException(SystemResStatusEnum.ALREADY_BIND_ROLE);
		}

		//部门
		if (ssoDeptMapper.countBySysCode(sysCode) > 0) {
			throw new BusinessException(SystemResStatusEnum.ALREADY_BIND_DEPT);
		}
	}

	/**
	 * 添加用户-系统关系
	 *
	 * @param sysCode
	 * @param userId
	 * @param operateBO
	 */
	public void addUserSystemRelation(String sysCode, Long userId, BaseOperateBO operateBO) {
		SsoUserSystem ssoUserSystem = ssoUserSystemMapper.getByUserIdAndSysCode(userId, sysCode);
		if (null != ssoUserSystem) {
			log.error("[ 该用户与系统-关系已经存在 ] >> sysCode:{} , userId:{}, operateBy:{}", sysCode, userId, operateBO.getOperateBy());
			return;
		}

		SsoUserSystem userSystem = new SsoUserSystem();
		userSystem.setUserId(userId);
		userSystem.setSysCode(sysCode);
		userSystem.setCreateBy(operateBO.getOperateBy());
		userSystem.setUpdateBy(operateBO.getOperateBy());
		ssoUserSystemMapper.insertSelective(userSystem);
	}

	public void addSystemMgmt(String sysCode, Long userId, BaseOperateBO operateBO) {
		int count = ssoSystemManagerMapper.countBySysCodeAndUserId(sysCode, userId);
		if (count > 0) {
			log.error("[ 该用户管理权限关系已经存在 ] >> sysCode:{} , userId:{}, operateBy:{}", sysCode, userId, operateBO.getOperateBy());
			return;
		}
		SsoSystemManager saveEntity = new SsoSystemManager();
		saveEntity.setUserId(userId);
		saveEntity.setStatus(SystemManagerStatusEnum.OK.getStatus());
		saveEntity.setSysCode(sysCode);
		saveEntity.setCreateBy(operateBO.getOperateBy());
		saveEntity.setUpdateBy(operateBO.getOperateBy());
		ssoSystemManagerMapper.insertSelective(saveEntity);
		log.info("[ 新增平台-管理员权限完成 ] >> sysCode:{} , userId:{}, operateBy:{}", sysCode, userId, operateBO.getOperateBy());
	}

	private SsoSystem buildSsoSystem(SystemSaveBO saveBO) {
		SsoSystem ssoSystem = new SsoSystem();
		ssoSystem.setSysCode(saveBO.getSysCode());
		ssoSystem.setSysName(saveBO.getSysName());
		ssoSystem.setSysUrl(saveBO.getSysUrl());
		ssoSystem.setSysIcon(saveBO.getSysIcon());
		ssoSystem.setSysEnv(saveBO.getSysEnv());
		ssoSystem.setSortNum(saveBO.getSortNum());
		ssoSystem.setRemarks(saveBO.getRemarks());
		ssoSystem.setCreateBy(saveBO.getOperateBy());
		ssoSystem.setUpdateBy(saveBO.getOperateBy());
		return ssoSystem;
	}
}
