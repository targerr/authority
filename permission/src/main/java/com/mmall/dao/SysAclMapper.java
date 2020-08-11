package com.mmall.dao;

import com.mmall.beans.PageQuery;
import com.mmall.model.SysAcl;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysAclMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysAcl record);

    int insertSelective(SysAcl record);

    SysAcl selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysAcl record);

    int updateByPrimaryKey(SysAcl record);

    int countByNameAndParentId(@Param("parentId") Integer parentId,@Param("name") String name,@Param("id") Integer id);

    int countByNameAndAclModuleId(@Param("aclModuleId") int aclModuleId, @Param("name") String name, @Param("id") Integer id);

    int countByAclModuleId(@Param("aclModuleId") int aclModuleId);

    List<SysAcl> getPageByAclModuleId(@Param("aclModuleId")int aclModuleId,@Param("page") PageQuery page);

    List<SysAcl> findAll();

    List<SysAcl> findByUserId(int userId);

    List<SysAcl> getByIdList(@Param("userAclIdList") List<Integer> userAclIdList);

    List<SysAcl> getByUrl(@Param("url") String url);
}