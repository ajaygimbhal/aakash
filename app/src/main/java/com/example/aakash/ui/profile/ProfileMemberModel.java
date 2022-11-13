package com.example.aakash.ui.profile;

import androidx.annotation.Keep;

@Keep
public class ProfileMemberModel {
    private String memberId;
    private String memberIcon;

    public ProfileMemberModel(String memberId, String memberIcon) {
        this.memberId = memberId;
        this.memberIcon = memberIcon;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberIcon() {
        return memberIcon;
    }

    public void setMemberIcon(String memberIcon) {
        this.memberIcon = memberIcon;
    }
}
