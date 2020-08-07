package com.mmall.service;

import com.google.common.base.Preconditions;
import com.mmall.dao.SysAclModuleMapper;
import com.mmall.exception.ParamException;
import com.mmall.model.SysAclModule;
import com.mmall.param.AclModuleParam;
import com.mmall.util.BeanValidator;
import com.mmall.util.LevelUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author Wgs
 * @version 1.0
 * @create：2020/08/05
 */
@Service
public class SysAclModuleService {
    @Resource
    private SysAclModuleMapper sysAclModuleMapper;

    public void save(AclModuleParam param) {
        // 校验属性
        BeanValidator.check(param);
        // 是否存在
        if (checkExist(param.getParentId(), param.getName(), param.getId())) {
            throw new ParamException("同一层级下存在相同名称的权限模块");
        }
        // 保存
        SysAclModule sysAclModule = SysAclModule.builder().name(param.getName()).parentId(param.getParentId())
                .seq(param.getSeq()).remark(param.getRemark()).status(param.getStatus()).build();
        sysAclModule.setOperateIp("127.0.0.1");
        sysAclModule.setOperator("user");
        sysAclModule.setOperateTime(new Date());
        sysAclModule.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()), param.getParentId()));

        sysAclModuleMapper.insertSelective(sysAclModule);
    }

    public void update(AclModuleParam param) {
        // 校验属性
        BeanValidator.check(param);
        // 是否c重复存在
        if (checkExist(param.getParentId(), param.getName(), param.getId())) {
            throw new ParamException("同一层级下存在相同名称的权限模块");
        }
        SysAclModule before = sysAclModuleMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "待更新的权限模块不存在");

        // 保存
        SysAclModule after = SysAclModule.builder().id(param.getId()).name(param.getName()).parentId(param.getParentId())
                .seq(param.getSeq()).remark(param.getRemark()).status(param.getStatus()).build();
        after.setOperateIp("127.0.0.1");
        after.setOperator("user");
        after.setOperateTime(new Date());
        after.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()), param.getParentId()));

        updateWithChild(before, after);

    }

    public void delete(Integer id) {
        // 该模板是否存在
        SysAclModule sysAclModule = sysAclModuleMapper.selectByPrimaryKey(id);
        Preconditions.checkNotNull(sysAclModule, "待删除权限模板不存在");
        // 是否有子模板
        if (checkParentId(id)) {
            throw new ParamException("该模板下有子权限模块,无法删除");
        }
        sysAclModuleMapper.deleteByPrimaryKey(id);

    }

    @Transactional
    public void updateWithChild(SysAclModule before, SysAclModule after) {
        String newLevelPrefix = after.getLevel();
        String oldLevelPrefix = before.getLevel();
        if (!newLevelPrefix.equals(oldLevelPrefix)) {
            List<SysAclModule> sysAclModuleList = sysAclModuleMapper.getChildAclModuleListByLevel(oldLevelPrefix);
            if (CollectionUtils.isNotEmpty(sysAclModuleList)) {
                for (SysAclModule sysAclModule : sysAclModuleList) {
                    String level = sysAclModule.getLevel();
                    // 包含关系
                    if (level.indexOf(oldLevelPrefix) == 0) {
                        level = newLevelPrefix + level.substring(oldLevelPrefix.length());
                        sysAclModule.setLevel(level);
                    }
                }

                sysAclModuleList.forEach(e -> sysAclModuleMapper.updateByPrimaryKey(e));
            }
        }

        sysAclModuleMapper.updateByPrimaryKey(after);
    }

    private boolean checkParentId(Integer id) {
        return sysAclModuleMapper.countByParentId(id) > 0;
    }

    private String getLevel(Integer parentId) {
        SysAclModule sysAclModule = sysAclModuleMapper.selectByPrimaryKey(parentId);
        return sysAclModule.getLevel();
    }

    private boolean checkExist(Integer parentId, String name, Integer id) {
        return sysAclModuleMapper.countByNameAndParentId(parentId, name, id) > 0;
    }


}
