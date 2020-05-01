package com.chanshiyu.yuko.security.component;

import cn.hutool.json.JSONUtil;
import com.chanshiyu.yuko.model.vo.CommonResult;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author SHIYU
 * @description 当访问接口没有权限时，自定义的返回结果
 * @since 2020/4/9 14:53
 */
public class RestfulAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
        httpServletResponse.getWriter().println(JSONUtil.parse(CommonResult.forbidden(e.getMessage())));
        httpServletResponse.getWriter().flush();
    }

}
