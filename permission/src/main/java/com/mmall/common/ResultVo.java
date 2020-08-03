package com.mmall.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Wgs
 * @version 1.0
 * @create：2020/08/03
 */
@Data
public class ResultVo<T> implements Serializable {
    /**
     * 错误码
     */
    private Integer code;
    /**
     * 提示信息
     */
    private String msg;
    /**
     * 返回结果
     */
    private T data;
}
