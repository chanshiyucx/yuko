package com.chanshiyu.yuko.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chanshiyu.yuko.entity.UmsAdmin;
import com.chanshiyu.yuko.entity.UmsPermission;
import com.chanshiyu.yuko.model.vo.UmsAdminVO;
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

    /**
     * 获取分页用户
     */
    IPage<UmsAdminVO> getAdminList(Page page);

}
