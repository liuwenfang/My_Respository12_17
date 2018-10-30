package com.zzx.blog.http;


import java.io.Serializable;

/**
 * Created by Administrator on 2016/1/26.
 */
public class ServerData implements Serializable {

    private String message;
    private String data;
    private int result;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
