package com.mmall.service;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.mmall.dao.SysDeptMapper;
import com.mmall.dto.DeptLevelDto;
import com.mmall.model.SysDept;
import com.mmall.util.LevelUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Wgs
 * @version 1.0
 * @create：2020/08/03
 */
@Service
public class SysTreeService {
    @Resource
    private SysDeptMapper sysDeptMapper;
    public List<DeptLevelDto> deptTree() {
        List<SysDept> sysDepts = sysDeptMapper.getAllDept();
        if (CollectionUtils.isEmpty(sysDepts)){
            return null;
        }
        List<DeptLevelDto> deptLevelDtoList = Lists.newArrayList();
        for (SysDept sysDept : sysDepts){
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

    public static void main(String[] args) {
        String str = "abcdad1341";

        System.out.println(str.indexOf("d"));
//        Multimap<String,String> multimap = ArrayListMultimap.create();
//
//        multimap.put("lower", "a");
//        multimap.put("lower", "b");
//        multimap.put("lower", "c");
//
//        multimap.put("upper", "A");
//        multimap.put("upper", "B");
//
//        List<String> lowerList = (List<String>)multimap.get("lower");
//        //输出key为lower的list集合
//        System.out.println("输出key为lower的list集合=========");
//        System.out.println(lowerList.toString());
//        lowerList.add("f");
//        System.out.println(lowerList.toString());
//
//
//        Map<String, Collection<String>> map = multimap.asMap();
//        System.out.println("把Multimap转为一个map============");
//        for (Map.Entry<String,  Collection<String>> entry : map.entrySet()) {
//            String key = entry.getKey();
//            Collection<String> value =  multimap.get(key);
//            System.out.println(key + ":" + value);
//        }
//
//        System.out.println("获得所有Multimap的key值==========");
//        Set<String> keys =  multimap.keySet();
//        for(String key:keys){
//            System.out.println(key);
//        }
//
//        System.out.println("输出Multimap所有的value值========");
//        Collection<String> values = multimap.values();
//        System.out.println(values);
//


    }

}