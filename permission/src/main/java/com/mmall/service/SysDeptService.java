package com.mmall.service;

import com.google.common.base.Preconditions;
import com.mmall.dao.SysDeptMapper;
import com.mmall.dao.SysLogMapper;
import com.mmall.exception.ParamException;
import com.mmall.model.SysDept;
import com.mmall.param.DeptParam;
import com.mmall.util.BeanValidator;
import com.mmall.util.JsonMapper;
import com.mmall.util.LevelUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author Wgs
 * @version 1.0
 * @create：2020/08/03
 */
@Service
public class SysDeptService {
    @Autowired
    private SysDeptMapper sysDeptMapper;
    @Autowired
    private SysLogMapper sysLogMapper;

    public void save(DeptParam param) {
        // 校验参数
        BeanValidator.check(param);
        if (checkExist(param.getParentId(), param.getName(), param.getId())) {
            throw new ParamException("同一层级下存在相同名称的部门");
        }
        SysDept dept = SysDept.builder().name(param.getName()).parentId(param.getParentId())
                .seq(param.getSeq()).remark(param.getRemark()).build();

        dept.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()), param.getParentId()));
        dept.setOperator("root");
        dept.setOperateIp("127.0.0.1");
        dept.setOperateTime(new Date());
        sysDeptMapper.insertSelective(dept);

    }

    public boolean checkExist(Integer parentId, String deptName, Integer deptId) {
        return sysDeptMapper.countByNameAndParentId(parentId, deptName, deptId) > 0;
    }

    public String getLevel(Integer deptId) {
        SysDept sysDept = sysDeptMapper.selectByPrimaryKey(deptId);
        return sysDept == null ? null : sysDept.getLevel();

    }

    public void update(DeptParam param) {
        // 校验参数
        BeanValidator.check(param);
        // 判断是否存在
        if (checkExist(param.getParentId(), param.getName(), param.getId())) {
            throw new ParamException("同一层级下存在相同名称的部门");
        }
        // 判断传入部门是否存在

        SysDept before = sysDeptMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "待更新部门不存在");
        if (checkExist(param.getParentId(), param.getName(), param.getId())) {
            throw new ParamException("同一层级下存在相同名称的部门!");
        }
        SysDept after = SysDept.builder().id(param.getId()).name(param.getName()).parentId(param.getParentId())
                .seq(param.getSeq()).remark(param.getRemark()).build();
        after.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()), param.getParentId()));
        after.setOperator("root");
        after.setOperateIp("127.0.0.1");
        after.setOperateTime(new Date());
        // 更新
        updateWithChild(before, after);
    }

    @Transactional
    void updateWithChild(SysDept before, SysDept after) {
        String newLevelPrefix = after.getLevel();
        String oldLevelPrefix = before.getLevel();
        if (!after.getLevel().equals(before.getLevel())) {
            List<SysDept> deptList = sysDeptMapper.getChildDeptListByLevel(before.getLevel());
            if (CollectionUtils.isNotEmpty(deptList)) {
                for (SysDept dept : deptList) {
                    String level = dept.getLevel();
                    if (level.indexOf(oldLevelPrefix) == 0) {
                        level = newLevelPrefix + level.substring(oldLevelPrefix.length());
                        dept.setLevel(level);
                    }
                }
                // sysDeptMapper.batchUpdateLevel(deptList);
                deptList.forEach(e -> {
                    sysDeptMapper.updateByPrimaryKey(e);
                });
            }
        }
        sysDeptMapper.updateByPrimaryKey(after);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void delete(Integer id) {
        // 判断是否存在
        SysDept sysDept = sysDeptMapper.selectByPrimaryKey(id);
        Preconditions.checkNotNull(sysDept, "删除部门不存在");
        // 判断是否存在子部门
        if (sysDeptMapper.countByParentId(id) > 0) {
            throw new ParamException("当前部门下面有子部门，无法删除");
        }
        //TODO 判断是否存在账号

        sysDeptMapper.deleteByPrimaryKey(id);
    }
}
