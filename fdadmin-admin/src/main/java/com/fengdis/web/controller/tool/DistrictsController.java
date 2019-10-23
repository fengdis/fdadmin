package com.fengdis.web.controller.tool;

import com.fengdis.common.annotation.Log;
import com.fengdis.common.core.controller.BaseController;
import com.fengdis.common.core.page.TableDataInfo;
import com.fengdis.common.enums.BusinessType;
import com.fengdis.common.utils.poi.ExcelUtil;
import com.fengdis.framework.util.ShiroUtils;
import com.fengdis.system.domain.Districts;
import com.fengdis.system.service.IDistrictsService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 地区 信息操作处理
 * 
 * @author ruoyi
 * @date 2018-12-19
 */
@Controller
@RequestMapping("/tool/districts")
public class DistrictsController extends BaseController
{
    private String prefix = "tool/districts";
	
	@Autowired
	private IDistrictsService districtsService;
	
	@RequiresPermissions("tool:districts:view")
	@GetMapping()
	public String districts()
	{
	    return prefix + "/districts";
	}
	
	@RequiresPermissions("tool:districts:list")
	@GetMapping("demo")
	public String demo()
	{
	    return prefix + "/demo";
	}
	
	/**
	 * 查询地区列表
	 */
	@RequiresPermissions("tool:districts:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(Districts districts)
	{
		startPage();
        List<Districts> list = districtsService.selectDistrictsList(districts);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出地区列表
	 */
	@RequiresPermissions("tool:districts:export")
    @PostMapping("/export")
    @ResponseBody
    public ResponseEntity<String> export(Districts districts)
    {
    	List<Districts> list = districtsService.selectDistrictsList(districts);
        ExcelUtil<Districts> util = new ExcelUtil<Districts>(Districts.class);
        return util.exportExcel(list, "districts");
    }
	
	/**
	 * 新增地区
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存地区
	 */
	@RequiresPermissions("tool:districts:add")
	@Log(title = "地区", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public ResponseEntity<String> addSave(Districts districts)
	{		
	    districts.setPid(districts.getId()/100);
	    districts.setCreateTime(new Date());
	    districts.setUpdateTime(new Date());
	    districts.setOperator(ShiroUtils.getLoginName());
		return toAjax(districtsService.insertDistricts(districts));
	}

	/**
	 * 修改地区
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Integer id, ModelMap mmap)
	{
		Districts districts = districtsService.selectDistrictsById(id);
		mmap.put("districts", districts);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存地区
	 */
	@RequiresPermissions("tool:districts:edit")
	@Log(title = "地区", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public ResponseEntity<String> editSave(Districts districts)
	{		
	    districts.setPid(districts.getId()/100);
	    districts.setOperator(ShiroUtils.getLoginName());
	    districts.setUpdateTime(new Date());
		return toAjax(districtsService.updateDistricts(districts));
	}
	
	/**
	 * 删除地区
	 */
	@RequiresPermissions("tool:districts:remove")
	@Log(title = "地区", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public ResponseEntity<String> remove(String ids)
	{		
		return toAjax(districtsService.deleteDistrictsByIds(ids));
	}
	
}
