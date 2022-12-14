package com.jadhav.aakash.supports;

import androidx.annotation.Keep;

@Keep
public class Notification {

    private String nType;
    private String nToUserId;
    private String nFromUserId;
    private String nPostId;
    private String nMessage;
    private String nDate;
    private boolean nRead = false;

    public Notification() {
    }

    public Notification(String nType, String nToUserId, String nFromUserId, String nPostId, String nMessage, String nDate) {
        this.nType = nType;
        this.nToUserId = nToUserId;
        this.nFromUserId = nFromUserId;
        this.nPostId = nPostId;
        this.nMessage = nMessage;
        this.nDate = nDate;
    }

    public String getnType() {
        return nType;
    }

    public void setnType(String nType) {
        this.nType = nType;
    }

    public String getnToUserId() {
        return nToUserId;
    }

    public void setnToUserId(String nToUserId) {
        this.nToUserId = nToUserId;
    }

    public String getnFromUserId() {
        return nFromUserId;
    }

    public void setnFromUserId(String nFromUserId) {
        this.nFromUserId = nFromUserId;
    }

    public String getnPostId() {
        return nPostId;
    }

    public void setnPostId(String nPostId) {
        this.nPostId = nPostId;
    }

    public String getnMessage() {
        return nMessage;
    }

    public void setnMessage(String nMessage) {
        this.nMessage = nMessage;
    }

    public String getnDate() {
        return nDate;
    }

    public void setnDate(String nDate) {
        this.nDate = nDate;
    }

    public boolean isnRead() {
        return nRead;
    }

    public void setnRead(boolean nRead) {
        this.nRead = nRead;
    }
}
