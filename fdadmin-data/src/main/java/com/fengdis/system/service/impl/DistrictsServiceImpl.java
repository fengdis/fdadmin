package com.fengdis.system.service.impl;

import com.fengdis.common.core.text.Convert;
import com.fengdis.system.domain.Districts;
import com.fengdis.system.mapper.DistrictsMapper;
import com.fengdis.system.service.IDistrictsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 地区 服务层实现
 * 
 * @author ruoyi
 * @date 2018-12-19
 */
@Service
public class DistrictsServiceImpl implements IDistrictsService
{
	@Autowired
	private DistrictsMapper districtsMapper;

	/**
     * 查询地区信息
     * 
     * @param id 地区ID
     * @return 地区信息
     */
    @Override
	public Districts selectDistrictsById(Integer id)
	{
	    return districtsMapper.selectDistrictsById(id);
	}
	
	/**
     * 查询地区列表
     * 
     * @param districts 地区信息
     * @return 地区集合
     */
	@Override
	public List<Districts> selectDistrictsList(Districts districts)
	{
	    return districtsMapper.selectDistrictsList(districts);
	}
	
    /**
     * 新增地区
     * 
     * @param districts 地区信息
     * @return 结果
     */
	@Override
	public int insertDistricts(Districts districts)
	{
	    return districtsMapper.insertDistricts(districts);
	}
	
	/**
     * 修改地区
     * 
     * @param districts 地区信息
     * @return 结果
     */
	@Override
	public int updateDistricts(Districts districts)
	{
	    return districtsMapper.updateDistricts(districts);
	}

	/**
     * 删除地区对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteDistrictsByIds(String ids)
	{
		return districtsMapper.deleteDistrictsByIds(Convert.toStrArray(ids));
	}
	
}
