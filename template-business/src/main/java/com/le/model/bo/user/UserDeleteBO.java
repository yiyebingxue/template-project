package com.le.model.bo.user;

import com.le.common.model.BaseOperateBO;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 用户删除bo
 *
 * @author Bruce Lu
 */
@Data
public class UserDeleteBO extends BaseOperateBO {

    private static final long serialVersionUID = -6260789437671185007L;

    /**
     * 系统编码
     */
    @NotBlank(message = "系统编码不能为空")
    private String sysCode;

    /**
     * 用户ID
     */
    @Size(min = 1, message = "用户ID不为空")
    private List<Long> userIdList;

}
