package com.spring.security.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.captcha.ICaptcha;
import com.spring.security.common.constants.SecurityConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @ClassName CodeController
 * @Description
 * @Author quan
 * @Date 2022/5/27 11:38
 * @Version 1.0
 */
@RestController
@Slf4j
public class CodeController {

    @GetMapping(SecurityConstants.DEFAULT_VALIDATE_CODE_URL_PREFIX + "/{type}")
    public void createCode(HttpSession session, HttpServletResponse response,
                           @PathVariable String type) throws Exception {
        /**
         * ServletWebRequest是一个包装类，
         * 如果参数为new ServletWebRequest(request, response)
         * 可以直接用ServletWebRequest request 去接，
         * 省去了写response对象的麻烦，如果想要再获取response对象，只需要用request.getResponse()方法就可以
         */
        //定义图形验证码的长、宽、验证码字符数、干扰元素个数
        CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(200,100);
        String code = captcha.getCode();
        log.info("验证码数字,code:{}", code);
        session.setAttribute("vf",code);
        captcha.write(response.getOutputStream());
    }


}
