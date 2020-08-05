package com.mmall.service;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
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
}
