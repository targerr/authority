package com.mmall.service;

import com.google.common.collect.Lists;
import com.mmall.common.RequestHolder;
import com.mmall.dao.SysAclMapper;
import com.mmall.dao.SysRoleAclMapper;
import com.mmall.dao.SysRoleUserMapper;
import com.mmall.model.SysAcl;
import com.mmall.model.SysUser;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Wgs
 * @version 1.0
 * @create：2020/08/07
 */
@Service
public class SysCoreService {
    @Resource
    private SysAclMapper sysAclMapper;
    @Resource
    private SysRoleUserMapper sysRoleUserMapper;
    @Resource
    private SysRoleAclMapper sysRoleAclMapper;

    public List<SysAcl> getCurrentUserAclList() {
        int userId = RequestHolder.getUser().getId();
        return getUserAclList(userId);
    }

    private List<SysAcl> getUserAclList(int userId) {
        // 超级管理员,查询所有
        if (isSuperAdmin()) {
            return sysAclMapper.findAll();
        }
//        return sysAclMapper.findByUserId(userId);
        return getUserAcls(userId);
    }

    public List<SysAcl> getUserAcls(int userId) {
        List<Integer> userRoleIdList = sysRoleUserMapper.getRoleIdListByUserId(userId);
        if (CollectionUtils.isEmpty(userRoleIdList)) {
            return Lists.newArrayList();
        }
        List<Integer> userAclIdList = sysRoleAclMapper.getAclIdListByRoleIdList(userRoleIdList);
        if (CollectionUtils.isEmpty(userAclIdList)) {
            return Lists.newArrayList();
        }
        return sysAclMapper.getByIdList(userAclIdList);
    }

    private boolean isSuperAdmin() {
        // 这里是我自己定义了一个假的超级管理员规则，实际中要根据项目进行修改
        // 可以是配置文件获取，可以指定某个用户，也可以指定某个角色
        SysUser sysUser = RequestHolder.getUser();
        if (sysUser.getMail().contains("admin")) {
            return true;
        }
        return false;
    }

    public List<SysAcl> getRoleAclList(int roleId) {
        List<Integer> aclIdList = sysRoleAclMapper.getAclIdListByRoleIdList(Lists.<Integer>newArrayList(roleId));
        if (CollectionUtils.isEmpty(aclIdList)) {
            return Lists.newArrayList();
        }
        return sysAclMapper.getByIdList(aclIdList);
    }
}
