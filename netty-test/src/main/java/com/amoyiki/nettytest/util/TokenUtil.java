package com.amoyiki.nettytest.util;

import com.amoyiki.nettytest.constant.TokenConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author amoyiki
 * @since 2019/3/25
 */
@Component
@Slf4j
public class TokenUtil {
    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 验证 token 是否存在 redis
     *
     * @param token
     * @return boolean
     * @author amoyiki
     */
    public boolean checkToken(String token) {
        if(token.endsWith(TokenConstant.TOKEN_SUFFIX_APP)){
//            log.info("{}",redisTemplate.opsForValue().get(TokenConstant.TOKEN_PREFIX_APP+":"+token));
            return redisTemplate.opsForValue().get(TokenConstant.TOKEN_PREFIX_APP+":"+token) != null;
        }else{
            return redisTemplate.opsForValue().get(TokenConstant.TOKEN_PREFIX_WEB+":"+token) != null;
        }
    }
}
