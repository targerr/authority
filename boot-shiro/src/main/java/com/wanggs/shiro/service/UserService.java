package com.wanggs.shiro.service;

import com.wanggs.shiro.model.User;

/**
 * @author Wgs
 * @version 1.0
 * @create：2020/08/01
 */
public interface UserService {
    public abstract User findByUserName(String userName);
}
