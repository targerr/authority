package com.mmall.dao;

import com.mmall.model.SysUser;
import org.apache.ibatis.annotations.Param;

public interface SysUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    /**
     * 校验手机号是否存在
     * @param id
     * @param telephone
     * @return
     */
    int countByTelePhone(@Param("id") Integer id,@Param("telephone") String telephone);

    /**
     * 校验邮箱是否使用
     * @param id
     * @param mail
     * @return
     */
    int countByMail(@Param("id") Integer id,@Param("mail") String mail);
}