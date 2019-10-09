package com.fengdis.framework.web.service.impl;

import com.fengdis.common.utils.PageUtil;
import com.fengdis.framework.web.domain.RedisVo;
import com.fengdis.framework.web.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Zheng Jie
 * @date 2018-12-10
 */
@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    RedisTemplate redisTemplate;

    @Value("${loginCode.expiration}")
    private Long expiration;

    @Override
    public List<RedisVo> findByKey(String key,Pageable pageable){
        List<RedisVo> redisVos = new ArrayList<>();
        if(!"".equals(key)){
            key = "*" + key + "*";
        }else {
            key = "*";
        }
        for (Object s : redisTemplate.keys(key)) {
            // 过滤掉权限的缓存
            /*if (s.toString().indexOf("role::loadPermissionByUser") != -1 || s.toString().indexOf("user::loadUserByUsername") != -1) {
                continue;
            }*/
            RedisVo redisVo = new RedisVo(s.toString(),redisTemplate.opsForValue().get(s.toString()).toString());
            redisVos.add(redisVo);
        }

        Page<RedisVo> page = new PageImpl<RedisVo>(
                PageUtil.toPage(pageable.getPageNumber(),pageable.getPageSize(),redisVos),
                pageable,
                redisVos.size());

        return redisVos;
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public void flushdb() {
        redisTemplate.getConnectionFactory().getConnection().flushDb();
    }

    @Override
    public String getCodeVal(String key) {
        try {
            String value = redisTemplate.opsForValue().get(key).toString();
            return value;
        }catch (Exception e){
            return "";
        }
    }

    @Override
    public void saveCode(String key, Object val) {
        redisTemplate.opsForValue().set(key,val);
        redisTemplate.expire(key,expiration, TimeUnit.MINUTES);
    }
}
