package com.mmall.dao;


import com.mmall.model.SysDept;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;


/**
 * Created by Wgs on 2020/8/4.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class SysDeptMapperTest {
    @Autowired
    private SysDeptMapper sysDeptMapper;

    @Test
    public void test() {
        List<SysDept> d = sysDeptMapper.getChildDeptListByLevel("0.1");
        System.out.println("-----------");
    }

    @Test
    public void level(){
        String oldLevelPrefix = "0.2";
        String level = "0.2.4";
        String newLevelPrefix = "0.1";
//        if (level.indexOf(oldLevelPrefix) == 0) {
//            level = newLevelPrefix + level.substring(oldLevelPrefix.length());
//            System.out.println(level);
//        }

        System.out.println("=======");
        System.out.println(level.indexOf(oldLevelPrefix));

        System.out.println(level.substring(oldLevelPrefix.length()));
    }


}
