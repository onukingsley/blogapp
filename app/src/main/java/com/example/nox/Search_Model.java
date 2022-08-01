package com.example.nox;

public class Search_Model {
    String fullname, username,profileimage,userid,datepublished,title,image,content,blog_id;


    public Search_Model() {
    }

    public Search_Model(String fullname, String username, String profileimage, String userid) {
        this.fullname = fullname;
        this.username = username;
        this.profileimage = profileimage;
        this.userid = userid;
    }

    public Search_Model(String fullname, String username, String profileimage, String userid, String datepublished, String title, String image, String content,String blog_id) {
        this.fullname = fullname;
        this.username = username;
        this.profileimage = profileimage;
        this.userid = userid;
        this.datepublished = datepublished;
        this.title = title;
        this.image = image;
        this.content = content;
        this.blog_id = blog_id;
    }

    public String getFullname() {
        return fullname;
    }

    public String getUsername() {
        return username;
    }

    public String getProfileimage() {
        return profileimage;
    }

    public String getUserid() {
        return userid;
    }

    public String getDatepublished() {
        return datepublished;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public String getContent() {
        return content;
    }

    public String getBlog_id() {
        return blog_id;
    }
}
