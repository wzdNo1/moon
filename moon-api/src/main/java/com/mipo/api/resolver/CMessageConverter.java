package com.mipo.api.resolver;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.mipo.core.context.CContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class CMessageConverter extends AbstractHttpMessageConverter<Object> {

  /**
   * 输出日期格式设置为dd-MM-yyyy HH:mm:ss
   */
  static {
    JSONObject.DEFFAULT_DATE_FORMAT = "dd-MM-yyyy HH:mm:ss";
  }

  private final String NORMAL_LOG_PATTERN = ">>>[HashCode=%s]请求,返回数据:{%s},request执行时间为{%d}";
  private final String ERROR_LOG_PATTERN = ">>>[HashCode=%s]请求[%s]发生异常:%s";
  public static final String URLS = "urls";


  public final static Charset UTF8 = Charset.forName("UTF-8");
  private static final Logger logger = LoggerFactory.getLogger(CMessageConverter.class);


  private Charset charset = UTF8;

  private SerializerFeature[] serializerFeature;


  public CMessageConverter(String charset) {
    super(new MediaType("application", "json", Charset.forName(charset)),
            new MediaType("application", "*+json", Charset.forName(charset)));
    this.charset = Charset.forName(charset);
  }

  public CMessageConverter() {
    super(new MediaType("application", "json", UTF8), new MediaType("application", "*+json", UTF8));
  }

  public Charset getCharset() {
    return this.charset;
  }

  public void setCharset(Charset charset) {
    this.charset = charset;
  }

  public SerializerFeature[] getFeatures() {
    return serializerFeature;
  }

  public void setFeatures(SerializerFeature... features) {
    this.serializerFeature = features;
  }

  @Override
  protected boolean supports(Class<?> clazz) {
    System.out.println(1);
    return true;
  }

  @Override
  protected Object readInternal(Class<? extends Object> clazz, HttpInputMessage inputMessage)
          throws IOException, HttpMessageNotReadableException {
    System.out.println(2);
    String jsonBody = CContext.getCurrentContext().getPostJsonBodyStr();
    return JSON.parseObject(jsonBody, clazz);
  }


  @Override
  protected void writeInternal(Object obj, HttpOutputMessage outputMessage)
          throws IOException, HttpMessageNotWritableException {
    System.out.println(3);
    CContext context = CContext.getCurrentContext();
    Object result;

    try {
      long spentTime = System.currentTimeMillis() - context.getRequestStartTime();
      logger.debug(String
              .format(NORMAL_LOG_PATTERN, context.getRequestHashCode(), JSON.toJSONString(obj),
                      spentTime));
     result = 321;

    } catch (Exception ex) {
      result = "123";
      String errorLog = String.format(ERROR_LOG_PATTERN, context.getRequestHashCode(),
              context.getServiceUrl(), ex.getMessage());
      logger.error(errorLog, ex);
    }

    byte[] jsonBytes = convertToJsonBytes(result);
    OutputStream out = outputMessage.getBody();
    out.write(jsonBytes);

  }

  private byte[] convertToJsonBytes(Object obj) {
    if (null == obj) {
      return "\"\"".getBytes(charset);
    } else if (obj instanceof String) {
      String value = (String) obj;
      if (value.equals(StringUtils.EMPTY)) {
        return "\"\"".getBytes(charset);
      } else {
        return value.getBytes(charset);
      }
    } else {
      if (serializerFeature != null) {
        return JSON.toJSONBytes(obj, serializerFeature);
      } else {
        return JSON.toJSONBytes(obj);
      }
    }
  }



}

