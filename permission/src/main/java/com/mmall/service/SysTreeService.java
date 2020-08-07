package com.mmall.service;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.mmall.dao.SysAclMapper;
import com.mmall.dao.SysAclModuleMapper;
import com.mmall.dao.SysDeptMapper;
import com.mmall.dto.AclDto;
import com.mmall.dto.AclModuleLevelDto;
import com.mmall.dto.DeptLevelDto;
import com.mmall.model.SysAcl;
import com.mmall.model.SysAclModule;
import com.mmall.model.SysDept;
import com.mmall.util.LevelUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Wgs
 * @version 1.0
 * @create：2020/08/03
 */
@Service
public class SysTreeService {
    @Resource
    private SysDeptMapper sysDeptMapper;
    @Resource
    private SysAclModuleMapper sysAclModuleMapper;
    @Autowired
    private SysCoreService sysCoreService;
    @Resource
    private SysAclMapper sysAclMapper;

    public List<DeptLevelDto> deptTree() {
        List<SysDept> sysDepts = sysDeptMapper.getAllDept();
        if (CollectionUtils.isEmpty(sysDepts)) {
            return null;
        }
        List<DeptLevelDto> deptLevelDtoList = Lists.newArrayList();
        for (SysDept sysDept : sysDepts) {
            deptLevelDtoList.add(DeptLevelDto.adapt(sysDept));
        }
        return deptListToTree(deptLevelDtoList);
    }

    public List<DeptLevelDto> deptListToTree(List<DeptLevelDto> deptLevelList) {
        if (CollectionUtils.isEmpty(deptLevelList)) {
            return Lists.newArrayList();
        }
        // level -> [dept1, dept2, ...] Map<String, List<Object>>
        Multimap<String, DeptLevelDto> levelDeptMap = ArrayListMultimap.create();
        List<DeptLevelDto> rootList = Lists.newArrayList();

        for (DeptLevelDto dto : deptLevelList) {
            levelDeptMap.put(dto.getLevel(), dto);
            if (LevelUtil.ROOT.equals(dto.getLevel())) {
                rootList.add(dto);
            }
        }
        // 按照seq从小到大排序
        Collections.sort(rootList, new Comparator<DeptLevelDto>() {
            @Override
            public int compare(DeptLevelDto o1, DeptLevelDto o2) {
                return o1.getSeq() - o2.getSeq();
            }
        });
        // 递归生成树
        transformDeptTree(rootList, LevelUtil.ROOT, levelDeptMap);
        return rootList;
    }

    // level:0, 0, all 0->0.1,0.2
    // level:0.1
    // level:0.2
    public void transformDeptTree(List<DeptLevelDto> deptLevelList, String level, Multimap<String, DeptLevelDto> levelDeptMap) {
        for (int i = 0; i < deptLevelList.size(); i++) {
            // 遍历该层的每个元素
            DeptLevelDto deptLevelDto = deptLevelList.get(i);
            // 处理当前层级的数据
            String nextLevel = LevelUtil.calculateLevel(level, deptLevelDto.getId());
            // 处理下一层
            List<DeptLevelDto> tempDeptList = (List<DeptLevelDto>) levelDeptMap.get(nextLevel);
            if (CollectionUtils.isNotEmpty(tempDeptList)) {
                // 排序
                Collections.sort(tempDeptList, deptSeqComparator);
                // 设置下一层部门
                deptLevelDto.setDeptList(tempDeptList);
                // 进入到下一层处理
                transformDeptTree(tempDeptList, nextLevel, levelDeptMap);
            }
        }
    }

    public Comparator<DeptLevelDto> deptSeqComparator = new Comparator<DeptLevelDto>() {
        @Override
        public int compare(DeptLevelDto o1, DeptLevelDto o2) {
            return o1.getSeq() - o2.getSeq();
        }
    };

    public List<AclModuleLevelDto> aclModuleTree() {
        List<SysAclModule> aclModules = sysAclModuleMapper.findAll();
        // 转换对象
        List<AclModuleLevelDto> aclModuleLevelDtoList = AclModuleLevelDto.aclModuleLevelDtoList(aclModules);
        return aclModuleListToTree(aclModuleLevelDtoList);
    }

    private List<AclModuleLevelDto> aclModuleListToTree(List<AclModuleLevelDto> aclModuleLevelDtoList) {
        if (CollectionUtils.isEmpty(aclModuleLevelDtoList)) {
            return Lists.newArrayList();
        }
        Multimap<String, AclModuleLevelDto> levelDtoMultimap = ArrayListMultimap.create();
        List<AclModuleLevelDto> rootList = Lists.newArrayList();

        for (AclModuleLevelDto aclModuleLevelDto : aclModuleLevelDtoList) {
            levelDtoMultimap.put(aclModuleLevelDto.getLevel(), aclModuleLevelDto);
            // 根节点
            if (LevelUtil.ROOT.equals(aclModuleLevelDto.getLevel())) {
                rootList.add(aclModuleLevelDto);
            }
        }
        // 按照seq从小到大排序
        Collections.sort(rootList, new Comparator<AclModuleLevelDto>() {
            @Override
            public int compare(AclModuleLevelDto o1, AclModuleLevelDto o2) {
                return o1.getSeq() - o2.getSeq();
            }
        });
        // 递归生成树
        transformAclModelTree(rootList, LevelUtil.ROOT, levelDtoMultimap);
        return rootList;
    }

    private void transformAclModelTree(List<AclModuleLevelDto> rootList, String root, Multimap<String, AclModuleLevelDto> levelDtoMultimap) {
        for (AclModuleLevelDto dto : rootList) {
            // 处理当前层级的数据
            String nextLevel = LevelUtil.calculateLevel(root, dto.getId());
            // 处理下一层
            List<AclModuleLevelDto> moduleLevelDtoList = (List<AclModuleLevelDto>) levelDtoMultimap.get(nextLevel);

            if (CollectionUtils.isNotEmpty(moduleLevelDtoList)) {
                // 排序
                Collections.sort(moduleLevelDtoList, new Comparator<AclModuleLevelDto>() {
                    @Override
                    public int compare(AclModuleLevelDto o1, AclModuleLevelDto o2) {
                        return o1.getSeq() - o2.getSeq();
                    }
                });
                // 设置下一层部门
                dto.setAclModuleList(moduleLevelDtoList);
                // 进入下层管理
                transformAclModelTree(moduleLevelDtoList, nextLevel, levelDtoMultimap);
            }

        }
    }

    /**
     * 获取角色树
     * @param roleId
     * @return
     */
    public List<AclModuleLevelDto> roleTree(int roleId) {
        // 1.当前用户已分配的权限点
        List<SysAcl> userAclList = sysCoreService.getCurrentUserAclList();
        // 2.获取当前角色分配的权限点
        List<SysAcl> roleAclList = sysCoreService.getRoleAclList(roleId);
        // 3、当前系统所有权限点
        List<AclDto> aclDtoList = Lists.newArrayList();

        Set<Integer> userAclIdSet = userAclList.stream().map(sysAcl -> sysAcl.getId()).collect(Collectors.toSet());
        Set<Integer> roleAclIdSet = roleAclList.stream().map(sysAcl -> sysAcl.getId()).collect(Collectors.toSet());

        // 获取所有权限
        List<SysAcl> sysAclList = sysAclMapper.findAll();

        for (SysAcl acl : sysAclList) {
            AclDto dto = AclDto.adapt(acl);
            if (userAclIdSet.contains(acl.getId())) {
                dto.setHasAcl(true);
            }
            if (roleAclIdSet.contains(acl.getId())) {
                dto.setChecked(true);
            }
            aclDtoList.add(dto);
        }
        return aclListToTree(aclDtoList);
    }

    private List<AclModuleLevelDto> aclListToTree(List<AclDto> aclDtoList) {
        if (CollectionUtils.isEmpty(aclDtoList)) {
            return Lists.newArrayList();
        }
        List<AclModuleLevelDto> aclModuleLevelList = aclModuleTree();

        Multimap<Integer, AclDto> moduleIdAclMap = ArrayListMultimap.create();
        for(AclDto acl : aclDtoList) {
            if (acl.getStatus() == 1) {
                moduleIdAclMap.put(acl.getAclModuleId(), acl);
            }
        }
        bindAclsWithOrder(aclModuleLevelList, moduleIdAclMap);
        return aclModuleLevelList;
    }

    private void bindAclsWithOrder(List<AclModuleLevelDto> aclModuleLevelList, Multimap<Integer, AclDto> moduleIdAclMap) {
        if (CollectionUtils.isEmpty(aclModuleLevelList)) {
            return;
        }
        for (AclModuleLevelDto dto : aclModuleLevelList) {
            List<AclDto> aclDtoList = (List<AclDto>)moduleIdAclMap.get(dto.getId());
            if (CollectionUtils.isNotEmpty(aclDtoList)) {
                Collections.sort(aclDtoList, new Comparator<AclDto>() {
                    @Override
                    public int compare(AclDto o1, AclDto o2) {
                        return o1.getSeq() - o2.getSeq();
                    }
                });
                dto.setAclList(aclDtoList);
            }
            bindAclsWithOrder(dto.getAclModuleList(), moduleIdAclMap);
        }
    }
}
