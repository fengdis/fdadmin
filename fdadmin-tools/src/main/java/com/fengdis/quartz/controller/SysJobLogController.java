package com.fengdis.quartz.controller;

import com.fengdis.common.annotation.Log;
import com.fengdis.common.core.controller.BaseController;
import com.fengdis.common.core.domain.AjaxResult;
import com.fengdis.common.core.page.TableDataInfo;
import com.fengdis.common.enums.BusinessType;
import com.fengdis.common.utils.poi.ExcelUtil;
import com.fengdis.quartz.domain.SysJobLog;
import com.fengdis.quartz.service.ISysJobLogService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 调度日志操作处理
 * 
 * @author fengdis
 */
@Controller
@RequestMapping("/tool/jobLog")
public class SysJobLogController extends BaseController
{
    private String prefix = "tool/job";

    @Autowired
    private ISysJobLogService jobLogService;

    @RequiresPermissions("tool:job:view")
    @GetMapping()
    public String jobLog()
    {
        return prefix + "/jobLog";
    }

    @RequiresPermissions("tool:job:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysJobLog jobLog)
    {
        startPage();
        List<SysJobLog> list = jobLogService.selectJobLogList(jobLog);
        return getDataTable(list);
    }

    @Log(title = "调度日志", businessType = BusinessType.EXPORT)
    @RequiresPermissions("tool:job:export")
    @PostMapping("/export")
    @ResponseBody
    public ResponseEntity<String> export(SysJobLog jobLog)
    {
        List<SysJobLog> list = jobLogService.selectJobLogList(jobLog);
        ExcelUtil<SysJobLog> util = new ExcelUtil<SysJobLog>(SysJobLog.class);
        return util.exportExcel(list, "调度日志");
    }

    @Log(title = "调度日志", businessType = BusinessType.DELETE)
    @RequiresPermissions("tool:job:remove")
    @PostMapping("/remove")
    @ResponseBody
    public ResponseEntity<String> remove(String ids)
    {
        return toAjax(jobLogService.deleteJobLogByIds(ids));
    }

    @RequiresPermissions("tool:job:detail")
    @GetMapping("/detail/{jobLogId}")
    public String detail(@PathVariable("jobLogId") Long jobLogId, ModelMap mmap)
    {
        mmap.put("name", "jobLog");
        mmap.put("jobLog", jobLogService.selectJobLogById(jobLogId));
        return prefix + "/detail";
    }

    @Log(title = "调度日志", businessType = BusinessType.CLEAN)
    @RequiresPermissions("tool:job:remove")
    @PostMapping("/clean")
    @ResponseBody
    public ResponseEntity<String> clean()
    {
        jobLogService.cleanJobLog();
        return AjaxResult.success();
    }
}
