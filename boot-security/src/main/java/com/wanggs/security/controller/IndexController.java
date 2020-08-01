package com.wanggs.security.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Wgs
 * @version 1.0
 * @create：2020/08/01
 */
@RestController
public class IndexController {
    @RequestMapping("/")
    public String index(){
        return "Spring Boot Security!";
    }
    @GetMapping("/hello")
    public String hello(){
        return "Spring Boot hello Security!";
    }
    // 指定权限访问
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/roleAuth")
    public String roleAuth(){
        return "Spring Boot roleAuth Security!";
    }
}
