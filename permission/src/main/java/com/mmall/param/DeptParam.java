package com.mmall.param;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Wgs
 * @version 1.0
 * @create：2020/08/03
 */
@Data
public class DeptParam implements Serializable {
    private Integer id;

    @NotBlank(message = "部门名称不可以为空")
    @Length(max = 15, min = 2, message = "部门名称长度需要在2-15个字之间")
    private String name;
    /**
     * 默认是父id
     */
    private Integer parentId = 0;
    @NotNull(message = "展示顺序不可以为空")
    private Integer seq;
    @Length(max = 150, message = "备注的长度需要在150个字以内")
    private String remark;
}
