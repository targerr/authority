package com.mmall.dto;

import com.google.common.collect.Lists;
import com.mmall.model.SysAcl;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.util.List;

@Getter
@Setter
@ToString
public class AclDto extends SysAcl {

    // 是否要默认选中
    private boolean checked = false;

    // 是否有权限操作
    private boolean hasAcl = false;

    public static AclDto adapt(SysAcl acl) {
        AclDto dto = new AclDto();
        BeanUtils.copyProperties(acl, dto);
        return dto;
    }

    public static List<AclDto> acl2AclDto(List<SysAcl> aclList) {
        List<AclDto> aclDtos = Lists.newArrayList();
        aclList.forEach(e -> aclDtos.add(adapt(e)));
        return aclDtos;
    }
}
