package com.mipo.api.interceptor;

import com.mipo.api.annotation.ForbidRepeat;
import com.mipo.core.exception.RException;
import com.mipo.core.util.RedisUtils;
import com.mipo.db.damain.vo.UserToken;
import com.mipo.db.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 重复提交处理
 * 通过注解@ForbidRepeat注解使用在防重提交的方法上。
 */
@Component
public class RepeatInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RedisUtils redisUtils;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        ForbidRepeat annotation;
        if(handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = ((HandlerMethod) handler);
            annotation = handlerMethod.getMethodAnnotation(ForbidRepeat.class);
            if (annotation==null){
                return true;
            }
            int forbidSecond = annotation.value();
            String token = request.getParameter("token");
            String methodName = handlerMethod.getBeanType().getName()+"."+handlerMethod.getMethod().getName()+"_";
            UserToken userToken = tokenService.getUserToken(token);
            //先对token进行验证
            if (StringUtils.isBlank(token)|| userToken==null){
                throw new RException("TOKEN为空");
            }
            String key = methodName+userToken.getUserId();
            if (!redisUtils.lockMs(key, forbidSecond)){
                throw new RException("重复提交");
            }else {
                return true;
            }
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

}
