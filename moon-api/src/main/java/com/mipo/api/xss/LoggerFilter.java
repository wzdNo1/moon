package com.mipo.api.xss;


import com.mipo.core.util.IPUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;

@Slf4j
public class LoggerFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse res,
                         FilterChain chain) throws IOException, ServletException {
        Date reqTime = new Date();
        String method = "GET";
        HttpServletRequest requestWrapper = null;
        HttpServletResponseWrapper responseWrapper = null;
        String url = "";
        String urlParam = "";
        String ip="";
        if (servletRequest instanceof HttpServletRequest) {
            method = ((HttpServletRequest) servletRequest).getMethod();
            requestWrapper = new HttpServletRequestWrapper((HttpServletRequest) servletRequest);  //替换
            responseWrapper = new HttpServletResponseWrapper((HttpServletResponse) res); //response替换
            url = ((HttpServletRequest) servletRequest).getServletPath();
            urlParam = ((HttpServletRequest) servletRequest).getQueryString();
            ip= IPUtils.getIpAddr((HttpServletRequest) servletRequest);
        }

        String param = this.getBodyString(requestWrapper.getReader());
        log.info(url + "请求方法" + method + "，请求参数" + param);
        chain.doFilter(servletRequest, responseWrapper);
        //chain.doFilter(servletRequest, responseWrapper);
        String resStr = getResponseString(responseWrapper, res);
        if(!url.matches("(/v2/.*)|(/swagger.*)|(/webjars.*)|(/configuration.*)")) {
            Date respTime = new Date();
            double m= (double)(respTime.getTime() - reqTime.getTime())/1000;
            log.info("ip:{},URL:{}?{},请求耗时{}秒,参数:{},返回结果:{}",ip, url, urlParam, m, param, resStr);
            if(m>5){
                String elog = String.format("请求耗时%s秒\nurl%s参数:%s", m,url, param);
                log.error(elog);
            }
        }
    }


    @Override
    public void destroy() {
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
            log.error("IOException: " + e);
        }
        return bodyBuffer.toString();
    }

    public static String getResponseString(HttpServletResponseWrapper httpServletResponseWrapper, ServletResponse response) throws IOException {
        String result = null;
        byte[] content = httpServletResponseWrapper.getContent();
        if (content.length > 0) {
            result = new String(content, "UTF-8");
        }
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(content);
        outputStream.flush();
        return result;
    }

}
