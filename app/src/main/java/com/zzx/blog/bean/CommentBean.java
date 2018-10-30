package com.zzx.blog.bean;

public class CommentBean {
    /**
     * CommentID : 1
     * CommentDate : 2018-10-20 11:33:58
     * CommentName : null
     * CommentContent : 评论1
     */

    private int CommentID;
    private String CommentDate;
    private String CommentName;
    private String CommentContent;

    public int getCommentID() {
        return CommentID;
    }

    public void setCommentID(int CommentID) {
        this.CommentID = CommentID;
    }

    public String getCommentDate() {
        return CommentDate;
    }

    public void setCommentDate(String CommentDate) {
        this.CommentDate = CommentDate;
    }

    public String getCommentName() {
        return CommentName;
    }

    public void setCommentName(String CommentName) {
        this.CommentName = CommentName;
    }

    public String getCommentContent() {
        return CommentContent;
    }

    public void setCommentContent(String CommentContent) {
        this.CommentContent = CommentContent;
    }
}
