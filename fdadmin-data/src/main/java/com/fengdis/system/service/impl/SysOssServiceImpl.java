package com.fengdis.system.service.impl;

import java.util.List;

import com.fengdis.common.core.text.Convert;
import com.fengdis.system.domain.SysOss;
import com.fengdis.system.mapper.SysOssMapper;
import com.fengdis.system.service.ISysOssService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service("sysOssService")
public class SysOssServiceImpl implements ISysOssService
{
    @Autowired
    private SysOssMapper sysOssMapper;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.zmr.wind.modules.sys.service.ISysOssService#getList(com.zmr.wind.
     * modules.sys.entity.SysOss)
     */
    @Override
    public List<SysOss> getList(SysOss sysOss)
    {

        return sysOssMapper.selectSysOssList(sysOss);
    }

    /* (non-Javadoc)
     * @see com.ruoyi.system.service.ISysOssService#save(com.ruoyi.system.domain.SysOss)
     */
    @Override
    public int save(SysOss ossEntity)
    {
        /*return sysOssMapper.insertSelective(ossEntity);*/
        return sysOssMapper.insertSysOss(ossEntity);
    }

    /* (non-Javadoc)
     * @see com.ruoyi.system.service.ISysOssService#findById(java.lang.Long)
     */
    @Override
    public SysOss findById(Long ossId)
    {
        /*return sysOssMapper.selectByPrimaryKey(ossId);*/
        return sysOssMapper.selectSysOssById(ossId);
    }

    /* (non-Javadoc)
     * @see com.ruoyi.system.service.ISysOssService#update(com.ruoyi.system.domain.SysOss)
     */
    @Override
    public int update(SysOss sysOss)
    {
        /*return sysOssMapper.updateByPrimaryKeySelective(sysOss);*/
        return sysOssMapper.updateSysOss(sysOss);
    }

    /* (non-Javadoc)
     * @see com.ruoyi.system.service.ISysOssService#deleteByIds(java.lang.String)
     */
    @Override
    public int deleteByIds(String ids)
    {
        /*return sysOssMapper.deleteByIds(ids);*/
        return sysOssMapper.deleteSysOssByIds(Convert.toStrArray(ids));
    }
}
