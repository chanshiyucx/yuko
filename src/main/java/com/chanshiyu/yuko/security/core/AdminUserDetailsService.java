package com.chanshiyu.yuko.security.core;

import com.chanshiyu.yuko.service.IUmsAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author SHIYU
 * @description SpringSecurity 核心接口，根据用户名获取用户信息
 * @since 2020/4/9 17:07
 */
public class AdminUserDetailsService implements UserDetailsService {

    @Autowired
    private IUmsAdminService umsAdminService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return umsAdminService.loadUserByUsername(username);
    }

}
