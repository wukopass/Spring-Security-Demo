package com.spring.security.config.handler;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName CodeFailureHandler
 * @Description
 * @Author quan
 * @Date 2022/5/27 16:13
 * @Version 1.0
 */
@Component
public class CodeFailureHandler extends SimpleUrlAuthenticationFailureHandler {


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String errorMsg = "用户名或者密码输入错误!";//返回的错误信息，默认是登录的错误

        //如果异常属于验证码session的异常，则获取异常的信息
        if (exception instanceof SessionAuthenticationException) {
            errorMsg = exception.getMessage();
        }
        //将返回的对象转换成json数据
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(JSON.toJSONString(errorMsg));
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(json);

    }
}
