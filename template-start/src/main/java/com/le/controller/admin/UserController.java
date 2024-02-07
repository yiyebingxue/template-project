/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.le.controller.admin;


import com.le.common.model.login.LoginUserVO;
import com.le.common.model.result.ResultModel;
import com.le.common.model.result.ResultPageModel;
import com.le.common.utils.SecurityUtils;
import com.le.model.bo.user.*;
import com.le.model.vo.user.UserDetailVO;
import com.le.model.vo.user.UserOptionVO;
import com.le.model.vo.user.UserPageVO;
import com.le.model.vo.user.UserProfileVO;
import com.le.service.admin.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 用户管理接口
 *
 * @author Bruce Lu
 */
@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	/**
	 * 用户分页列表
	 *
	 * @param pageBO
	 */
	@PreAuthorize("@ss.hasPermission('user:listPage')")
	@RequestMapping("/listPage")
	public ResultPageModel<UserPageVO> listPage(@Valid UserListPageBO pageBO) {
		return userService.listPageUser(pageBO);
	}

	/**
	 * 用户详情
	 *
	 * @param detailBO 用户ID
	 */
	@RequestMapping("/detail")
	public ResultModel<UserDetailVO> getUserDetail(@Valid @RequestBody UserDetailBO detailBO) {
		return ResultModel.success(userService.getUserDetail(detailBO));
	}

	/**
	 * 个人详情
	 */
	@RequestMapping("/profile")
	public ResultModel<UserProfileVO> getUserProfile() {
		LoginUserVO loginUser = SecurityUtils.getLoginUser();
		return ResultModel.success(userService.getUserProfile(loginUser.getUserId()));
	}

	/**
	 * 根据手机号查询用户
	 *
	 * @param phone 手机号
	 */
	@RequestMapping("/getUserByPhone/{phone}")
	public ResultModel<UserOptionVO> getUserByPhone(@PathVariable String phone) {
		return ResultModel.success(userService.getUserByPhone(phone));
	}

	/**
	 * 根据用户名查询用户
	 *
	 * @param username 用户名
	 */
	@RequestMapping("/getUserByUserName/{username}")
	public ResultModel<UserOptionVO> getByPhone(@PathVariable String username) {
		return ResultModel.success(userService.getUserByUserName(username));
	}

	/**
	 * 根据-关键字搜索用户
	 *
	 * @param keywords 关键字模糊
	 */
	@RequestMapping("/listUserOption")
	public ResultModel<?> listUserOption(@RequestParam(required = false) String keywords) {
		return ResultModel.success(userService.listUserOption(keywords));
	}

	/**
	 * 添加用户
	 *
	 * @param addBO
	 */
	@PostMapping("/add")
	@PreAuthorize("@ss.hasPermission('user:add')")
	public ResultModel<?> addUser(@Valid @RequestBody UserAddBO addBO) {
		addBO.setOperateBy(SecurityUtils.getOperateName());
		userService.addUser(addBO);
		return ResultModel.success();
	}

	/**
	 * 添加用户系统关系
	 *
	 * @param addBO
	 */
	@PostMapping("/addUserPlatformRelation")
	public ResultModel<?> addUserPlatformRelation(@Valid @RequestBody UserPlatformAddBO addBO) {
		addBO.setOperateBy(SecurityUtils.getOperateName());
		userService.addUserPlatformRelation(addBO);
		return ResultModel.success();
	}

	/**
	 * 修改用户
	 *
	 * @param updateBO
	 */
	@PutMapping("/update")
	@PreAuthorize("@ss.hasPermission('user:update')")
	public ResultModel<?> updateUser(@Valid @RequestBody UserUpdateBO updateBO) {
		updateBO.setOperateBy(SecurityUtils.getOperateName());
		userService.updateUser(updateBO);
		return ResultModel.success();
	}

	/**
	 * 移除用户与系统关系
	 *
	 * @param removeBO
	 */
	@RequestMapping("/removeUserSystem")
	@PreAuthorize("@ss.hasPermission('user:removeUserSystem')")
	public ResultModel<?> removeUserSystem(@Valid @RequestBody UserSystemRemoveBO removeBO) {
		removeBO.setOperateBy(SecurityUtils.getOperateName());
		userService.removeUserSystem(removeBO);
		return ResultModel.success();
	}

	/**
	 * 修改个人信息
	 *
	 * @param updateBO
	 */
	@PutMapping("/updateProfile")
	public ResultModel<?> updateProfile(@Valid @RequestBody UserUpdateProfileBO updateBO) {
		updateBO.setOperateBy(SecurityUtils.getOperateName());
		userService.updateProfile(updateBO);
		return ResultModel.success();
	}

	/**
	 * 修改个人密码
	 *
	 * @param updateBO
	 */
	@PutMapping("/updatePwd")
	public ResultModel<?> updatePwd(@Valid @RequestBody UserUpdatePwdBO updateBO) {
		updateBO.setOperateBy(SecurityUtils.getOperateName());
		userService.updatePwd(updateBO);
		return ResultModel.success();
	}

	/**
	 * 重置密码
	 *
	 * @param restPwdBO
	 */
	@PutMapping("/resetPwd")
	@PreAuthorize("@ss.hasPermission('user:resetPwd')")
	public ResultModel<?> resetPwd(@Valid @RequestBody UserRestPwdBO restPwdBO) {
		restPwdBO.setOperateBy(SecurityUtils.getOperateName());
		userService.resetPwd(restPwdBO);
		return ResultModel.success();
	}

	/**
	 * 删除用户
	 *
	 * @param deleteBO
	 */
	@RequestMapping("/delete")
	@PreAuthorize("@ss.hasPermission('user:delete')")
	public ResultModel<?> deleteUser(@Valid @RequestBody UserDeleteBO deleteBO) {
		deleteBO.setOperateBy(SecurityUtils.getOperateName());
		userService.deleteUser(deleteBO);
		return ResultModel.success();
	}
}
