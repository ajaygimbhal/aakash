package com.jadhav.aakash.supports;

import androidx.annotation.Keep;

@Keep
public class Replies {
    String parentId;
    String toUserId;
    String fromUserId;
    String message;
    String date;

    public Replies() {
    }

    public Replies(String parentId, String toUserId, String fromUserId, String message, String date) {
        this.parentId = parentId;
        this.toUserId = toUserId;
        this.fromUserId = fromUserId;
        this.message = message;
        this.date = date;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
