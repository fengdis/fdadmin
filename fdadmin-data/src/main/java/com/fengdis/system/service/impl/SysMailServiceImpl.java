package com.fengdis.system.service.impl;

import com.fengdis.common.core.text.Convert;
import com.fengdis.system.domain.SysMail;
import com.fengdis.system.mapper.SysMailMapper;
import com.fengdis.system.service.ISysMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("sysMailService")
public class SysMailServiceImpl implements ISysMailService
{
    @Autowired
    private SysMailMapper sysMailMapper;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.zmr.wind.modules.sys.service.ISysMailService#getList(com.zmr.wind.
     * modules.sys.entity.SysMail)
     */
    @Override
    public List<SysMail> getList(SysMail sysMail)
    {

        return sysMailMapper.selectSysMailList(sysMail);
    }

    /* (non-Javadoc)
     * @see com.ruoyi.system.service.ISysMailService#save(com.ruoyi.system.domain.SysMail)
     */
    @Override
    public int save(SysMail mailEntity)
    {
        /*return sysMailMapper.insertSelective(mailEntity);*/
        return sysMailMapper.insertSysMail(mailEntity);
    }

    /* (non-Javadoc)
     * @see com.ruoyi.system.service.ISysMailService#findById(java.lang.Long)
     */
    @Override
    public SysMail findById(Long mailId)
    {
        /*return sysMailMapper.selectByPrimaryKey(mailId);*/
        return sysMailMapper.selectSysMailById(mailId);
    }

    /* (non-Javadoc)
     * @see com.ruoyi.system.service.ISysMailService#update(com.ruoyi.system.domain.SysMail)
     */
    @Override
    public int update(SysMail sysMail)
    {
        /*return sysMailMapper.updateByPrimaryKeySelective(sysMail);*/
        return sysMailMapper.updateSysMail(sysMail);
    }

    /* (non-Javadoc)
     * @see com.ruoyi.system.service.ISysMailService#deleteByIds(java.lang.String)
     */
    @Override
    public int deleteByIds(String ids)
    {
        /*return sysMailMapper.deleteByIds(ids);*/
        return sysMailMapper.deleteSysMailByIds(Convert.toStrArray(ids));
    }
}
