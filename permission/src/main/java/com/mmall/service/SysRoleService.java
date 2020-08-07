package com.mmall.service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.mmall.dao.SysRoleAclMapper;
import com.mmall.dao.SysRoleMapper;
import com.mmall.dao.SysRoleUserMapper;
import com.mmall.dao.SysUserMapper;
import com.mmall.exception.ParamException;
import com.mmall.model.SysRole;
import com.mmall.model.SysUser;
import com.mmall.param.RoleParam;
import com.mmall.util.BeanValidator;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Wgs
 * @version 1.0
 * @create：2020/08/07
 */
@Service
public class SysRoleService {

    @Resource
    private SysRoleMapper sysRoleMapper;
    @Resource
    private SysRoleUserMapper sysRoleUserMapper;
    @Resource
    private SysRoleAclMapper sysRoleAclMapper;
    @Resource
    private SysUserMapper sysUserMapper;

    public List<SysRole> getRoleListByAclId(int aclId) {
        List<Integer> roleIdList = sysRoleAclMapper.getRoleIdListByAclId(aclId);
        if (CollectionUtils.isEmpty(roleIdList)) {
            return Lists.newArrayList();
        }
        return sysRoleMapper.getByIdList(roleIdList);
    }

    public List<SysUser> getUserListByRoleList(List<SysRole> roleList) {
        if (CollectionUtils.isEmpty(roleList)) {
            return Lists.newArrayList();
        }
        List<Integer> roleIdList = roleList.stream().map(role -> role.getId()).collect(Collectors.toList());
        List<Integer> userIdList = sysRoleUserMapper.getUserIdListByRoleIdList(roleIdList);
        if (CollectionUtils.isEmpty(userIdList)) {
            return Lists.newArrayList();
        }
        return sysUserMapper.getByIdList(userIdList);
    }

    /**
     * 保存角色
     *
     * @param param
     */
    public void save(RoleParam param) {
        // 校验参数
        BeanValidator.check(param);
        // 是否存在
        if (checkExist(param.getId(), param.getName())) {
            throw new ParamException("该角色已经存在,无法保存!");
        }
        //保存
        SysRole role = SysRole.builder().name(param.getName()).status(param.getStatus()).type(param.getType())
                .remark(param.getRemark()).build();
        role.setOperator("root");
        role.setOperateIp("127.0.0.1");
        role.setOperateTime(new Date());
        sysRoleMapper.insertSelective(role);
    }


    /**
     * 更新角色
     *
     * @param param
     */
    public void update(RoleParam param) {
        // 校验参数
        BeanValidator.check(param);
        // 是否存在该名称
        if (checkExist(param.getId(), param.getName())) {
            throw new ParamException("该角色名称已经存在,无法保存!");
        }
        //是否存在
        SysRole sysRole = sysRoleMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(sysRole, "该角色不存在,无法修改");
        //保存
        SysRole role = SysRole.builder().id(param.getId()).name(param.getName()).status(param.getStatus()).type(param.getType())
                .remark(param.getRemark()).build();
        role.setOperator("root");
        role.setOperateIp("127.0.0.1");
        role.setOperateTime(new Date());
        sysRoleMapper.updateByPrimaryKey(role);
    }

    /**
     * 删除
     *
     * @param id
     */
    public void delete(Integer id) {
        //是否存在
        SysRole sysRole = sysRoleMapper.selectByPrimaryKey(id);
        Preconditions.checkNotNull(sysRole, "该角色不存在,无法修改");
        sysRoleMapper.deleteByPrimaryKey(id);
    }

    public List<SysRole> getAllList(){
       return sysRoleMapper.findAll();
    }

    private boolean checkExist(Integer id, String name) {
        return sysRoleMapper.checkExist(id, name) > 0;
    }
}
