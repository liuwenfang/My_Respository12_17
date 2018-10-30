package com.zzx.blog.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/7/2 0002.
 */

public class BlogBean implements Serializable{

    /**
     * ID : 1
     * UserName : 小鹏
     * Title : 标题1
     * AddTime : 2018-10-18 10:10:25
     * CommentCount : 1
     * TimeSpan : 6天前
     */

    private int ID;
    private String UserName;
    private String Title;
    private String AddTime;
    private int CommentCount;
    private String TimeSpan;
    private String Status;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getAddTime() {
        return AddTime;
    }

    public void setAddTime(String AddTime) {
        this.AddTime = AddTime;
    }

    public int getCommentCount() {
        return CommentCount;
    }

    public void setCommentCount(int CommentCount) {
        this.CommentCount = CommentCount;
    }

    public String getTimeSpan() {
        return TimeSpan;
    }

    public void setTimeSpan(String TimeSpan) {
        this.TimeSpan = TimeSpan;
    }
}
