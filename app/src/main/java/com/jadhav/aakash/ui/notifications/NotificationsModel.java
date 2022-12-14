package com.jadhav.aakash.ui.notifications;


public class NotificationsModel {

    private String notifyId;
    private String notifyType;
    private String notifyMessage;
    private String notifyToUserId;
    private String notifyFromUserId;
    private String notifyPostId;
    private String notifyDate;
    private boolean notifyRead;


    public NotificationsModel(String notifyId, String notifyType, String notifyMessage, String notifyToUserId, String notifyFromUserId, String notifyPostId, String notifyDate, boolean notifyRead) {
        this.notifyId = notifyId;
        this.notifyType = notifyType;
        this.notifyMessage = notifyMessage;
        this.notifyToUserId = notifyToUserId;
        this.notifyFromUserId = notifyFromUserId;
        this.notifyPostId = notifyPostId;
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

    public String getNotifyToUserId() {
        return notifyToUserId;
    }

    public void setNotifyToUserId(String notifyToUserId) {
        this.notifyToUserId = notifyToUserId;
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
