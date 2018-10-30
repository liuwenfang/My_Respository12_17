package com.zzx.blog.http;

/**
 * 获取json数据的基类
 * Created by liuzhu
 * on 2017/5/19.
 */

public class BaseBean<T> {

    /**
     * msg : 成功
     * obj : {"address":"合肥","createTime":"2018-05-27 10:58:02","email":"","hotelId":2,"hotelName":"大酒店","id":2,"password":"e10adc3949ba59abbe56e057f20f883e","phone":"13075569283","postId":0,"realName":"陈","sex":"MAN","token":"5842703bf5194d79ba9cfa4865c23d09","userName":"13075569283"}
     * result : 0
     */

    private String msg;
    private T obj;
    private int result;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

}
