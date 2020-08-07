package com.mmall.service;

import com.google.common.collect.Lists;
import com.mmall.common.RequestHolder;
import com.mmall.dao.SysRoleAclMapper;
import com.mmall.model.SysRoleAcl;
import com.mmall.util.IpUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author Wgs
 * @version 1.0
 * @create：2020/08/07
 */
@Service
public class SysRoleAclService {
    @Resource
    private SysRoleAclMapper sysRoleAclMapper;
    public void changeRoleAcls(int roleId, List<Integer> aclIdList) {
        updateRoleAcls(roleId,aclIdList);
    }

    @Transactional
    public void updateRoleAcls(int roleId, List<Integer> aclIdList) {
        sysRoleAclMapper.deleteByRoleId(roleId);
        if (CollectionUtils.isEmpty(aclIdList)) {
            return;
        }
        List<SysRoleAcl> roleAclList = Lists.newArrayList();
        for(Integer aclId : aclIdList) {
            SysRoleAcl roleAcl = SysRoleAcl.builder().roleId(roleId).aclId(aclId).operator(RequestHolder.getUser().getUsername())
                    .operateIp(IpUtil.getRemoteIp(RequestHolder.getRequest())).operateTime(new Date()).build();
            roleAclList.add(roleAcl);
            sysRoleAclMapper.insertSelective(roleAcl);
        }
    }
}
