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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

    public boolean hasUrlAcl(String url) {
        if (isSuperAdmin()){
            return true;
        }
        // 获取url权限
        List<SysAcl> sysAclList = sysAclMapper.getByUrl(url);
        if (CollectionUtils.isEmpty(sysAclList)) {
            return true;
        }
        // 获取当前用户权限
        List<SysAcl> sysAcls = getCurrentUserAclList();
        List<Integer> sysAclIdList = sysAcls.stream().map(SysAcl::getId).collect(Collectors.toList());
        // 规则：只要有一个权限点有权限，那么我们就认为有访问权限
        boolean hasValidAcl = false;
        for (SysAcl sysAcl : sysAclList){
            // 判断一个用户是否具有某个权限点的访问权限
            // 权限点无效
            if (sysAcl == null || sysAcl.getStatus() != 1){
                continue;
            }
            hasValidAcl = true;
            if (sysAclIdList.contains(sysAcl.getId())){
                return true;
            }
        }
        if (!hasValidAcl){
            return true;
        }

        return false;
    }
}
