package com.mmall.dto;

import com.google.common.collect.Lists;
import com.mmall.model.SysDept;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * @author Wgs
 * @version 1.0
 * @create：2020/08/03
 */
@Data
public class DeptLevelDto extends SysDept {
    private List<DeptLevelDto> deptList = Lists.newArrayList();

    public static DeptLevelDto adapt(SysDept sysDept) {
        DeptLevelDto deptLevelDto = new DeptLevelDto();
        BeanUtils.copyProperties(sysDept, deptLevelDto);
        return deptLevelDto;
    }
}
