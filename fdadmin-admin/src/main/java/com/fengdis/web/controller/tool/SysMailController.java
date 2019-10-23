package com.fengdis.web.controller.tool;

import com.alibaba.fastjson.JSON;
import com.fengdis.common.base.BaseExServiceException;
import com.fengdis.common.core.controller.BaseController;
import com.fengdis.common.core.domain.AjaxResult;
import com.fengdis.common.core.page.TableDataInfo;
import com.fengdis.common.core.text.Convert;
import com.fengdis.common.service.MailService;
import com.fengdis.framework.util.ShiroUtils;
import com.fengdis.framework.util.ValidatorUtils;
import com.fengdis.system.domain.SysMail;
import com.fengdis.system.domain.SysOss;
import com.fengdis.system.service.ISysConfigService;
import com.fengdis.system.service.ISysMailService;
import com.fengdis.web.controller.system.cloud.*;
import com.fengdis.web.controller.system.cloud.CloudConstant.CloudService;
import com.fengdis.web.controller.system.cloud.valdator.AliyunGroup;
import com.fengdis.web.controller.system.cloud.valdator.QcloudGroup;
import com.fengdis.web.controller.system.cloud.valdator.QiniuGroup;
import com.google.gson.Gson;
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
 * 邮件
 */
@Controller
@RequestMapping("tool/mail")
public class SysMailController extends BaseController
{
    private String              prefix = "tool/mail";

    private final static String KEY    = "sys.mail.config";

    @Autowired
    private ISysMailService sysMailService;

    @Autowired
    private ISysConfigService sysConfigService;

    @Autowired
    private MailService mailService;

    @RequiresPermissions("tool:mail:view")
    @GetMapping()
    public String dept()
    {
        return prefix + "/mail";
    }

    /**
     * 列表
     */
    @RequestMapping("list")
    @RequiresPermissions("tool:mail:list")
    @ResponseBody
    public TableDataInfo list(SysMail sysMail)
    {
        startPage();
        List<SysMail> list = sysMailService.getList(sysMail);
        return getDataTable(list);
    }

    /**
     * 邮件配置信息
     */
    @RequestMapping("config")
    @RequiresPermissions("tool:mail:config")
    public String config(Model model)
    {
        String jsonconfig = sysConfigService.selectConfigByKey(KEY);
        MailConfig config = JSON.parseObject(jsonconfig, MailConfig.class);
        model.addAttribute("config", config);
        return prefix + "/config";
    }

    /**
     * 保存邮件配置信息
     */
    @RequestMapping("saveConfig")
    @RequiresPermissions("tool:mail:config")
    @ResponseBody
    public ResponseEntity<String> saveConfig(MailConfig config)
    {

        return toAjax(sysConfigService.updateValueByKey(KEY, new Gson().toJson(config)));
    }

    @GetMapping("editor")
    @RequiresPermissions("tool:mail:add")
    public String editor()
    {
        return prefix + "/editor";
    }

    /**
     * 上传文件
     */
    @RequestMapping("/upload")
    @RequiresPermissions("tool:mail:add")
    @ResponseBody
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) throws Exception
    {
        /*if (file.isEmpty())
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
        return rows > 0 ? AjaxResult.success(map) : AjaxResult.error();*/
        return toAjax(0);
    }

    /**
     * 发送
     */
    @RequestMapping("/send")
    @RequiresPermissions("tool:mail:send")
    @ResponseBody
    public ResponseEntity<String> send(SysMail sysMail) throws Exception
    {
        String jsonconfig = sysConfigService.selectConfigByKey(KEY);
        MailConfig config = JSON.parseObject(jsonconfig, MailConfig.class);
        sysMail.setSender(config.getUser());
        mailService.sendMail(config.getName(), Convert.toStrArray(sysMail.getReceiver()),Convert.toStrArray(sysMail.getCc()),Convert.toStrArray(sysMail.getBcc()),sysMail.getSubject(),sysMail.getContent(),null,null);
        return toAjax(sysMailService.save(sysMail));
    }

    /**
     * 删除
     */
    @RequestMapping("remove")
    @RequiresPermissions("tool:mail:remove")
    @ResponseBody
    public ResponseEntity<String> delete(String ids)
    {
        return toAjax(sysMailService.deleteByIds(ids));
    }

}
