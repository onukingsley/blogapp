package com.example.nox;

import java.io.Serializable;

public class Comment_Model implements Serializable {
    String username, fullname, user_image, post_id, date_commented,user_id,comment,comment_id;
    int noofcomment;

    public String getUsername() {
        return username;
    }

    public String getFullname() {
        return fullname;
    }

    public String getUser_image() {
        return user_image;
    }

    public String getPost_id() {
        return post_id;
    }

    public String getDate_commented() {
        return date_commented;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getComment() {
        return comment;
    }
    public int getnoofcomment() {
        return noofcomment;
    }




    public String getComment_id() {
        return comment_id;
    }

    public Comment_Model(String username, String fullname, String user_image, String post_id, String date_commented, String user_id, String comment) {
        this.username = username;
        this.fullname = fullname;
        this.user_image = user_image;
        this.post_id = post_id;
        this.date_commented = date_commented;
        this.user_id = user_id;
        this.comment = comment;
    }

    public Comment_Model(String username, String fullname, String user_image, String post_id, String date_commented, String user_id, String comment, String comment_id) {
        this.username = username;
        this.fullname = fullname;
        this.user_image = user_image;
        this.post_id = post_id;
        this.date_commented = date_commented;
        this.user_id = user_id;
        this.comment = comment;
        this.comment_id = comment_id;
    }

    public Comment_Model() {
    }

    public Comment_Model(String username, String fullname, String user_image, String post_id, String date_commented, String user_id, String comment, int noofcomment) {
        this.username = username;
        this.fullname = fullname;
        this.user_image = user_image;
        this.post_id = post_id;
        this.date_commented = date_commented;
        this.user_id = user_id;
        this.comment = comment;
        this.noofcomment = noofcomment;
    }
}
