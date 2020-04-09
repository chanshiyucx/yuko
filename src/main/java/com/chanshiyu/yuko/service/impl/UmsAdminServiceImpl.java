package com.chanshiyu.yuko.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chanshiyu.yuko.entity.UmsAdmin;
import com.chanshiyu.yuko.entity.UmsPermission;
import com.chanshiyu.yuko.mapper.UmsAdminMapper;
import com.chanshiyu.yuko.mapper.UmsRolePermissionRelationMapper;
import com.chanshiyu.yuko.service.IUmsAdminService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author SHIYU
 * @since 2020-04-09
 */
@Service
public class UmsAdminServiceImpl extends ServiceImpl<UmsAdminMapper, UmsAdmin> implements IUmsAdminService {

    private final UmsAdminMapper umsAdminMapper;

    private final UmsRolePermissionRelationMapper umsRolePermissionRelationMapper;

    public UmsAdminServiceImpl(UmsAdminMapper umsAdminMapper, UmsRolePermissionRelationMapper umsRolePermissionRelationMapper) {
        this.umsAdminMapper = umsAdminMapper;
        this.umsRolePermissionRelationMapper = umsRolePermissionRelationMapper;
    }

    @Override
    public UmsAdmin getAdminByUsername(String username) {
        QueryWrapper<UmsAdmin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return umsAdminMapper.selectOne(queryWrapper);
    }

    @Override
    public List<UmsPermission> getPermissionList(Integer id) {
        return umsAdminMapper.getPermissionList(id);
    }

    @Override
    public String refreshToken(HttpServletRequest request) {
        return null;
    }
}
