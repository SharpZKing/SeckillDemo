package com.seckill.exception;

/**
 * Created by zjfsharp on 2017/5/16.
 */
public class SeckillException extends RuntimeException{

    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}
