package com.chanshiyu.yuko.service;

import com.chanshiyu.yuko.entity.UmsAdmin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chanshiyu.yuko.entity.UmsPermission;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author SHIYU
 * @since 2020-04-09
 */
public interface IUmsAdminService extends IService<UmsAdmin> {

    /**
     * 根据用户名获取后台管理员
     */
    UmsAdmin getAdminByUsername(String username);

    /**
     * 获取用户所有权限
     */
    List<UmsPermission> getPermissionList(Integer id);

    /**
     * 刷新 token
     */
    String refreshToken(HttpServletRequest request);

}
