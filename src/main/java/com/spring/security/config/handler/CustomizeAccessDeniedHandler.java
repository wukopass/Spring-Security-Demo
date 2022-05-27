package com.spring.security.config.handler;

import com.alibaba.fastjson.JSON;
import com.spring.security.common.entity.JsonResult;
import com.spring.security.common.enums.ResultCode;
import com.spring.security.common.utils.ResultTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

/**
 * @Author: Hutengfei
 * @Description: 权限拒绝处理逻辑
 * @Date Create in 2019/9/3 20:56
 */
@Component
@Slf4j
public class CustomizeAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest,
                       HttpServletResponse httpServletResponse,
                       AccessDeniedException e) throws IOException, ServletException {

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        String username = authentication.getName();
        Object principal = authentication.getPrincipal();
        Object credentials = authentication.getCredentials();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        log.info("查询到用户的,username:{},principal:{},credentials{},authorities:{}"
                , username, principal, credentials, authorities);
        httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
        JsonResult result = ResultTool.fail(ResultCode.NO_PERMISSION);
        httpServletResponse.setContentType("text/json;charset=utf-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(result));
    }
}
