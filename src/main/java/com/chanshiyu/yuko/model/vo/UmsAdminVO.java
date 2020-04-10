package com.chanshiyu.yuko.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author SHIYU
 * @description 后台用户信息
 * @since 2020/4/10 9:43
 */
@Data
public class UmsAdminVO {

    private Long id;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("昵称")
    private String nickName;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "备注信息")
    private String note;

    @ApiModelProperty(value = "启用状态<0:禁用,1:启用>")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "角色")
    private List<Integer> roleIds;

    @ApiModelProperty(value = "权限")
    private List<Integer> permissionIds;

    @ApiModelProperty(value = "Token")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String token;

}
