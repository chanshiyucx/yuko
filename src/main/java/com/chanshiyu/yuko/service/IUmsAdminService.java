package com.chanshiyu.yuko.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chanshiyu.yuko.entity.UmsAdmin;
import com.chanshiyu.yuko.entity.UmsPermission;
import com.chanshiyu.yuko.model.params.UmsAdminParam;
import com.chanshiyu.yuko.model.params.UmsUpdateAdminPasswordParam;
import com.chanshiyu.yuko.model.vo.CommonPage;
import com.chanshiyu.yuko.model.vo.CommonResult;
import com.chanshiyu.yuko.model.vo.UmsAdminVO;
import org.springframework.security.core.userdetails.UserDetails;

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
     * 注册功能
     */
    UmsAdmin register(UmsAdminParam umsAdminParam);

    /**
     * 登录功能
     *
     * @param username 用户名
     * @param password 密码
     * @return 生成的JWT的token
     */
    UmsAdminVO login(String username, String password);

    /**
     * 更新用户
     */
    UmsAdmin update(UmsAdminParam umsAdminParam);

    /**
     * 删除用户
     */
    int delete(Integer id);

    /**
     * 分页获取用户列表
     */
    CommonPage<UmsAdminVO> list(Integer pageNum, Integer pageSize);

    /**
     * 更新密码
     */
    void updatePassword(UmsUpdateAdminPasswordParam umsUpdateAdminPasswordParam);

    /**
     * 获取用户所有权限
     */
    List<UmsPermission> getPermissionList(Integer adminId);

    /**
     * 刷新 token
     */
    String refreshToken(String oldToken);

    /**
     * 获取用户信息
     */
    UserDetails loadUserByUsername(String username);

}
