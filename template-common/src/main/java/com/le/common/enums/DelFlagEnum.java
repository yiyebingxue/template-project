package com.le.common.enums;

import lombok.Getter;

/**
 * 数据删除状态
 *
 * @author Bruce Lu
 */
@Getter
public enum DelFlagEnum {

    OK(0, "正常"),
    DELETED(1, "删除");

    private Integer status;
    private String desc;

    DelFlagEnum(Integer status, String desc) {
	this.status = status;
	this.desc = desc;
    }

}
