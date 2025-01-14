package com.spring.security.controller;

import com.spring.security.common.entity.JsonResult;
import com.spring.security.common.utils.ResultTool;
import com.spring.security.entity.SysUser;
import com.spring.security.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: Hutengfei
 * @Description:
 * @Date Create in 2019/8/28 19:34
 */
@RestController
public class UserController {

    @Resource
    private SysUserService sysUserService;

    @GetMapping("/getUser")
    public JsonResult<List<SysUser>> getUser() {
        List<SysUser> users = sysUserService.queryAllByLimit(1, 100);
        return ResultTool.success(users);
    }

    @GetMapping("/test")
    public JsonResult<String> test() {
        return ResultTool.success("hello world");
    }
}
