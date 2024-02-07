/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.le.dao.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 部门实体类
 *
 * @author Bruce Lu
 * @date 2020-12-27 18:07:20
 */
@Data
public class SsoDept implements Serializable {

    private static final long serialVersionUID = 4501341919143820458L;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 系统编码
     */
    private String sysCode;

    /**
     * 父部门ID
     */
    private Long deptParentId;

    /**
     * 顺序
     */
    private Integer sortNum;

    /**
     * 状态 0-正常;1-停用
     */
    private Integer status;

    /**
     * 删除标志 0-正常;1-删除
     */
    private Integer delFlag;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 最后修改者
     */
    private String updateBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;


}
