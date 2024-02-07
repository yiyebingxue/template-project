package com.le.model.bo.platform;

import com.le.common.model.BaseOperateBO;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 系统管理权限-新增BO
 *
 * @author Bruce Lu
 */
@Data
public class SystemMgmtStatusBO extends BaseOperateBO {

	private static final long serialVersionUID = -5187844120648189372L;

	/**
	 * ID
	 */
	@NotNull(message = "ID不为空")
	private Long id;

	/**
	 * 状态
	 */
	@NotNull(message = "状态不为空")
	private Integer status;

	public String getLogValue() {
		return String.format("sysId:%d, status:%s , operateBy:%s", this.id, this.status, this.getOperateBy());
	}
}
