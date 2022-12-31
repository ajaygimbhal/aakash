package com.jadhav.aakash.ui.notifications;


public class NotificationsModel {

    private String notifyId;
    private String notifyType;
    private String notifyMessage;
    private String notifyCommentId;
    private String notifyToUserId;
    private String notifyToUsername;
    private String notifyToProfileImg;
    private String notifyFromUserId;
    private String notifyPostId;
    private String notifyPostImg;
    private String notifyDate;
    private boolean notifyRead;


    public NotificationsModel(String notifyId, String notifyType, String notifyMessage, String notifyCommentId, String notifyToUserId, String notifyToUsername, String notifyToProfileImg, String notifyFromUserId, String notifyPostId, String notifyPostImg, String notifyDate, boolean notifyRead) {
        this.notifyId = notifyId;
        this.notifyType = notifyType;
        this.notifyMessage = notifyMessage;
        this.notifyCommentId = notifyCommentId;
        this.notifyToUserId = notifyToUserId;
        this.notifyToUsername = notifyToUsername;
        this.notifyToProfileImg = notifyToProfileImg;
        this.notifyFromUserId = notifyFromUserId;
        this.notifyPostId = notifyPostId;
        this.notifyPostImg = notifyPostImg;
        this.notifyDate = notifyDate;
        this.notifyRead = notifyRead;
    }

    public String getNotifyId() {
        return notifyId;
    }

    public void setNotifyId(String notifyId) {
        this.notifyId = notifyId;
    }

    public String getNotifyType() {
        return notifyType;
    }

    public void setNotifyType(String notifyType) {
        this.notifyType = notifyType;
    }

    public String getNotifyMessage() {
        return notifyMessage;
    }

    public void setNotifyMessage(String notifyMessage) {
        this.notifyMessage = notifyMessage;
    }

    public String getNotifyCommentId() {
        return notifyCommentId;
    }

    public void setNotifyCommentId(String notifyCommentId) {
        this.notifyCommentId = notifyCommentId;
    }

    public String getNotifyToUserId() {
        return notifyToUserId;
    }

    public void setNotifyToUserId(String notifyToUserId) {
        this.notifyToUserId = notifyToUserId;
    }

    public String getNotifyToUsername() {
        return notifyToUsername;
    }

    public void setNotifyToUsername(String notifyToUsername) {
        this.notifyToUsername = notifyToUsername;
    }

    public String getNotifyToProfileImg() {
        return notifyToProfileImg;
    }

    public void setNotifyToProfileImg(String notifyToProfileImg) {
        this.notifyToProfileImg = notifyToProfileImg;
    }

    public String getNotifyFromUserId() {
        return notifyFromUserId;
    }

    public void setNotifyFromUserId(String notifyFromUserId) {
        this.notifyFromUserId = notifyFromUserId;
    }

    public String getNotifyPostId() {
        return notifyPostId;
    }

    public void setNotifyPostId(String notifyPostId) {
        this.notifyPostId = notifyPostId;
    }

    public String getNotifyPostImg() {
        return notifyPostImg;
    }

    public void setNotifyPostImg(String notifyPostImg) {
        this.notifyPostImg = notifyPostImg;
    }

    public String getNotifyDate() {
        return notifyDate;
    }

    public void setNotifyDate(String notifyDate) {
        this.notifyDate = notifyDate;
    }

    public boolean isNotifyRead() {
        return notifyRead;
    }

    public void setNotifyRead(boolean notifyRead) {
        this.notifyRead = notifyRead;
    }
}
