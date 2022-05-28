package com.spring.security.config.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @ClassName CodeException
 * @Description
 * @Author quan
 * @Date 2022/5/27 18:19
 * @Version 1.0
 */
public class CodeException extends AuthenticationException {


    public CodeException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public CodeException(String msg) {
        super(msg);
    }
}
