package com.zzx.blog.bean;

public class LoanSuperBean {

    /**
     * ID : 1
     * Name : 钱哥哥
     * Image : 7d96d746-854d-42a9-8561-ecbeb1d44780.jpg
     * Limit : 500-5000
     * Interest : 0.3%
     * Url : https://www.baidu.com/
     * Tips : 凭身份证轻松贷
     * IsShow : 1
     * Sort : 0
     */

    private int ID;
    private String Name;
    private String Image;
    private String Limit;
    private String Interest;
    private String Url;
    private String Tips;
    private int IsShow;
    private int Sort;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String Image) {
        this.Image = Image;
    }

    public String getLimit() {
        return Limit;
    }

    public void setLimit(String Limit) {
        this.Limit = Limit;
    }

    public String getInterest() {
        return Interest;
    }

    public void setInterest(String Interest) {
        this.Interest = Interest;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String Url) {
        this.Url = Url;
    }

    public String getTips() {
        return Tips;
    }

    public void setTips(String Tips) {
        this.Tips = Tips;
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
