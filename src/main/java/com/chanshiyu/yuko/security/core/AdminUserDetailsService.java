package com.chanshiyu.yuko.security.core;

import com.chanshiyu.yuko.entity.UmsAdmin;
import com.chanshiyu.yuko.entity.UmsPermission;
import com.chanshiyu.yuko.service.IUmsAdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author SHIYU
 * @description SpringSecurity 核心接口，根据用户名获取用户信息
 * @since 2020/4/9 17:07
 */
@Slf4j
@Component
public class AdminUserDetailsService implements UserDetailsService {

    private final IUmsAdminService umsAdminService;

    public AdminUserDetailsService(IUmsAdminService umsAdminService) {
        this.umsAdminService = umsAdminService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UmsAdmin admin = umsAdminService.getAdminByUsername(username);
        if (admin == null) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        List<UmsPermission> permissionList = umsAdminService.getPermissionList(admin.getId());
        return new AdminUserDetails(admin, permissionList);
    }

}
