package com.le.model.bo.platform;

import com.le.common.model.BaseOperateBO;
import lombok.Data;

/**
 * 修改平台新增bo
 *
 * @author Bruce Lu
 */
@Data
public class SystemDeleteBO extends BaseOperateBO {

	private static final long serialVersionUID = 4340162202000538970L;

	/**
	 * 系统平台ID
	 */
	private Long sysId;


	public String getLogValue() {
		return String.format("sysId:%d, operateBy:%s", this.sysId, this.getOperateBy());
	}

}
