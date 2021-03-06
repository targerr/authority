package com.mmall.dao;

import com.mmall.model.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysRoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysRole record);

    int insertSelective(SysRole record);

    SysRole selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRole record);

    int updateByPrimaryKey(SysRole record);

    List<SysRole> getByIdList(@Param("roleIdList") List<Integer> roleIdList);

    int checkExist(@Param("id") Integer id,@Param("name") String name);

    List<SysRole> findAll();
}