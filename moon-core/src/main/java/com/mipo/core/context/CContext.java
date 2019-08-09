package com.mipo.core.context;

import com.mipo.core.exception.RException;
import com.mipo.core.util.IPUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 解析请求信息
 */
@Slf4j
public class CContext {

  protected static final ThreadLocal<CContext> THREAD_LOCAL = new ThreadLocal<CContext>() {
    @Override
    protected CContext initialValue() {
      return new CContext();
    }
  };


  private Object handler;

  private long requestStartTime;

  protected HttpServletRequest httpServletRequest;

  private int requestHashCode;

  private String requestIP;

  private String requestURI;

  private String urlParamStr;

  private String postParamStr;

  private String partnerJsonStr;

  private String partnerAppNo;

  private String onepayPrivateKey;

  private String partnerTokenStr;

  private String managerTokenStr;

  private String env;

  private boolean multipartRequest;


  protected CContext() {

  }

  /**
   * 创建context
   */
  public static CContext createContext() {
    CContext xContext = new CContext();
    THREAD_LOCAL.set(xContext);
    return xContext;
  }

  /**
   * 获取当前Context
   */
  public static CContext getCurrentContext() {
    return THREAD_LOCAL.get();
  }


  /**
   * 在Interceptor中使用，初始化参数
   */
  public void init(HttpServletRequest request, Object handler) {
    this.setRequestStartTime(System.currentTimeMillis());
    this.httpServletRequest = request;
    this.handler = handler;
    this.requestHashCode = request.hashCode();
    this.requestIP = parseIp(request);
    this.requestURI = request.getRequestURI();
    this.urlParamStr = request.getQueryString();
    this.postParamStr = this.initPost(request);
  }

  private String parseIp(HttpServletRequest request){
    return IPUtils.getIpAddr(request);
  }

  private String initPost(HttpServletRequest request){

    String postStr;
    try {
      postStr = IOUtils.toString(request.getInputStream(), "UTF-8");
    } catch (IOException e) {
      e.printStackTrace();
      throw new RException("");
    }
    return postStr;
  }

  /**
   * 获取请求对应的URL
   */
  public String getServiceUrl() {
    String queryString = httpServletRequest.getQueryString();
    if (StringUtils.isNotEmpty(queryString)) {
      return httpServletRequest.getRequestURI() + "?" + queryString;
    } else {
      return httpServletRequest.getRequestURI();
    }
  }

  /**
   * Method 为POST时候以JSON方式传递参数
   */
  public String getPostJsonBodyStr() {
    if (StringUtils.isEmpty(postParamStr)) {
      postParamStr = "{}";
    }

    return postParamStr;
  }

  public void setPostJsonBodyStr(String postParamStr){
    this.postParamStr = postParamStr;
  }
  /**
   * 判断请求类型是否为Post
   *
   * @return 是否为Post请求
   */
  public boolean isPostRequest() {
    if (httpServletRequest == null) {
      throw new RException("");
    }
    String method = httpServletRequest.getMethod();
    boolean isPost = method.equals(HttpMethod.POST.name()) ||  method.equals(HttpMethod.PUT.name());

    return isPost && isJsonRequest();
  }

  /**
   * 判断请求类型是否为Json
   */
  public boolean isJsonRequest() {
    if (httpServletRequest == null) {
      throw new RException("");
    }

    String contentType = httpServletRequest.getContentType();
    if(contentType == null){
      return false;
    }

    if (contentType.contains("application/json") || contentType.contains("text/json")) {
      return true;
    }

    return false;
  }


  /**
   * 清理当前线程中的数据
   */
  public void clear() {
    this.requestStartTime = 0;
    this.httpServletRequest = null;
    this.postParamStr = null;
    this.urlParamStr = null;
    this.requestHashCode = 0;
  }

  public static void clearCurrentContext() {
    CContext context = getCurrentContext();
    if (context != null) {
      context.clear();
    }
    THREAD_LOCAL.remove();
  }

  public Object getHandler() {
    return handler;
  }

  public void setHandler(Object handler) {
    this.handler = handler;
  }

  public String getIP() {
    return requestIP;
  }

  public int getRequestHashCode() {
    return requestHashCode;
  }

  public void setRequestHashCode(int requestHashCode) {
    this.requestHashCode = requestHashCode;
  }

  public long getRequestStartTime() {
    return requestStartTime;
  }

  public void setRequestStartTime(long requestStartTime) {
    this.requestStartTime = requestStartTime;
  }

  public String getRequestURI() {
    return requestURI;
  }

  public void setRequestURI(String requestURI) {
    this.requestURI = requestURI;
  }

  public String getPartnerJsonStr() {
    return partnerJsonStr;
  }

  public void setPartnerJsonStr(String partnerJsonStr) {
    this.partnerJsonStr = partnerJsonStr;
  }

  public String getPartnerAppNo() {
    return partnerAppNo;
  }

  public void setPartnerAppNo(String partnerAppNo) {
    this.partnerAppNo = partnerAppNo;
  }

  public String getOnepayPrivateKey() {
    return onepayPrivateKey;
  }

  public void setOnepayPrivateKey(String onepayPrivateKey) {
    this.onepayPrivateKey = onepayPrivateKey;
  }

  public String getPartnerTokenStr() {
    return partnerTokenStr;
  }

  public void setPartnerTokenStr(String partnerTokenStr) {
    this.partnerTokenStr = partnerTokenStr;
  }

  public String getEnv() {
    return env;
  }

  public void setEnv(String env) {
    this.env = env;
  }

  public boolean isMultipartRequest() {
    return multipartRequest;
  }

  public void setMultipartRequest(boolean multipartRequest) {
    this.multipartRequest = multipartRequest;
  }

  public String getManagerTokenStr() {
    return managerTokenStr;
  }

  public void setManagerTokenStr(String managerTokenStr) {
    this.managerTokenStr = managerTokenStr;
  }
}
