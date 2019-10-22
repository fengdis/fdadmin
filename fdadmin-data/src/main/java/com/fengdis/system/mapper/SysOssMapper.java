package com.fengdis.system.mapper;

import com.fengdis.common.core.dao.BaseMapper;
import com.fengdis.system.domain.SysOss;

import java.util.List;

/**
 * 文件上传
 */
public interface SysOssMapper /*extends BaseMapper<SysOss>*/
{

    /**
     * 查询地区信息
     *
     * @param id 地区ID
     * @return 地区信息
     */
    public SysOss selectSysOssById(Long id);

    /**
     * 查询地区列表
     *
     * @param sysOss 地区信息
     * @return 地区集合
     */
    public List<SysOss> selectSysOssList(SysOss sysOss);

    /**
     * 新增地区
     *
     * @param sysOss 地区信息
     * @return 结果
     */
    public int insertSysOss(SysOss sysOss);

    /**
     * 修改地区
     *
     * @param sysOss 地区信息
     * @return 结果
     */
    public int updateSysOss(SysOss sysOss);

    /**
     * 删除地区
     *
     * @param id 地区ID
     * @return 结果
     */
    public int deleteSysOssById(Long id);

    /**
     * 批量删除地区
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysOssByIds(String[] ids);

}
