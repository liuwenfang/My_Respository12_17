package com.zzx.blog.bean;

public class MessageBean {

    /**
     * ID : 2
     * MsgContent : 消息二
     * IsShow : 1
     * Sort : 1
     */

    private int ID;
    private String MsgContent;
    private int IsShow;
    private int Sort;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getMsgContent() {
        return MsgContent;
    }

    public void setMsgContent(String MsgContent) {
        this.MsgContent = MsgContent;
    }

    public int getIsShow() {
        return IsShow;
    }

    public void setIsShow(int IsShow) {
        this.IsShow = IsShow;
    }

    public int getSort() {
        return Sort;
    }

    public void setSort(int Sort) {
        this.Sort = Sort;
    }
}
