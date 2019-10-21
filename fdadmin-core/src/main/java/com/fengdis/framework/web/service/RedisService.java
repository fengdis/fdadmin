package com.fengdis.framework.web.service;

import com.fengdis.framework.web.domain.RedisVo;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RedisService {

    /**
     * findById
     * @param key
     * @return
     */
    List<RedisVo> findByKey(String key, Pageable pageable);

    /**
     * 查询验证码的值
     * @param key
     * @return
     */
    String getCodeVal(String key);

    /**
     * 保存验证码
     * @param key
     * @param val
     */
    void saveCode(String key, Object val);

    /**
     * delete
     * @param key
     */
    void delete(String key);

    /**
     * 清空所有缓存
     */
    void flushdb();
}
