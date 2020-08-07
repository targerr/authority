package com.mmall.controller;

import com.mmall.common.JsonData;
import com.mmall.param.RoleParam;
import com.mmall.service.SysRoleService;
import com.mmall.service.SysTreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Wgs
 * @version 1.0
 * @create：2020/08/07
 */
@Controller
@RequestMapping("/sys/role")
public class SysRoleController {
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysTreeService sysTreeService;

    @GetMapping("role.page")
    public ModelAndView page() {
        return new ModelAndView("role");
    }

    @PostMapping("save.json")
    @ResponseBody
    public JsonData saveRole(RoleParam param) {
        sysRoleService.save(param);
        return JsonData.success();
    }

    @PostMapping("/update.json")
    public JsonData updateRole(RoleParam param) {
        sysRoleService.update(param);
        return JsonData.success();
    }

    @RequestMapping("/list.json")
    @ResponseBody
    public JsonData list() {
        return JsonData.success(sysRoleService.getAllList());
    }

    @RequestMapping("/roleTree.json")
    @ResponseBody
    public JsonData roleTree(@RequestParam("roleId") int roleId) {
        return JsonData.success(sysTreeService.roleTree(roleId));
    }
}
