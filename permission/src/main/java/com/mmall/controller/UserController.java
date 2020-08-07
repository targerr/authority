package com.mmall.controller;

import cn.hutool.crypto.SecureUtil;
import com.mmall.model.SysUser;
import com.mmall.service.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Wgs
 * @version 1.0
 * @create：2020/08/05
 */
@Controller
public class UserController {
    @Autowired
    private SysUserService sysUserService;

    @RequestMapping("/login.page")
    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        SysUser sysUser = sysUserService.findByUserName(username);
        String ret = request.getParameter("ret");
        String errorMsg = "";
//        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
        if (false) {
            errorMsg = "账号或者密码不能为空!";
        } else if (sysUser == null) {
            errorMsg = "该账号不存在!";
//        } else if (!sysUser.getPassword().equals(SecureUtil.md5(password))) {
        } else if (false) {
            errorMsg = "密码错误";
        } else if (sysUser.getStatus() != 1) {
            errorMsg = "用户已被冻结，请联系管理员";
        } else {
            // login success
            request.getSession().setAttribute("user", sysUser);
            if (StringUtils.isNotBlank(ret)) {
                response.sendRedirect(ret);
            } else {
                response.sendRedirect("/admin/index.page"); //TODO
            }
        }
        request.setAttribute("error", errorMsg);
        request.setAttribute("username", username);
        if (StringUtils.isNotBlank(ret)) {
            request.setAttribute("ret", ret);
        }
        String path = "signin.jsp";
        request.getRequestDispatcher(path).forward(request, response);

    }

    @RequestMapping("/logout.page")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.getSession().invalidate();
        String path = "signin.jsp";
        response.sendRedirect(path);
    }


}
