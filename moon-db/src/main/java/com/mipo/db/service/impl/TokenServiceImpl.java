package com.mipo.db.service.impl;

import com.mipo.core.util.RedisUtils;
import com.mipo.db.damain.vo.UserToken;
import com.mipo.db.service.TokenService;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {

    private int expiredHour = 2;

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    RedisTemplate redisTemplate;




    @Override
    public UserToken getUserToken(String token){
        String keyToken = ""+token;
        UserToken userToken  = redisUtils.get(keyToken,UserToken.class, NumberUtils.toLong(String.valueOf(expiredHour * 3600),3600));
        if(userToken != null){
            getTokenById(userToken.getUserId());
        }
        return userToken;
    }

    @Override
    public void clear(Long userId) {
            String token = getTokenById(userId);
            if(token == null){
                return;
            }

            delBean(token);
            delToken(userId);
    }

    @Override
    public UserToken getUserTokenById(Long userId) {
        String token = getTokenById(userId);
        if(StringUtils.isBlank(token)){
            return null;
        }
        return getUserToken(token);
    }

    @Override
    public String getTokenById(Long userId) {
        String keyId = ""+userId;
        return redisUtils.get(keyId, NumberUtils.toLong(String.valueOf(expiredHour * 3600),3600));

    }

    private void delBean(String token){
        redisUtils.delete(""+token);
    }

    private void delToken(Long userId){
        redisUtils.delete(""+userId);
    }

}
