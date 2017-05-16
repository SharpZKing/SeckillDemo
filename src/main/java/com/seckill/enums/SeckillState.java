package com.seckill.enums;

/**
 * Created by zjfsharp on 2017/5/16.
 * 数据字典
 */
public enum  SeckillState {

    SUCCES(1,"秒杀成功"),
    ERROR(0,"秒杀结束"),
    REPEAT_KILL(-1,""),
    INNER_ERROR(-2, "系统异常"),
    DATA_REWRITE(-3,"数据篡改异常");

    private int state;

    private String stateInfo;

    SeckillState(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public static SeckillState stateOf(int index){
        for (SeckillState state: values()){
            if (state.getState() == index){
                return state;
            }
        }

        return null;
    }























}
