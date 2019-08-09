package com.mipo.core.exception;

/**
 * 常用返回码
 */
public enum ResponseCodeEnum {

    SUCCESS(100,"成功"),
    PARAMTER_ERROR(1003,"参数错误");

    private int code;

    private String message;

     ResponseCodeEnum(Integer code, String message){
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
