package com.mipo.db.service;


import com.mipo.db.damain.vo.UserToken;

public interface TokenService {


   UserToken getUserToken(String token);


    /**
     * 清除用户缓存信息，根据token清除
     * @param userId
     */
   void clear(Long userId);

    UserToken getUserTokenById(Long userId);

    String getTokenById(Long userId);
}
