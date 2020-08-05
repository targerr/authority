package com.mmall.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Wgs
 * @version 1.0
 * @create：2020/08/03
 */
@RestController
public class TestController {
    @GetMapping("/index")
    public String index() {
        return "Hello index";
    }

    @GetMapping("/set")
    public String set(HttpServletResponse response, HttpServletRequest request) {
        request.getSession().setAttribute("haha", "1314114");
        return "Hello index";
    }
}
