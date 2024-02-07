package com.le.model.bo.user;

import com.le.common.model.BaseOperateBO;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 用户删除bo
 *
 * @author Bruce Lu
 */
@Data
public class UserSystemRemoveBO extends BaseOperateBO {

	private static final long serialVersionUID = 2060892792625221284L;

	/**
	 * 系统编码
	 */
	@NotBlank(message = "系统编码不能为空")
	private String sysCode;

	/**
	 * 用户ID
	 */
	@NotNull(message = "用户ID不为空")
	private Long userId;


	public String getLogValue() {
		return String.format("sysCode:%s , userId:%d, operateBy:%s",
				this.sysCode, this.userId, this.getOperateBy());
	}
}
