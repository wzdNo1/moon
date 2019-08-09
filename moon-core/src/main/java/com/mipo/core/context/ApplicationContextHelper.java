package com.mipo.core.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Spring Context 工具类
 */
@Component
public class ApplicationContextHelper implements ApplicationContextAware {
	public static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		ApplicationContextHelper.applicationContext = applicationContext;
	}

	public static Object getBean(String name) {
		return applicationContext.getBean(name);
	}

	public static <T> T getBean(String name, Class<T> requiredType) {
		return applicationContext.getBean(name, requiredType);
	}

	public static boolean containsBean(String name) {
		return applicationContext.containsBean(name);
	}

	public static boolean isSingleton(String name) {
		return applicationContext.isSingleton(name);
	}

	public static Class<? extends Object> getType(String name) {
		return applicationContext.getType(name);
	}

	/**
	 * 获取httpRequest对象
	 * @return
	 */
	public static HttpServletRequest getHttpServletRequest() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}
	/**
	 * 获取httpResponse对象
	 * @return
	 */
	public static HttpServletResponse getHttpServletReponse() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
	}
	/**
	 *获取session
	 * @return
	 */
	public static HttpSession getHttpSession(){
		return  ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getSession();
	}

	/**
	 * 从session中获取某一对象
	 */
	public static Object getAttribute(String key){
		return getHttpSession().getAttribute(key);
	}
}
