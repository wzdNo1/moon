
package com.mipo.core.util;

import com.alibaba.fastjson.JSONObject;
import jodd.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

/**
 * Web工具类
 * 
 * <ul>
 * <li>
 * <b>修改历史：</b><br/>
 * <p>
 * [2015-9-8上午11:58:35]涂小炼<br/>
 * 新建
 * </p>
 * </li>
 * </ul>
 */
public class WebUtil {

	private static Logger logger = LoggerFactory.getLogger(WebUtil.class);
	/**
	 * 获得输入流内容
	 * @author 涂小炼
	 * @creationDate. 2014-12-2 上午09:48:58 
	 * @param request 请求
	 * @param encoding 编码
	 * @return 输入流内容
	 * @throws Exception
	 */
	public static String getInputStreamContent(
            HttpServletRequest request, String encoding) throws Exception {
		// 从request中取得输入流
		InputStream is = null;
		BufferedReader inReader = null;
		try {
			is = request.getInputStream();
			/*
			 * 读取xml内容
			 */
			StringBuilder content = new StringBuilder("");
			inReader = new BufferedReader(new InputStreamReader(is, encoding));
			String line = "";
			while ((line = inReader.readLine()) != null) {
				content.append(line);
			}
			return content.toString();
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			// 释放资源
			if (inReader != null) inReader.close();
			inReader = null;
			if (is != null) is.close();
			is = null;
		}
	}
	
	/**
	 * 获得真实IP地址
	 * @author 涂小炼
	 * @param request 请求
	 * @creationDate. 2011-5-9 上午09:14:17 
	 * @return 真实IP地址
	 * @throws Exception 
	 */
	public static String getIpAdrress(HttpServletRequest request) throws Exception {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
		    ip = request.getHeader("Proxy-Client-IP");
		}     
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
		    ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {     
		    ip = request.getRemoteAddr();
		}

		if ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)) { // IPv4和IPv6的localhost
			// 客户端和服务端是在同一台机器上、获取本机的IP
			InetAddress inet = InetAddress.getLocalHost();
			ip = inet.getHostAddress();
		}
		if(ip.indexOf(",")>=0){
			return ip.split(",")[0];
		}
		return ip;
	}

	/**
	 * 获取Ip地址
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String Xip = request.getHeader("X-Real-IP");
		String XFor = request.getHeader("X-Forwarded-For");
		if(StringUtils.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)){
			//多次反向代理后会有多个ip值，第一个ip才是真实ip
			int index = XFor.indexOf(",");
			if(index != -1){
				return XFor.substring(0,index);
			}else{
				return XFor;
			}
		}
		XFor = Xip;
		if(StringUtils.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)){
			return XFor;
		}
		if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
			XFor = request.getHeader("Proxy-Client-IP");
		}
		if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
			XFor = request.getHeader("WL-Proxy-Client-IP");
		}
		if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
			XFor = request.getHeader("HTTP_CLIENT_IP");
		}
		if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
			XFor = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
			XFor = request.getRemoteAddr();
		}
		return XFor;
	}
	
	/**
	 * 获得请求URL
	 * @author 涂小炼
	 * @creationDate. 2011-5-10 下午06:39:11 
	 * @param request 请求
	 * @return 请求URL
	 */
	public static String getRequestURL(HttpServletRequest request) {
		StringBuilder url = new StringBuilder(request.getRequestURI());
		Map<String, String[]> parameterMap = request.getParameterMap();
		String key = null;
		String[] value = null;
		if (parameterMap != null && parameterMap.size() > 0) {
			url.append("?");
			for(Iterator<?> it = parameterMap.keySet().iterator(); it.hasNext(); ) {
				key = (String)it.next();
				value = parameterMap.get(key);
				if (value != null && value.length > 0) {
					for (String val : value) {
						url.append(key).append("=").append(val).append("&");
					}
				}
			}
			url.delete(url.length() - 1, url.length());
		}
		return url.toString();
	}
	
	/**
	 * 获得基路径
	 * @author 涂小炼
	 * @creationDate. 2012-7-5 下午10:08:02 
	 * @param request 请求
	 * @return 基路径
	 */
	public static String getBasePath(HttpServletRequest request) {
		int port = request.getServerPort();
		return request.getScheme() + "://" + request.getServerName()
					+ ((port == 80) ? "" : (":" + port)) + request.getContextPath() + "/";
	}
	
	/**
	 * 获得基路径   不带最后的  /
	 * @author 涂小炼
	 * @creationDate. 2012-7-5 下午10:08:02 
	 * @param request 请求
	 * @return 基路径
	 */
	public static String getBasePathUrl(HttpServletRequest request) {
		int port = request.getServerPort();
		return request.getScheme() + "://" + request.getServerName()
					+ ((port == 80) ? "" : (":" + port)) + request.getContextPath();
	}
	
	/**
	 * 是否为Ajax请求
	 * @author 涂小炼
	 * @creationDate. 2011-8-24 下午08:46:46 
	 * @param request 请求
	 * @return 布尔
	 */
	public static boolean isAjaxRequest(HttpServletRequest request) {
		/*
		 * iframe提交特殊处理，当作ajax请求处理
		 */
		String iframe = (String) request.getParameter("iframe");
		boolean isIframe = (StringUtils.isNotEmpty(iframe)) ? Boolean.valueOf(iframe) : false;
		if (isIframe) return true;
		return (request.getHeader("X-Requested-With") == null) ? false : true;
	}
	
	/**
	 * 向客户端输出
	 * @author 涂小炼
	 * @creationDate. 2010-9-18 上午00:19:16 
	 * @response 响应对象
	 * @param outObj 输出的Object
	 * @param outEncoding 输出编码
	 * @throws IOException 
	 */
	public static void write(HttpServletResponse response, Object outObj, String outEncoding) throws IOException {
		write(response, outObj, outEncoding, "text/html");
		
	}
	
	/**
	 * 向客户端输出
	 * @author 涂小炼
	 * @creationDate. 2010-9-18 上午00:19:16 
	 * @response 响应对象
	 * @param outObj 输出的Object
	 * @param outEncoding 输出编码
	 * @throws IOException 
	 */
	public static void write(HttpServletResponse response, Object outObj, String outEncoding, String contentType) throws IOException {
		// 设置默认响应类型
		if (response.getContentType() == null) response.setContentType(contentType);
		response.setCharacterEncoding(outEncoding);
		PrintWriter out = null;
		out = response.getWriter();
		out.print(outObj);
		out.flush();
		out.close();
	}

	public static void write(HttpServletResponse response, Object outObj)  {
		// 设置默认响应类型
		String contentType = "application/json";
		String outEncoding = "utf-8";
		if (response.getContentType() == null) response.setContentType(contentType);
		response.setCharacterEncoding(outEncoding);
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.write(JSONObject.toJSONString(outObj));
		} catch (IOException e) {
			logger.error(e.getMessage());
		}finally {
			if(out != null){
				out.close();
			}
		}

	}
	
	/**
	 * 
	 */
	public static String httpget(String url,String param){
		BufferedReader in = null;
		String result = "";
		try {
            String rurl = url;
			if(param != null && !StringUtil.isBlank(param)) {
                rurl += "?" + param;
            }
			URL urls = new URL(rurl);
			HttpURLConnection conn = (HttpURLConnection)urls.openConnection();
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setRequestProperty("charset", "utf-8");
			conn.connect();
//			Map<String, List<String>> map = conn.getHeaderFields();
//            // 遍历所有的响应头字段
//            for (String key : map.keySet()) {
//                Logger.DEBUG(key + "--->" + map.get(key));
//            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
            		conn.getInputStream(),"utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
		return result;
	}
	
	/**  
     * 通过IP地址获取MAC地址  
     * @param ip String,127.0.0.1格式  
     * @return mac String  
     * @throws Exception  
     */    
    public String getMACAddress(String ip) throws Exception {    
        String line = "";    
        String macAddress = "";    
        final String MAC_ADDRESS_PREFIX = "MAC Address = ";    
        final String LOOPBACK_ADDRESS = "127.0.0.1";    
        //如果为127.0.0.1,则获取本地MAC地址。    
        if (LOOPBACK_ADDRESS.equals(ip)) {    
            InetAddress inetAddress = InetAddress.getLocalHost();    
            //貌似此方法需要JDK1.6。    
            byte[] mac = NetworkInterface.getByInetAddress(inetAddress).getHardwareAddress();    
            //下面代码是把mac地址拼装成String    
            StringBuilder sb = new StringBuilder();    
            for (int i = 0; i < mac.length; i++) {    
                if (i != 0) {    
                    sb.append("-");    
                }    
                //mac[i] & 0xFF 是为了把byte转化为正整数    
                String s = Integer.toHexString(mac[i] & 0xFF);    
                sb.append(s.length() == 1 ? 0 + s : s);    
            }    
            //把字符串所有小写字母改为大写成为正规的mac地址并返回    
            macAddress = sb.toString().trim().toUpperCase();    
            return macAddress;    
        }    
        //获取非本地IP的MAC地址    
        try {    
            Process p = Runtime.getRuntime().exec("nbtstat -A " + ip);    
            InputStreamReader isr = new InputStreamReader(p.getInputStream());    
            BufferedReader br = new BufferedReader(isr);    
            while ((line = br.readLine()) != null) {    
                if (line != null) {    
                    int index = line.indexOf(MAC_ADDRESS_PREFIX);    
                    if (index != -1) {    
                        macAddress = line.substring(index + MAC_ADDRESS_PREFIX.length()).trim().toUpperCase();    
                    }    
                }    
            }    
            br.close();    
        } catch (IOException e) {    
            e.printStackTrace(System.out);    
        }    
        return macAddress;    
    } 
}
