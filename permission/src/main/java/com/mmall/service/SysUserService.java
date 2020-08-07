package com.mmall.service;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import com.google.common.base.Preconditions;
import com.mmall.beans.PageQuery;
import com.mmall.beans.PageResult;
import com.mmall.dao.SysDeptMapper;
import com.mmall.dao.SysUserMapper;
import com.mmall.exception.ParamException;
import com.mmall.model.SysDept;
import com.mmall.model.SysUser;
import com.mmall.param.UserParam;
import com.mmall.util.BeanValidator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author Wgs
 * @version 1.0
 * @create：2020/08/05
 */
@Service
public class SysUserService {
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysDeptMapper sysDeptMapper;

    /**
     * 保存系统用户
     *
     * @param userParam
     */
    public void save(UserParam userParam) {
        // 校验参数
        BeanValidator.check(userParam);
        // 验证手机号
        if (checkPhoneExist(userParam)) {
            throw new ParamException("该手机号码已经存在");
        }
        // 验证邮箱
        if (checkEmailExist(userParam)) {
            throw new ParamException("该邮箱已经存在,无法使用");
        }
        if (checkDeptExist(userParam)) {
            throw new ParamException("部门不存在,无法使用");
        }
        String password = RandomUtil.randomString(6);
        password = "123456";
        String encryptedPassword = SecureUtil.md5(password);
        SysUser user = SysUser.builder().username(userParam.getUsername()).telephone(userParam.getTelephone()).mail(userParam.getMail())
                .password(encryptedPassword).deptId(userParam.getDeptId()).status(userParam.getStatus()).remark(userParam.getRemark()).build();
        user.setOperator("admin");
        user.setOperateIp("127.0.0.1");
        user.setOperateTime(new Date());
        // TODO: sendEmail

        sysUserMapper.insertSelective(user);
    }

    public void update(UserParam param) {
        BeanValidator.check(param);
        if (checkPhoneExist(param)) {
            throw new ParamException("电话已被占用");
        }
        if (checkEmailExist(param)) {
            throw new ParamException("邮箱已被占用");
        }
        if (checkDeptExist(param)) {
            throw new ParamException("部门不存在,无法使用");
        }

        SysUser before = sysUserMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "待更新的用户不存在");
        SysUser after = SysUser.builder().id(param.getId()).username(param.getUsername()).telephone(param.getTelephone()).mail(param.getMail())
                .deptId(param.getDeptId()).status(param.getStatus()).remark(param.getRemark()).build();
        after.setOperator("admin");
        after.setOperateIp("127.0.0.10.0.1");
        after.setOperateTime(new Date());
        sysUserMapper.updateByPrimaryKeySelective(after);
    }

    private boolean checkDeptExist(UserParam userParam) {
        SysDept sysDept = sysDeptMapper.selectByPrimaryKey(userParam.getDeptId());
        return sysDept == null;
    }

    private boolean checkEmailExist(UserParam param) {
        return sysUserMapper.countByMail(param.getId(), param.getMail()) > 0;
    }

    private boolean checkPhoneExist(UserParam param) {
        return sysUserMapper.countByTelePhone(param.getId(), param.getTelephone()) > 0;
    }

    public PageResult<SysUser> getPageByDeptId(int deptId, PageQuery page) {
        BeanValidator.check(page);
        int count = sysUserMapper.countByDeptId(deptId);
        if (count > 0) {
            List<SysUser> list = sysUserMapper.getPageByDeptId(deptId, page);
            return PageResult.<SysUser>builder().total(count).data(list).build();
        }
        return PageResult.<SysUser>builder().build();
    }

    public SysUser findByUserName(String username) {
       return sysUserMapper.findByUserName(username);
    }

    public List<SysUser> getAll() {
        return sysUserMapper.findAll();
    }
}
