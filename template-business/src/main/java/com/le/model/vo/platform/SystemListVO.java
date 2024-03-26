package com.le.model.vo.platform;

import lombok.Data;

import java.io.Serializable;


/**
 * 目标系统实体类
 *
 * @author Bruce Lu
 * @date 2020-12-27 10:59:43
 */
@Data
public class SystemListVO implements Serializable {

	private static final long serialVersionUID = -2760156351359473263L;

	/**
	 * 系统平台ID
	 */
	private Long sysId;

	/**
	 * 系统编码
	 */
	private String sysCode;

	/**
	 * 系统名称
	 */
	private String sysName;

	/**
	 * 排序，数字越小排在越前面
	 */
	private Integer sortNum;

	/**
	 * 系统权限编码
	 */
	private String sysGrantCode;

	/**
	 * 系统链接
	 */
	private String sysUrl;

	/**
	 * 系统图标
	 */
	private String sysIcon;

	/**
	 * 系统环境 0-测试 1-beta 2-生产
	 */
	private Integer sysEnv;

	/**
	 * 状态 0-正常;1-停用
	 */
	private Integer status;

	/**
	 * 备注
	 */
	private String remarks;

	/**
	 * 签名类型
	 */
	private Integer signType;

	/**
	 * 网关验签公钥
	 */
	private String publicKey;

	private String createBy;

	/**
	 * 创建时间
	 */
	private String createTime;

	private String updateBy;

	/**
	 * 更新时间
	 */
	private String updateTime;

}
