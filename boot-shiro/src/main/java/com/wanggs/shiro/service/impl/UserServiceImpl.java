package com.wanggs.shiro.service.impl;

import com.wanggs.shiro.mappers.UserMapper;
import com.wanggs.shiro.model.User;
import com.wanggs.shiro.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Wgs
 * @version 1.0
 * @create：2020/08/02
 */
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;
    @Override
    public User findByUserName(String userName) {
        return userMapper.findByUserName(userName);
    }
}
