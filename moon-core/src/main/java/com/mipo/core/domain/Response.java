package com.mipo.core.domain;

public class Response<T> {

    private int code;

    private String message;

    private T data;

    public Response() {
    }

    public Response(int code) {
        this.code = code;
    }

    public Response(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Response(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static  <R> Response<R> sucess(R data){
        Response<R> response = new Response<>(100,"成功", data);
        return response;
    }
    public static  <R> Response<R> sucess(int code , String message, R data){
        Response<R> response = new Response<>(code,message, data);
        return response;
    }

    public static Response<String> sucess(int code, String message){
        Response<String> response = new Response(code,message);
        return response;
    }

    public static Response<String> fail(){
        Response<String> response = new Response<>();
        response.setCode(500);
        response.setMessage("服务器错误错误");
        return response;
    }

    public static Response<String> fail(String message){
        Response<String> response = new Response<>();
        response.setCode(500);
        response.setMessage(message);
        return response;
    }

    public static Response<String> fail(int code , String message){
        Response<String> response = new Response<>();
        response.setCode(code);
        response.setMessage(message);
        return response;
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
