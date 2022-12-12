package com.jadhav.aakash.ui.comment;

public class CommentModel {

    private String commentId, cUserId, comment, cDate;

    public CommentModel(String commentId, String cUserId, String comment, String cDate) {
        this.commentId = commentId;
        this.cUserId = cUserId;
        this.comment = comment;
        this.cDate = cDate;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getcUserId() {
        return cUserId;
    }

    public void setcUserId(String cUserId) {
        this.cUserId = cUserId;
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
