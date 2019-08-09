package com.mipo.core.exception;

/**
 * 自定义异常
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年10月27日 下午10:11:27
 */
public class RException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
    private String message;
    private int code = 500;
    
    public RException(String msg) {
		super(msg);
		this.message = msg;
	}
	
	public RException(String msg, Throwable e) {
		super(msg, e);
		this.message = msg;
	}
	
	public RException(String msg, int code) {
		super(msg);
		this.message = msg;
		this.code = code;
	}
	
	public RException(String msg, int code, Throwable e) {
		super(msg, e);
		this.message = msg;
		this.code = code;
	}

	public RException(ResponseCodeEnum codeEnum) {
		super(codeEnum.getMessage());
		this.message = codeEnum.getMessage();
		this.code = codeEnum.getCode();
	}

	public String getMsg() {
		return message;
	}

	public void setMsg(String msg) {
		this.message = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
	
}
