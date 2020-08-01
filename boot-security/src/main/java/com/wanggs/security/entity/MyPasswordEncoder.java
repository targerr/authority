package com.wanggs.security.entity;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Wgs
 * @version 1.0
 * @create：2020/08/01
 */
public class MyPasswordEncoder implements PasswordEncoder {
    // 盐值
    private final static String SALT = "123456";
    @Override
    public String encode(CharSequence rawPassword) {
        // 使用Md5加密
        Md5PasswordEncoder encoder = new Md5PasswordEncoder();
        return encoder.encodePassword(rawPassword.toString(), SALT);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        Md5PasswordEncoder encoder = new Md5PasswordEncoder();
        return encoder.isPasswordValid(encodedPassword, rawPassword.toString(), SALT);
    }
}
