package com.fengdis.system.service;

import com.fengdis.system.domain.SysMail;

import java.util.List;

/**
 * 文件上传
 */
public interface ISysMailService
{
    /**
     * 列表查询方法
     * @param sysMail
     * @return
     * @author zmr
     */
    List<SysMail> getList(SysMail sysMail);

    /**
     * @param ossEntity
     * @author zmr
     */
    int save(SysMail ossEntity);

    /**
     * @param ossId
     * @return
     * @author zmr
     */
    SysMail findById(Long ossId);

    /**
     * @param sysMail
     * @return
     * @author zmr
     */
    int update(SysMail sysMail);

    /**
     * @param ids
     * @return
     * @author zmr
     */
    int deleteByIds(String ids);
}
