package com.jadhav.aakash.ui.profile;

import androidx.annotation.Keep;

@Keep
public class ProfilePostModel {
    private String postId;
    private String postTitle;
    private String postImage;

    public ProfilePostModel(String postId, String postTitle, String postImage) {
        this.postId = postId;
        this.postTitle = postTitle;
        this.postImage = postImage;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }
}
