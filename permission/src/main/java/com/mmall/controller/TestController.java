package com.mmall.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Wgs
 * @version 1.0
 * @create：2020/08/03
 */
@RestController
public class TestController {
    @GetMapping("/index")
    public String index() {
        int i = 0;
       int j =  10/i;
        return "Hello index";
    }
}
