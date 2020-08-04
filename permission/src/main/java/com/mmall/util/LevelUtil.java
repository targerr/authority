package com.mmall.util;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Wgs
 * @version 1.0
 * @create：2020/08/03
 */
public class LevelUtil {
    public static final String SEPARATOR = ".";
    public static final String ROOT = "0";

    // 0
    // 0.1
    // 0.1.2
    // 0.1.3
    // 0.4
    public static String calculateLevel(String parentLevel, int parentId) {
        if (StringUtils.isBlank(parentLevel)) {
            return ROOT;
        } else {
            return StringUtils.join(parentLevel, SEPARATOR, parentId);
        }
    }

    public static void main(String[] args) {
        System.out.println(calculateLevel("0",1));
    }

}

