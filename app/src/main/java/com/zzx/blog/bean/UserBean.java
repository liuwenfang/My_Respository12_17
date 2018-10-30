package com.zzx.blog.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/14.
 */
public class UserBean implements Serializable {


    /**
     * issmrz : 0
     * token : EF94F63281CF90DF8F3E62E58BF7A93B
     */

    private int issmrz; //是否实名认证  0 认证成功  1 认证失败
    private String token;//
    /**
     * nickName : a
     * mobile : 18655050310
     * limit : null
     * realName : null
     */

    private String nickName;//昵称
    private String mobile;//手机号
    private String limit;//额度
    private String realName;//真实姓名

    public int getIssmrz() {
        return issmrz;
    }

    public void setIssmrz(int issmrz) {
        this.issmrz = issmrz;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
}
