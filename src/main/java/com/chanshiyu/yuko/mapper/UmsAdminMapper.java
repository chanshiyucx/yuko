package com.chanshiyu.yuko.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chanshiyu.yuko.entity.UmsAdmin;
import com.chanshiyu.yuko.entity.UmsPermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author SHIYU
 * @since 2020-04-09
 */
public interface UmsAdminMapper extends BaseMapper<UmsAdmin> {

    /**
     * 获取用户所有权限
     */
    List<UmsPermission> getPermissionList(@Param("adminId") Integer adminId);

}
