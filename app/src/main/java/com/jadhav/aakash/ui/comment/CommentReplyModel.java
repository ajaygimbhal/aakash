package com.jadhav.aakash.ui.comment;

public class CommentReplyModel {

    private String cRComment_id, cRUser_id, cRUsername, cRUserImg, cRComment, cRCDate;

    public CommentReplyModel(String cRComment_id, String cRUser_id, String cRUsername, String cRUserImg, String cRComment, String cRCDate) {
        this.cRComment_id = cRComment_id;
        this.cRUser_id = cRUser_id;
        this.cRUsername = cRUsername;
        this.cRUserImg = cRUserImg;
        this.cRComment = cRComment;
        this.cRCDate = cRCDate;
    }

    public String getcRComment_id() {
        return cRComment_id;
    }

    public void setcRComment_id(String cRComment_id) {
        this.cRComment_id = cRComment_id;
    }

    public String getcRUser_id() {
        return cRUser_id;
    }

    public void setcRUser_id(String cRUser_id) {
        this.cRUser_id = cRUser_id;
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
