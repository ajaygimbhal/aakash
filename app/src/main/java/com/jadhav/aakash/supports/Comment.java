package com.jadhav.aakash.supports;

import androidx.annotation.Keep;

@Keep
public class Comment {
    private String toUserId;
    private String comment;
    private String commentAt;

    public Comment() {
    }

    public Comment(String toUserId, String comment, String commentAt) {
        this.toUserId = toUserId;
        this.comment = comment;
        this.commentAt = commentAt;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }


    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCommentAt() {
        return commentAt;
    }

    public void setCommentAt(String commentAt) {
        this.commentAt = commentAt;
    }
}
