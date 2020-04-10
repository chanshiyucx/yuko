package com.chanshiyu.yuko.model.params;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author SHIYU
 * @description 更新用户密码参数
 * @since 2020/4/10 10:05
 */
@Data
public class UmsUpdateAdminPasswordParam {

    @ApiModelProperty(value = "用户名", required = true)
    private String username;

    @ApiModelProperty(value = "旧密码", required = true)
    private String oldPassword;

    @ApiModelProperty(value = "新密码", required = true)
    private String newPassword;

}
