package com.jadhav.aakash.ui.profile;

import androidx.annotation.Keep;

@Keep
public class ProfilePostModel {
    private String postId;
    private String postImage;

    public ProfilePostModel(String postId, String postImage) {
        this.postId = postId;
        this.postImage = postImage;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }
}
