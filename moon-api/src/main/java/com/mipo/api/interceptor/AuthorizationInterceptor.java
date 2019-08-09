package com.mipo.api.interceptor;


import com.mipo.core.annotation.NonHandle;
import com.mipo.core.context.CContext;
import com.mipo.core.exception.RException;
import com.mipo.core.exception.ResponseCodeEnum;
import com.mipo.core.util.MD5Util;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * 不添加Login注解，直接配置url进行相关的拦截
 * 权限(Token)验证与验签
 * 验签方式md5(token+body+timestamp)
 */
@Component
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {

    private static Logger logger = LoggerFactory.getLogger(AuthorizationInterceptor.class);

    //盐值
    public static final String  SALT_FIGURE = "sgzzzz";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        CContext.getCurrentContext().init(request, handler);
        NonHandle nonHandle;
        if(handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = ((HandlerMethod) handler);
            nonHandle = handlerMethod.getMethodAnnotation(NonHandle.class);
            if(nonHandle != null && nonHandle.request()){
                return true;
            }
        }

        String token = request.getParameter("token");

        //凭证为空
        if(StringUtils.isBlank(token)){
            throw new RException(ResponseCodeEnum.PARAMTER_ERROR);
        }
        //时间戳
        String timestamp = request.getParameter("timestamp");
        //时间戳为空
        if(StringUtils.isBlank(timestamp)){
            throw new RException("时间戳缺失", 1);
        }
        //签名
        String sign = request.getParameter("sign");
        //签名为空
        if(StringUtils.isBlank(sign)){
            throw new RException("sign缺失", 2);
        }
        //请求体报文
        String body = getBodyString(request.getReader());
        //验签方式md5(token+body+timestamp)
        String rightSign = MD5Util.sign(MD5Util.sign(token+SALT_FIGURE+timestamp));
        if (!sign.equals(rightSign)){
            throw new RException(ResponseCodeEnum.PARAMTER_ERROR);
        }
        return true;
    }


    //获取request请求body内容
    public static String getBodyString(BufferedReader br) {
        String inputLine;
        StringBuffer bodyBuffer = new StringBuffer();
        try {
            while ((inputLine = br.readLine()) != null) {
                bodyBuffer.append(inputLine);
            }
            br.close();
        } catch (IOException e) {
            logger.error("获取请求体内容失败"+e.getMessage());
        }
        return bodyBuffer.toString();
    }

}
