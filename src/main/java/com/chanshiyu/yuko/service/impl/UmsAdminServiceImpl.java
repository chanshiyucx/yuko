package com.chanshiyu.yuko.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chanshiyu.yuko.entity.UmsAdmin;
import com.chanshiyu.yuko.entity.UmsAdminLoginLog;
import com.chanshiyu.yuko.entity.UmsAdminRoleRelation;
import com.chanshiyu.yuko.entity.UmsPermission;
import com.chanshiyu.yuko.exception.BadRequestException;
import com.chanshiyu.yuko.mapper.UmsAdminLoginLogMapper;
import com.chanshiyu.yuko.mapper.UmsAdminMapper;
import com.chanshiyu.yuko.mapper.UmsAdminRoleRelationMapper;
import com.chanshiyu.yuko.model.params.UmsAdminParam;
import com.chanshiyu.yuko.model.params.UmsUpdateAdminPasswordParam;
import com.chanshiyu.yuko.model.vo.CommonPage;
import com.chanshiyu.yuko.model.vo.UmsAdminVO;
import com.chanshiyu.yuko.security.core.AdminUserDetails;
import com.chanshiyu.yuko.service.IUmsAdminRoleRelationService;
import com.chanshiyu.yuko.service.IUmsAdminService;
import com.chanshiyu.yuko.utils.IpUtil;
import com.chanshiyu.yuko.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author SHIYU
 * @since 2020-04-09
 */
@Service
@RequiredArgsConstructor
public class UmsAdminServiceImpl extends ServiceImpl<UmsAdminMapper, UmsAdmin> implements IUmsAdminService {

    private final UmsAdminMapper umsAdminMapper;

    private final UmsAdminRoleRelationMapper umsAdminRoleRelationMapper;

    private final UmsAdminLoginLogMapper umsAdminLoginLogMapper;

    private final IUmsAdminRoleRelationService umsAdminRoleRelationService;

    private final JwtTokenUtil jwtTokenUtil;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UmsAdmin getAdminByUsername(String username) {
        LambdaQueryWrapper<UmsAdmin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UmsAdmin::getUsername, username);
        return umsAdminMapper.selectOne(queryWrapper);
    }

    @Override
    public UmsAdmin register(UmsAdminParam umsAdminParam) {
        UmsAdmin umsAdmin = new UmsAdmin();
        BeanUtils.copyProperties(umsAdminParam, umsAdmin);
        umsAdmin.setCreateTime(LocalDateTime.now());
        umsAdmin.setStatus(1);
        // 查询是否有相同用户名的用户
        UmsAdmin result = getAdminByUsername(umsAdmin.getUsername());
        if (result != null) {
            throw new BadRequestException("该用户已存在");
        }
        // 将密码进行加密操作
        String encodePassword = passwordEncoder.encode(umsAdmin.getPassword());
        umsAdmin.setPassword(encodePassword);
        umsAdminMapper.insert(umsAdmin);
        // 更新用户角色关系表
        updateRole(umsAdmin.getId(), umsAdminParam.getRoleIds());
        return umsAdmin;
    }

    @Override
    public UmsAdminVO login(String username, String password) {
        // 密码需要客户端加密后传递
        org.springframework.security.core.userdetails.UserDetails userDetails = loadUserByUsername(username);
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("密码不正确");
        }
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenUtil.generateToken(userDetails);
        // 获取用户详细信息
        UmsAdmin umsAdmin = getAdminByUsername(username);
        UmsAdminVO umsAdminVO = new UmsAdminVO();
        BeanUtils.copyProperties(umsAdmin, umsAdminVO);
        umsAdminVO.setToken(token);
        List<Integer> permissionIds = getPermissionList(umsAdmin.getId()).stream()
                .map(UmsPermission::getId)
                .collect(Collectors.toList());
        umsAdminVO.setPermissionIds(permissionIds);
        // 写入登录日志
        insertLoginLog(username);
        return umsAdminVO;
    }

    @Override
    public UmsAdmin update(UmsAdminParam umsAdminParam) {
        UmsAdmin umsAdmin = new UmsAdmin();
        BeanUtils.copyProperties(umsAdminParam, umsAdmin);
        // 如果密码不为空
        if (StringUtils.isNotBlank(umsAdmin.getPassword())) {
            String encodePassword = passwordEncoder.encode(umsAdmin.getPassword());
            umsAdmin.setPassword(encodePassword);
        }
        umsAdminMapper.updateById(umsAdmin);
        // 更新用户角色关系表
        updateRole(umsAdmin.getId(), umsAdminParam.getRoleIds());
        return umsAdmin;
    }

    @Override
    public int delete(Integer id) {
        return umsAdminMapper.deleteById(id);
    }

    @Override
    public CommonPage<UmsAdminVO> list(Integer pageNum, Integer pageSize) {
        Page<UmsAdmin> page = new Page<>(pageNum, pageSize);
        IPage<UmsAdminVO> result = umsAdminMapper.getAdminList(page);
        return new CommonPage<>(result);
    }

    @Override
    public void updatePassword(UmsUpdateAdminPasswordParam umsUpdateAdminPasswordParam) {
        UmsAdmin umsAdmin = getAdminByUsername(umsUpdateAdminPasswordParam.getUsername());
        if (umsAdmin == null) {
            throw new BadRequestException("用户不存在");
        }
        if (!passwordEncoder.matches(umsUpdateAdminPasswordParam.getOldPassword(), umsAdmin.getPassword())) {
            throw new BadRequestException("旧密码错误");
        }
        umsAdmin.setPassword(passwordEncoder.encode(umsUpdateAdminPasswordParam.getNewPassword()));
        umsAdminMapper.updateById(umsAdmin);
    }

    @Override
    public List<UmsPermission> getPermissionList(Integer adminId) {
        return umsAdminMapper.getPermissionList(adminId);
    }

    @Override
    public String refreshToken(String oldToken) {
        return jwtTokenUtil.refreshHeadToken(oldToken);
    }

    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username) {
        UmsAdmin admin = getAdminByUsername(username);
        if (admin == null) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        List<UmsPermission> permissionList = getPermissionList(admin.getId());
        return new AdminUserDetails(admin, permissionList);
    }

    private void insertLoginLog(String username) {
        UmsAdmin admin = getAdminByUsername(username);
        if (admin == null) return;
        UmsAdminLoginLog loginLog = new UmsAdminLoginLog();
        loginLog.setAdminId(admin.getId());
        loginLog.setCreateTime(LocalDateTime.now());
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        loginLog.setIp(IpUtil.getIpAddr(request));
        loginLog.setUserAgent(request.getHeader("User-Agent"));
        umsAdminLoginLogMapper.insert(loginLog);
    }

    private void updateRole(Integer adminId, List<Integer> roleIds) {
        // 先删除原有关系
        LambdaQueryWrapper<UmsAdminRoleRelation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UmsAdminRoleRelation::getAdminId, adminId);
        umsAdminRoleRelationMapper.delete(queryWrapper);
        // 批量插入新关系
        List<UmsAdminRoleRelation> relationList = roleIds.stream()
                .map(roleId -> {
                    UmsAdminRoleRelation relation = new UmsAdminRoleRelation();
                    relation.setAdminId(adminId);
                    relation.setRoleId(roleId);
                    return relation;
                }).collect(Collectors.toList());
        umsAdminRoleRelationService.saveBatch(relationList);
    }

}
