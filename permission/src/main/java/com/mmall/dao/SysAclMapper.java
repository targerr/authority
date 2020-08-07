package com.mmall.dao;

import com.mmall.model.SysAcl;
import org.apache.ibatis.annotations.Param;

public interface SysAclMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysAcl record);

    int insertSelective(SysAcl record);

    SysAcl selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysAcl record);

    int updateByPrimaryKey(SysAcl record);

    int countByNameAndParentId(@Param("parentId") Integer parentId,@Param("name") String name,@Param("id") Integer id);
}