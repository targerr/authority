package com.mmall.controller;

import com.mmall.common.JsonData;
import com.mmall.dao.SysDeptMapper;
import com.mmall.param.UserParam;
import com.mmall.service.SysUserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @author Wgs
 * @version 1.0
 * @create：2020/08/05
 */
@Controller
@RequestMapping("sys/user")
public class SysUserController {
    @Resource
    private SysUserService sysUserService;


    @RequestMapping("/save.json")
    @ResponseBody
    public JsonData saveUser(UserParam userParam) {
        sysUserService.save(userParam);
        return JsonData.success();
    }
}
