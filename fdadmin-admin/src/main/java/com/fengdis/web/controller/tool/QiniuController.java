package com.fengdis.web.controller.tool;

import com.fengdis.common.core.controller.BaseController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * build 表单构建
 * 
 * @author fengdis
 */
@Controller
@RequestMapping("/tool/qiniu")
public class QiniuController extends BaseController
{
    private String prefix = "tool/qiniu";

    @RequiresPermissions("tool:qiniu:view")
    @GetMapping()
    public String build()
    {
        return prefix + "/qiniu";
    }
}
