package com.chanshiyu.yuko.security.core;

import com.chanshiyu.yuko.service.IUmsAdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

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
        return umsAdminService.loadUserByUsername(username);
    }

}
