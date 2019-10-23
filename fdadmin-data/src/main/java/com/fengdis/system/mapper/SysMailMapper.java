package com.fengdis.system.mapper;

import com.fengdis.system.domain.SysMail;

import java.util.List;

/**
 * 文件上传
 */
public interface SysMailMapper
{

    public SysMail selectSysMailById(Long id);

    public List<SysMail> selectSysMailList(SysMail sysMail);

    public int insertSysMail(SysMail sysMail);

    public int updateSysMail(SysMail sysMail);

    public int deleteSysMailById(Long id);

    public int deleteSysMailByIds(String[] ids);

}
