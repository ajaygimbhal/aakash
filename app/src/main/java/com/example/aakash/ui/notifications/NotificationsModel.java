package com.example.aakash.ui.notifications;

import androidx.annotation.Keep;

@Keep
public class NotificationsModel {

    private String notifyId;
    private String notifyType;
    private String notifyUserIcon;
    private String notifyUsername;
    private String notifyMessage;
    private String notifyPostId;
    private String notifyPostImg;
    private String notifyDate;
    private boolean notifyRead;


    public NotificationsModel(String notifyId, String notifyType, String notifyUserIcon, String notifyUsername, String notifyMessage, String notifyPostId, String notifyPostImg, String notifyDate, boolean notifyRead) {
        this.notifyId = notifyId;
        this.notifyType = notifyType;
        this.notifyUserIcon = notifyUserIcon;
        this.notifyUsername = notifyUsername;
        this.notifyMessage = notifyMessage;
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

    public String getNotifyUserIcon() {
        return notifyUserIcon;
    }

    public void setNotifyUserIcon(String notifyUserIcon) {
        this.notifyUserIcon = notifyUserIcon;
    }

    public String getNotifyUsername() {
        return notifyUsername;
    }

    public void setNotifyUsername(String notifyUsername) {
        this.notifyUsername = notifyUsername;
    }

    public String getNotifyMessage() {
        return notifyMessage;
    }

    public void setNotifyMessage(String notifyMessage) {
        this.notifyMessage = notifyMessage;
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
