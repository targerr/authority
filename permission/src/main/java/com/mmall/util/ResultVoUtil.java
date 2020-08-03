package com.mmall.util;

import com.mmall.common.ResultVo;

/**
 * @author Wgs
 * @version 1.0
 * @create：2020/08/03
 */
public class ResultVoUtil {
    public static ResultVo success(Object object) {
        ResultVo resultVo = new ResultVo();
        resultVo.setCode(0);
        resultVo.setMsg("成功");
        resultVo.setData(object);
        return resultVo;
    }

    public static ResultVo success() {
        return success(null);
    }

    public static ResultVo error(Integer code, String msg) {
        ResultVo resultVo = new ResultVo();
        resultVo.setCode(code);
        resultVo.setMsg("成功");
        return resultVo;
    }
}
