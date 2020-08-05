package com.mmall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Wgs
 * @version 1.0
 * @create：2020/08/05
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @RequestMapping("index.page")
    public ModelAndView index() {
        return new ModelAndView("admin");
    }
}
