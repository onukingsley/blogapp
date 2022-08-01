package com.example.nox;

import java.io.Serializable;

public class Post_Model implements Serializable {
    public String dateposted,content,image,title,user_id,blog_id,user_image,username,fullname,comment;
    public boolean like,dislike;
    int no_of_like,no_of_dislike,no_of_comment;

    public Post_Model(String dateposted, String content, String image, String title, String user_id, String blog_id) {
        this.dateposted = dateposted;
        this.content = content;
        this.image = image;
        this.title = title;
        this.user_id = user_id;
        this.blog_id = blog_id;

    }

    public Post_Model(String dateposted, String content, String image, String title, String user_id, String blog_id, String user_image, String username, String fullname, boolean like, int no_of_like, int no_of_dislike, int no_of_comment) {
        this.dateposted = dateposted;
        this.content = content;
        this.image = image;
        this.title = title;
        this.user_id = user_id;
        this.blog_id = blog_id;
        this.user_image = user_image;
        this.username = username;
        this.fullname = fullname;
        this.comment = comment;
        this.like = like;
        this.dislike = dislike;
        this.no_of_like = no_of_like;
        this.no_of_dislike = no_of_dislike;

    }

    public int getNo_of_like() {
        return no_of_like;
    }

    public int getNo_of_dislike() {
        return no_of_dislike;
    }

    public int getNo_of_comment() {
        return no_of_comment;
    }

    public String getUser_image() {
        return user_image;
    }

    public String getUsername() {
        return username;
    }

    public String getFullname() {
        return fullname;
    }

    public boolean getLike() {
        return like;
    }

    public boolean getDislike() {
        return dislike;
    }

    public String getComment() {
        return comment;
    }

    public Post_Model(String dateposted, String content, String image, String title, String user_id, String blog_id, String user_image, String username, String fullname,boolean like) {
        this.dateposted = dateposted;
        this.content = content;
        this.image = image;
        this.title = title;
        this.user_id = user_id;
        this.blog_id = blog_id;
        this.user_image = user_image;
        this.username = username;
        this.fullname = fullname;
        this.like = like;

    }

    public Post_Model() {
    }

    public String getDateposted() {
        return dateposted;
    }

    public String getContent() {
        return content;
    }

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getBlog_id() {
        return blog_id;
    }
}
