package com.mipo.api.interceptor;

import com.mipo.api.annotation.FiltrationContent;
import com.mipo.core.exception.RException;
import com.mipo.core.util.RedisUtils;
import com.mipo.db.damain.vo.UserToken;
import com.mipo.db.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * 文本，内容过滤
 * 通过注解@FiltrationContent 注解使用在防重提交的方法上。
 */
@Slf4j
@Component
public class FiltrationContentceptor implements HandlerInterceptor {

    @Autowired
    private TokenService tokenService;
    @Autowired
    RedisUtils redisUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(true){
            return true;
        }
        FiltrationContent annotation;
        if(handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = ((HandlerMethod) handler);
            annotation = handlerMethod.getMethodAnnotation(FiltrationContent.class);
            //未使用FiltrationContent注解返回即可
            if (annotation==null||annotation.value()==0){
                return true;
            }
            String token = request.getParameter("token");
            UserToken userToken = tokenService.getUserToken(token);
            if (userToken !=null&&userToken.getUserId()!=null){
                if(redisUtils.get(userToken.getUserId()+"五分钟")!=null){
                    throw new RException("你之前发布敏感信息，此功能禁用5分钟！", 1003);
                }
            }

            String param = "";
            param = this.getBodyString(request.getReader());
            if(param.length()>2){
                param =param.substring(1,param.length()-1);
            }
            if(false){ // 查询文本有没有敏感信息
                long userId = 0;
                if (userToken !=null&&userToken.getUserId()!=null){
                    userId =userToken.getUserId();
                    // 敏感信息入缓存 时长五分钟
                    //redisUtils.set(RedisKeyUtils.checkKey(userId),"1",5 * TimeConstants.MINUTE_S);
                }
                log.info("--TextCheckContentQuery---用户ID{},内容{}",userId,param);
                throw new RException("内容包含敏感信息！", 1003);
            }
            return true;

        }else{
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    //获取request请求body中参数
    public static String getBodyString(BufferedReader br) {
        String inputLine;
        StringBuffer bodyBuffer = new StringBuffer();
        try {
            while ((inputLine = br.readLine()) != null) {
                bodyBuffer.append(inputLine);
            }
            br.close();
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        }
        return bodyBuffer.toString();
    }



}
