package com.example.aakash.ui.comment;

public class CommentModel {

    private String commentId, countComment, cUserId, username, imageUrl, comment, cDate;

    public CommentModel(String commentId, String countComment, String cUserId, String username, String imageUrl, String comment, String cDate) {
        this.commentId = commentId;
        this.countComment = countComment;
        this.cUserId = cUserId;
        this.username = username;
        this.imageUrl = imageUrl;
        this.comment = comment;
        this.cDate = cDate;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getCountComment() {
        return countComment;
    }

    public void setCountComment(String countComment) {
        this.countComment = countComment;
    }

    public String getcUserId() {
        return cUserId;
    }

    public void setcUserId(String cUserId) {
        this.cUserId = cUserId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getcDate() {
        return cDate;
    }

    public void setcDate(String cDate) {
        this.cDate = cDate;
    }
}
