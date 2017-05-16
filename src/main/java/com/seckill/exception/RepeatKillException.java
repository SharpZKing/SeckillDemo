package com.seckill.exception;

/**
 * Created by zjfsharp on 2017/5/16.
 * 重复秒杀异常(运行期异常:不需要try...catch )
 */
public class RepeatKillException extends SeckillException{

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }

    public RepeatKillException(String message) {
        super(message);
    }
}
