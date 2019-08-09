package com.mipo.api.resolver;

import com.mipo.core.annotation.NonHandle;
import com.mipo.core.domain.Response;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Method;

/**
 * @Author: lyy
 * @Description:
 * @Date: 2019-08-05 17:27
 */
@ControllerAdvice
public class FormatResponseBodyAdvice implements ResponseBodyAdvice {


    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        Class<?> controllerClass = returnType.getContainingClass();
        returnType.getMethodAnnotation(ResponseBody.class);
        if (StringUtils.containsAny(returnType.getMethod().getName(),"swagger","doc")){
            return false;
        }
        return controllerClass.isAnnotationPresent(RestController.class)
                || controllerClass.isAnnotationPresent(ResponseBody.class);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        Object res;
        Method method = returnType.getMethod();
        NonHandle nonHandle = method.getAnnotation(NonHandle.class);
        if((nonHandle != null && nonHandle.response())||body instanceof Response){
            res = body;
        } else {
            res = Response.sucess(body);
        }
        return res;
    }
}
