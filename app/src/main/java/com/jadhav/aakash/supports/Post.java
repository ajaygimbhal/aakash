package com.jadhav.aakash.supports;

import androidx.annotation.Keep;

@Keep
public class Post {
    private String postTitle;
    private String postUserId;
    private String postImageUrl;
    private String postDate;

    public Post() {
    }

    public Post(String postTitle, String postUserId, String postImageUrl, String postDate) {
        this.postTitle = postTitle;
        this.postUserId = postUserId;
        this.postImageUrl = postImageUrl;
        this.postDate = postDate;
    }


    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostUserId() {
        return postUserId;
    }

    public void setPostUserId(String postUserId) {
        this.postUserId = postUserId;
    }

    public String getPostImageUrl() {
        return postImageUrl;
    }

    public void setPostImageUrl(String postImageUrl) {
        this.postImageUrl = postImageUrl;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }
}
