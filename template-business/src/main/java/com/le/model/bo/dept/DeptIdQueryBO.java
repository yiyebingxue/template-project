package com.le.model.bo.dept;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 部门详情bo
 *
 * @author Bruce Lu
 */
@Data
public class DeptIdQueryBO implements Serializable {

    private static final long serialVersionUID = -5367920763344359224L;

    /**
     * 部门ID
     */
    @NotNull(message = "部门ID不为空")
    private Long deptId;

}
