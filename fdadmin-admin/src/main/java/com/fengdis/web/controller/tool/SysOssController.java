package com.fengdis.web.controller.tool;

import com.alibaba.fastjson.JSON;
import com.fengdis.common.base.BaseExServiceException;
import com.fengdis.common.core.controller.BaseController;
import com.fengdis.common.core.domain.AjaxResult;
import com.fengdis.common.core.page.TableDataInfo;
import com.fengdis.framework.util.ShiroUtils;
import com.fengdis.framework.util.ValidatorUtils;
import com.fengdis.system.domain.SysOss;
import com.fengdis.system.service.ISysConfigService;
import com.fengdis.system.service.ISysOssService;
import com.fengdis.web.controller.system.cloud.CloudConstant;
import com.fengdis.web.controller.system.cloud.CloudStorageConfig;
import com.google.gson.Gson;
import com.fengdis.web.controller.system.cloud.CloudConstant.CloudService;
import com.fengdis.web.controller.system.cloud.CloudStorageService;
import com.fengdis.web.controller.system.cloud.OSSFactory;
import com.fengdis.web.controller.system.cloud.valdator.AliyunGroup;
import com.fengdis.web.controller.system.cloud.valdator.QcloudGroup;
import com.fengdis.web.controller.system.cloud.valdator.QiniuGroup;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件上传
 */
@Controller
@RequestMapping("tool/oss")
public class SysOssController extends BaseController
{
    private String              prefix = "tool/oss";

    private final static String KEY    = CloudConstant.CLOUD_STORAGE_CONFIG_KEY;

    @Autowired
    private ISysOssService sysOssService;

    @Autowired
    private ISysConfigService sysConfigService;

    @RequiresPermissions("tool:oss:view")
    @GetMapping()
    public String dept()
    {
        return prefix + "/oss";
    }

    /**
     * 列表
     */
    @RequestMapping("list")
    @RequiresPermissions("tool:oss:list")
    @ResponseBody
    public TableDataInfo list(SysOss sysOss)
    {
        startPage();
        List<SysOss> list = sysOssService.getList(sysOss);
        return getDataTable(list);
    }

    /**
     * 云存储配置信息
     */
    @RequestMapping("config")
    @RequiresPermissions("tool:oss:config")
    public String config(Model model)
    {
        String jsonconfig = sysConfigService.selectConfigByKey(CloudConstant.CLOUD_STORAGE_CONFIG_KEY);
        // 获取云存储配置信息
        CloudStorageConfig config = JSON.parseObject(jsonconfig, CloudStorageConfig.class);
        model.addAttribute("config", config);
        return prefix + "/config";
    }

    /**
     * 保存云存储配置信息
     */
    @RequestMapping("saveConfig")
    @RequiresPermissions("tool:oss:config")
    @ResponseBody
    public ResponseEntity<String> saveConfig(CloudStorageConfig config)
    {
        // 校验类型
        ValidatorUtils.validateEntity(config);
        if (config.getType() == CloudService.QINIU.getValue())
        {
            // 校验七牛数据
            ValidatorUtils.validateEntity(config, QiniuGroup.class);
        }
        else if (config.getType() == CloudService.ALIYUN.getValue())
        {
            // 校验阿里云数据
            ValidatorUtils.validateEntity(config, AliyunGroup.class);
        }
        else if (config.getType() == CloudService.QCLOUD.getValue())
        {
            // 校验腾讯云数据
            ValidatorUtils.validateEntity(config, QcloudGroup.class);
        }
        return toAjax(sysConfigService.updateValueByKey(KEY, new Gson().toJson(config)));
    }

    /**
     * 上传文件
     */
    @RequestMapping("/upload")
    @RequiresPermissions("tool:oss:add")
    @ResponseBody
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) throws Exception
    {
        if (file.isEmpty())
        {
            throw new BaseExServiceException("上传文件不能为空");
        }
        // 上传文件
        String fileName = file.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        CloudStorageService storage = OSSFactory.build();
        String url = storage.uploadSuffix(file.getBytes(), suffix);
        // 保存文件信息
        SysOss ossEntity = new SysOss();
        ossEntity.setUrl(url);
        ossEntity.setFileSuffix(suffix);
        ossEntity.setCreateBy(ShiroUtils.getLoginName());
        ossEntity.setFileName(fileName);
        ossEntity.setCreateTime(new Date());
        ossEntity.setService(storage.getService());

        int rows = sysOssService.save(ossEntity);
        Map<String,Object> map = new HashMap<>();
        map.put("url", ossEntity.getUrl());
        map.put("fileName",ossEntity.getFileName());
        return rows > 0 ? AjaxResult.success(map) : AjaxResult.error();
        //return toAjax(sysOssService.save(ossEntity));
    }

    /**
     * 修改
     */
    @GetMapping("edit/{ossId}")
    @RequiresPermissions("tool:oss:edit")
    public String edit(@PathVariable("ossId") Long ossId, Model model)
    {
        SysOss sysOss = sysOssService.findById(ossId);
        model.addAttribute("sysOss", sysOss);
        return prefix + "/edit";
    }

    @GetMapping("editor")
    @RequiresPermissions("tool:oss:add")
    public String editor()
    {
        return prefix + "/editor";
    }

    /**
     * 修改
     */
    @PostMapping("edit")
    @RequiresPermissions("tool:oss:edit")
    @ResponseBody
    public ResponseEntity<String> editSave(SysOss sysOss)
    {
        return toAjax(sysOssService.update(sysOss));
    }

    /**
     * 删除
     */
    @RequestMapping("remove")
    @RequiresPermissions("tool:oss:remove")
    @ResponseBody
    public ResponseEntity<String> delete(String ids)
    {
        return toAjax(sysOssService.deleteByIds(ids));
    }
}
