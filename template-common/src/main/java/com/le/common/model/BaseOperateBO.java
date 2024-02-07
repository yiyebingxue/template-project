package com.le.common.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 操作基础BO
 *
 * @author Bruce Lu
 */
@Data
public class BaseOperateBO implements Serializable {

	private static final long serialVersionUID = 4316038518168512167L;

	private String operateBy;

}
