package com.fengdis.web.controller.monitor;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/monitor/log")
public class LogController {

    private String prefix = "monitor/log";

    @RequiresPermissions("monitor:log:view")
    @GetMapping()
    public String list(ModelMap mmap) {
        return prefix + "/log";
    }

}
