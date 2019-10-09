package com.fengdis.web.controller.monitor;

import com.fengdis.common.annotation.Log;
import com.fengdis.common.core.controller.BaseController;
import com.fengdis.common.core.domain.AjaxResult;
import com.fengdis.common.core.page.TableDataInfo;
import com.fengdis.common.enums.BusinessType;
import com.fengdis.common.utils.PageUtil;
import com.fengdis.common.utils.poi.ExcelUtil;
import com.fengdis.framework.web.domain.RedisVo;
import com.fengdis.framework.web.service.RedisService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/monitor/redis")
public class RedisController extends BaseController {

    @Autowired
    private RedisService redisService;

    private String prefix = "monitor/redis";

    @RequiresPermissions("monitor:redis:view")
    @GetMapping()
    public String list() {
        return prefix + "/redis";
    }

    @RequiresPermissions("monitor:redis:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(RedisVo resources){
        Pageable pageable = buildPageable();
        List<RedisVo> list = redisService.findByKey(resources.getKey(), pageable);
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(200);
        rspData.setRows(PageUtil.toPage(pageable.getPageNumber(),pageable.getPageSize(),list));
        rspData.setTotal(list.size());
        return rspData;
    }

    @Log(title = "系统缓存", businessType = BusinessType.DELETE)
    @PostMapping(value = "/remove")
    @RequiresPermissions("monitor:redis:remove")
    @ResponseBody
    public ResponseEntity<String> remove(String ids){
        String[] split = ids.split(",");
        for(String temp : split){
            redisService.delete(temp);
        }

        return AjaxResult.success();
    }

    @Log(title = "系统缓存", businessType = BusinessType.CLEAN)
    @PostMapping(value = "/clean")
    @RequiresPermissions("monitor:redis:clean")
    @ResponseBody
    public ResponseEntity<String> clean(){
        redisService.flushdb();
        return AjaxResult.success();
    }

    @Log(title = "系统缓存", businessType = BusinessType.EXPORT)
    @RequiresPermissions("monitor:redis:export")
    @PostMapping("/export")
    @ResponseBody
    public ResponseEntity<String> export(RedisVo resources)
    {
        Pageable pageable = buildPageable();
        List<RedisVo> list = redisService.findByKey(resources.getKey(), pageable);
        ExcelUtil<RedisVo> util = new ExcelUtil<RedisVo>(RedisVo.class);
        return util.exportExcel(list, "系统缓存");
    }

}
