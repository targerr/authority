package com.wanggs.shiro.service;

import com.wanggs.shiro.model.User;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by Wgs on 2020/8/2.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
class UserServiceTest {

    @Autowired
    private UserService userService;
    @Test
    void findByUserName() {

        User tom = userService.findByUserName("demo");
        System.out.println("user: " + tom);


    }
}