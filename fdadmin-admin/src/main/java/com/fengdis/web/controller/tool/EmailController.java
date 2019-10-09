package com.fengdis.web.controller.tool;

import com.fengdis.common.core.controller.BaseController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author fengdis
 */
@Controller
@RequestMapping("/tool/email")
public class EmailController extends BaseController
{
    private String prefix = "tool/email";

    @RequiresPermissions("tool:email:view")
    @GetMapping()
    public String build()
    {
        return prefix + "/email";
    }
}
