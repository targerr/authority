package com.mmall.controller;

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
    @RequestMapping("/login.page")
    public void login(HttpServletRequest request, HttpServletResponse response) {
        String path = "signin.jsp";
        try {
            request.getRequestDispatcher(path).forward(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
