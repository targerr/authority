package com.wanggs.shiro.mappers;

import com.wanggs.shiro.model.User;
import org.apache.ibatis.annotations.Param;

/**
 * @author Wgs
 * @version 1.0
 * @create：2020/08/01
 */
public interface UserMapper {
    User findByUserName(@Param("userName") String userName);
}
