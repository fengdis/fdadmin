package com.fengdis.quartz.controller;

import com.fengdis.common.annotation.Log;
import com.fengdis.common.core.controller.BaseController;
import com.fengdis.common.core.domain.AjaxResult;
import com.fengdis.common.core.page.TableDataInfo;
import com.fengdis.common.enums.BusinessType;
import com.fengdis.common.exception.job.TaskException;
import com.fengdis.common.utils.poi.ExcelUtil;
import com.fengdis.quartz.domain.SysJob;
import com.fengdis.quartz.service.ISysJobService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 调度任务信息操作处理
 * 
 * @author fengdis
 */
@Controller
@RequestMapping("/tool/job")
public class SysJobController extends BaseController
{
    private String prefix = "tool/job";

    @Autowired
    private ISysJobService jobService;

    @RequiresPermissions("tool:job:view")
    @GetMapping()
    public String job()
    {
        return prefix + "/job";
    }

    @RequiresPermissions("tool:job:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysJob job)
    {
        startPage();
        List<SysJob> list = jobService.selectJobList(job);
        return getDataTable(list);
    }

    @Log(title = "定时任务", businessType = BusinessType.EXPORT)
    @RequiresPermissions("tool:job:export")
    @PostMapping("/export")
    @ResponseBody
    public ResponseEntity<String> export(SysJob job)
    {
        List<SysJob> list = jobService.selectJobList(job);
        ExcelUtil<SysJob> util = new ExcelUtil<SysJob>(SysJob.class);
        return util.exportExcel(list, "定时任务");
    }

    @Log(title = "定时任务", businessType = BusinessType.DELETE)
    @RequiresPermissions("tool:job:remove")
    @PostMapping("/remove")
    @ResponseBody
    public ResponseEntity<String> remove(String ids) throws SchedulerException
    {
        jobService.deleteJobByIds(ids);
        return AjaxResult.success();
    }

    @RequiresPermissions("tool:job:detail")
    @GetMapping("/detail/{jobId}")
    public String detail(@PathVariable("jobId") Long jobId, ModelMap mmap)
    {
        mmap.put("name", "job");
        mmap.put("job", jobService.selectJobById(jobId));
        return prefix + "/detail";
    }

    /**
     * 任务调度状态修改
     */
    @Log(title = "定时任务", businessType = BusinessType.UPDATE)
    @RequiresPermissions("tool:job:changeStatus")
    @PostMapping("/changeStatus")
    @ResponseBody
    public ResponseEntity<String> changeStatus(SysJob job) throws SchedulerException
    {
        SysJob newJob = jobService.selectJobById(job.getJobId());
        newJob.setStatus(job.getStatus());
        return toAjax(jobService.changeStatus(newJob));
    }

    /**
     * 任务调度立即执行一次
     */
    @Log(title = "定时任务", businessType = BusinessType.UPDATE)
    @RequiresPermissions("tool:job:changeStatus")
    @PostMapping("/run")
    @ResponseBody
    public ResponseEntity<String> run(SysJob job) throws SchedulerException
    {
        jobService.run(job);
        return AjaxResult.success();
    }

    /**
     * 新增调度
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存调度
     */
    @Log(title = "定时任务", businessType = BusinessType.INSERT)
    @RequiresPermissions("tool:job:add")
    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<String> addSave(@Validated SysJob job) throws SchedulerException, TaskException
    {
        return toAjax(jobService.insertJob(job));
    }

    /**
     * 修改调度
     */
    @GetMapping("/edit/{jobId}")
    public String edit(@PathVariable("jobId") Long jobId, ModelMap mmap)
    {
        mmap.put("job", jobService.selectJobById(jobId));
        return prefix + "/edit";
    }

    /**
     * 修改保存调度
     */
    @Log(title = "定时任务", businessType = BusinessType.UPDATE)
    @RequiresPermissions("tool:job:edit")
    @PostMapping("/edit")
    @ResponseBody
    public ResponseEntity<String> editSave(@Validated SysJob job) throws SchedulerException, TaskException
    {
        return toAjax(jobService.updateJob(job));
    }

    /**
     * 校验cron表达式是否有效
     */
    @PostMapping("/checkCronExpressionIsValid")
    @ResponseBody
    public boolean checkCronExpressionIsValid(SysJob job)
    {
        return jobService.checkCronExpressionIsValid(job.getCronExpression());
    }
}
