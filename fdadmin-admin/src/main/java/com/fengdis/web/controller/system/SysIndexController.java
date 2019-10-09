package com.fengdis.web.controller.system;

import java.util.List;

import com.fengdis.system.domain.SysNotice;
import com.fengdis.system.service.ISysNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import com.fengdis.common.config.Global;
import com.fengdis.common.core.controller.BaseController;
import com.fengdis.framework.util.ShiroUtils;
import com.fengdis.system.domain.SysMenu;
import com.fengdis.system.domain.SysUser;
import com.fengdis.system.service.ISysMenuService;

/**
 * 首页 业务处理
 * 
 * @author fengdis
 */
@Controller
public class SysIndexController extends BaseController
{
    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private ISysNoticeService iSysNoticeService;

    // 系统首页
    @GetMapping("/index")
    public String index(ModelMap mmap)
    {
        // 取身份信息
        SysUser user = ShiroUtils.getSysUser();
        // 根据用户id取出菜单
        List<SysMenu> menus = menuService.selectMenusByUser(user);
        mmap.put("menus", menus);
        mmap.put("user", user);
        mmap.put("copyrightYear", Global.getCopyrightYear());
        mmap.put("demoEnabled", Global.isDemoEnabled());
        return "index";
    }

    // 系统介绍
    @GetMapping("/system/main")
    public String main(ModelMap mmap)
    {
        // 取身份信息
        SysUser user = ShiroUtils.getSysUser();
        mmap.put("user", user);
        mmap.put("version", Global.getVersion());
        /*SysNotice sysNotice = iSysNoticeService.selectNoticeByType("2");
        mmap.put("notice",sysNotice);*/
        return "main";
    }
}
