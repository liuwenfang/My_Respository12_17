package com.zzx.blog.bean;

import java.util.List;

/**
 * Created by urnotxx on 2018/10/24.
 */

public class BlogDetailBean {

    /**
     * ID : 1
     * UserName : 小鹏
     * Title : 标题1
     * AddTime : 2018-10-18 10:10:25
     * Content : 内容1
     * CommentList : [{"CommentID":1,"CommentDate":"2018-10-20 11:33:58","CommentName":null,"CommentContent":"评论1"}]
     */

    private int ID;
    private String UserName;
    private String Title;
    private String AddTime;
    private String Content;
    private String Image1;
    private String Image2;
    private String Image3;
    private String Image4;

    public String getImage1() {
        return Image1;
    }

    public void setImage1(String image1) {
        Image1 = image1;
    }

    public String getImage2() {
        return Image2;
    }

    public void setImage2(String image2) {
        Image2 = image2;
    }

    public String getImage3() {
        return Image3;
    }

    public void setImage3(String image3) {
        Image3 = image3;
    }

    public String getImage4() {
        return Image4;
    }

    public void setImage4(String image4) {
        Image4 = image4;
    }

    private List<CommentBean> CommentList;

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

    public String getContent() {
        return Content;
    }

    public void setContent(String Content) {
        this.Content = Content;
    }

    public List<CommentBean> getCommentList() {
        return CommentList;
    }

    public void setCommentList(List<CommentBean> CommentList) {
        this.CommentList = CommentList;
    }


}
