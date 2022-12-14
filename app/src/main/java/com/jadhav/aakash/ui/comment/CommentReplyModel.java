package com.jadhav.aakash.ui.comment;

public class CommentReplyModel {

    private String cRCommentId, cRUserId, cRUsername, cRUserImg, cRComment, cRCDate;

    public CommentReplyModel(String cRCommentId, String cRUserId, String cRComment, String cRCDate) {
        this.cRCommentId = cRCommentId;
        this.cRUserId = cRUserId;
        this.cRComment = cRComment;
        this.cRCDate = cRCDate;
    }

    public String getcRCommentId() {
        return cRCommentId;
    }

    public void setcRCommentId(String cRCommentId) {
        this.cRCommentId = cRCommentId;
    }

    public String getcRUserId() {
        return cRUserId;
    }

    public void setcRUserId(String cRUserId) {
        this.cRUserId = cRUserId;
    }

    public String getcRUsername() {
        return cRUsername;
    }

    public void setcRUsername(String cRUsername) {
        this.cRUsername = cRUsername;
    }

    public String getcRUserImg() {
        return cRUserImg;
    }

    public void setcRUserImg(String cRUserImg) {
        this.cRUserImg = cRUserImg;
    }

    public String getcRComment() {
        return cRComment;
    }

    public void setcRComment(String cRComment) {
        this.cRComment = cRComment;
    }

    public String getcRCDate() {
        return cRCDate;
    }

    public void setcRCDate(String cRCDate) {
        this.cRCDate = cRCDate;
    }
}
