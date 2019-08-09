package com.mipo.core.config;


import com.mipo.core.domain.Response;
import com.mipo.core.exception.RException;
import com.mipo.core.exception.ResponseCodeEnum;
import com.mipo.core.util.ResponseUtil;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;


/**
 * 统一错误处理
 */
@ControllerAdvice
public class GlobalExceptionHandler implements ErrorController {

    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public Object badArgumentHandler(IllegalArgumentException e) {
        e.printStackTrace();
        logger.error(e.getMessage());
        return ResponseUtil.badArgumentValue();
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseBody
    public Object badArgumentHandler(MethodArgumentTypeMismatchException e) {
        e.printStackTrace();
        logger.error(e.getMessage());
        return ResponseUtil.badArgumentValue();
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public Object badArgumentHandler(MissingServletRequestParameterException e) {
        e.printStackTrace();
        logger.error(e.getMessage());
        return ResponseUtil.badArgumentValue();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public Object badArgumentHandler(HttpMessageNotReadableException e) {
        e.printStackTrace();
        logger.error(e.getMessage());
        return ResponseUtil.badArgumentValue();
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseBody
    public Object badArgumentHandler(ValidationException e) {
        e.printStackTrace();
        logger.error(e.getLocalizedMessage());
        if (e instanceof ConstraintViolationException) {
            ConstraintViolationException exs = (ConstraintViolationException) e;
            Set<ConstraintViolation<?>> violations = exs.getConstraintViolations();
            for (ConstraintViolation<?> item : violations) {
                String message = ((PathImpl) item.getPropertyPath()).getLeafNode().getName() + item.getMessage();
                return ResponseUtil.fail(402, message);
            }
        }
        logger.error(e.getLocalizedMessage());
        return ResponseUtil.badArgumentValue();
    }

    @ExceptionHandler(RException.class)
    @ResponseBody
    public Object rExceptionHandler(RException e) {
        logger.error( getExcptionPoint(e));
        return ResponseUtil.fail(e.getCode(),e.getMsg());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object seriousHandler(Exception e) {
        logger.error(getExcptionPoint(e));
        return ResponseUtil.serious();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Object validHandler(MethodArgumentNotValidException e){
        AtomicReference<String> message = new AtomicReference<>("");
        List<FieldError> errors = e.getBindingResult().getFieldErrors();
        errors.stream().forEach((item)->{
            message.set(message + item.getDefaultMessage());
        });
        logger.error(getExcptionPoint(e) +message.get());
        return  Response.fail(ResponseCodeEnum.PARAMTER_ERROR.getCode(), message.get());
    }
    @Override
    public String getErrorPath() {
        return null;
    }

    private String getExcptionPoint(Exception e){

        StackTraceElement stackTraceElement = e.getStackTrace()[0];
        String s =stackTraceElement.toString() + "==" + e.getMessage();
        if(!(e instanceof RException) && !(e instanceof MethodArgumentNotValidException) && !(e instanceof HttpRequestMethodNotSupportedException)){
            logger.error("捕获到错误信息：", e);
        }
        return s;
    }
}
