package com.incar.entity;

/**
 * Created by zhouyongbo on 2017/6/7.
 */
public class Result {
    private boolean status;
    private Object message;

    public Result(boolean status, Object message) {
        this.status = status;
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }
}
