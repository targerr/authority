package com.mmall.service;

import com.google.common.collect.Lists;
import com.mmall.common.RequestHolder;
import com.mmall.dao.SysRoleUserMapper;
import com.mmall.dao.SysUserMapper;
import com.mmall.model.SysRoleUser;
import com.mmall.model.SysUser;
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
public class SysRoleUserService {
    @Resource
    private SysRoleUserMapper sysRoleUserMapper;
    @Resource
    private SysUserMapper sysUserMapper;

    public void changeRoleUsers(int roleId, List<Integer> userIdList) {
        updateRoleUser(roleId, userIdList);
    }

    @Transactional
    public void updateRoleUser(int roleId, List<Integer> userIdList) {
        sysRoleUserMapper.delByRoleId(roleId);

        if (CollectionUtils.isEmpty(userIdList)) {
            return;
        }
        List<SysRoleUser> roleUserList = Lists.newArrayList();
        for (Integer userId : userIdList) {
            SysRoleUser roleUser = SysRoleUser.builder().roleId(roleId).userId(userId).operator(RequestHolder.getUser().getUsername())
                    .operateIp(IpUtil.getRemoteIp(RequestHolder.getRequest())).operateTime(new Date()).build();
            roleUserList.add(roleUser);
            sysRoleUserMapper.insert(roleUser);
        }
    }

    public List<SysUser> getListByRoleId(int roleId) {
        List<Integer> userIdList = sysRoleUserMapper.getUserIdListByRoleId(roleId);
        if (CollectionUtils.isEmpty(userIdList)) {
            return Lists.newArrayList();
        }
        return sysUserMapper.getByIdList(userIdList);
    }
}
