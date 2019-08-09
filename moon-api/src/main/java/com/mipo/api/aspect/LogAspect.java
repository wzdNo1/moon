package com.mipo.api.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mipo.api.annotation.SysLogger;
import com.mipo.core.context.ApplicationContextHelper;
import com.mipo.core.exception.RException;
import com.mipo.core.util.IPUtils;
import com.mipo.db.damain.entity.SysLog;
import com.mipo.db.damain.vo.UserToken;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Aspect
@Component
public class LogAspect {
	private final static Logger logger = LoggerFactory.getLogger(LogAspect.class);

	ThreadLocal<HttpServletRequest> threadLocal = new ThreadLocal<>();

	@Pointcut("execution(* com.mipo.api.controller.*.*(..))")
	public void webLog() {}

	/**
	 * 注解指定保存日志
	 */
	@Pointcut("@annotation(com.mipo.api.annotation.SysLogger)")
	public void logPointCut() {}

	@Before("webLog()")
	public void deBefore(JoinPoint joinPoint) throws Throwable {
		// 接收到请求，记录请求内容
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
		threadLocal.set(request);
	}



	// 后置异常通知
	@AfterThrowing(pointcut = "webLog()",throwing = "e")
	public void throwss(JoinPoint joinPoint,Throwable e) {
		if(e instanceof RException || e instanceof MethodArgumentNotValidException || e instanceof HttpRequestMethodNotSupportedException){
			logger.info("捕获到错误信息:{},请求地址：{} ",e.getMessage(),threadLocal.get().getRequestURI());
		}else{
			logger.error("捕获到错误信息:{}\n请求地址：{}\n错误详情:{}",e.getMessage(),threadLocal.get().getRequestURL(),StringUtils.join(e.getStackTrace(), "\n"));
			StringBuilder builder = new StringBuilder();
			builder.append("捕获到错误信息:").append(e.getMessage()).append("\n请求地址：").append(threadLocal.get().getRequestURI()).append("\n错误详情:").append(StringUtils.join(e.getStackTrace(), "\n"));
			logger.error("方法异常时执行.....结束");
		}
	}
	@Around("webLog()")
	public Object aroundWebLog(ProceedingJoinPoint point) throws Throwable {
		long beginTime = System.currentTimeMillis();
		//执行方法
		Object result = point.proceed();
		List<Object> list = new ArrayList<>();
		if(point.getArgs() != null && point.getArgs().length >0){
			for (Object object:point.getArgs()){
				if(!(object instanceof ServletRequest)){
					list.add(object);
				}
			}
		}
		//执行时长(毫秒)
		Double time = (double)(System.currentTimeMillis() - beginTime);

		logger.info("ip:{},URI:{},请求耗时{}ms,参数:{},返回结果:{}", IPUtils.getIpAddr(threadLocal.get()),
				threadLocal.get().getRequestURI(),
				time, JSON.toJSONString(list),
                result == null?null: JSON.toJSONString(result));

		//保存日志
		//saveSysLog(point, time);
		return result;
	}

	@Around("logPointCut()")
	public Object aroundLog(ProceedingJoinPoint point) throws Throwable {
		long beginTime = System.currentTimeMillis();
		Object result = point.proceed();
		long time = System.currentTimeMillis() - beginTime;

		//保存日志
		saveSysLog(point, time);
		return result;
	}
	private void saveSysLog(ProceedingJoinPoint joinPoint, long time) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();

		SysLog sysLog = new SysLog();
		SysLogger syslogger = method.getAnnotation(SysLogger.class);
		if(syslogger != null){
			//注解上的描述
			sysLog.setOperation(syslogger.value());
		}

		//请求的方法名
		String className = joinPoint.getTarget().getClass().getName();
		String methodName = signature.getName();
		sysLog.setMethod(className + "." + methodName + "()");

		//请求的参数
		Object[] args = joinPoint.getArgs();
		UserToken userToken = null;
		for (int i =0 ;i<args.length;i++){
			if (args[i] instanceof UserToken){
				userToken = (UserToken) args[i];
				String userName = userToken.getName();
				sysLog.setUsername(userName);
				sysLog.setUserId(userToken.getUserId());
			}else {
				String params = JSONObject.toJSONString(args[i]);
				sysLog.setParams(params);
			}
		}
		//获取request
		HttpServletRequest request = ApplicationContextHelper.getHttpServletRequest();
		//设置IP地址
		sysLog.setIp(IPUtils.getIpAddr(request));

		sysLog.setTime((int)time);
		sysLog.setCreateDate(new Date());
		List<SysLog> list = new ArrayList<>();
		//保存系统日志
		//asyncService.insertSelective(sysLog);
	}

}
