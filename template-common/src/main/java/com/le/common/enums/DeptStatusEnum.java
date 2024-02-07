package com.le.common.enums;

import lombok.Getter;

/**
 * 部门状态-枚举
 *
 * @author Bruce Lu
 */
@Getter
public enum DeptStatusEnum {

    OK(0, "正常"),
    DISABLE(1, "停用");

    private Integer status;
    private String desc;

    DeptStatusEnum(Integer status, String desc) {
	this.status = status;
	this.desc = desc;
    }

}
