package com.spring.security.config.provider;

import com.spring.security.config.exception.CodeException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName CodeProvider
 * @Description
 * @Author quan
 * @Date 2022/5/27 17:31
 * @Version 1.0
 */
public class CodeProvider extends DaoAuthenticationProvider {


    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails,
                                                  UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        //校验密码前先校验验证码
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String code = request.getParameter("code");
        String vf = (String)request.getSession().getAttribute("vf");
        if (code == null || !code.equals(vf)) {
            throw new CodeException("验证码错误");
        }
        //校验密码
        super.additionalAuthenticationChecks(userDetails, authentication);
    }
}
