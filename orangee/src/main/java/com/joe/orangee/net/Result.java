package com.joe.orangee.net;

/**
 * @param <T>
 */
public class Result<T> {
    //正常
    public static final int OK = 0;
    
    //错误码
    public static final int NETWORK_INVALID = -100;
    public static final int NETWORK_TIMEOUT = -200;
    public static final int JSON_DATA_ERROR = -300;
    public static final int JSON_PARSE_ERROR = -300;
    
    private T result;
    private int errorCode;

    public Result(T result) {
        this.result = result;
    }

    public Result(T result, int errorCode) {
        this.result = result;
        this.errorCode = errorCode;
    }
    
    public boolean isSucceeded() {
        return errorCode == OK;
    }

    public int getErrorCode() {
        return errorCode;
    }


    public T getResult() {

        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

}
