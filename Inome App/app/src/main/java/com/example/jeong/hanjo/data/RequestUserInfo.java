package com.example.jeong.hanjo.data;

import java.io.Serializable;

/**
 * Created by Jeong on 2016-11-14.
 */
public class RequestUserInfo implements Serializable{

    private String familyId;
    private String familyPw;
    private String oldUserName;
    private String newUserId;
    private String newUserPw;
    private String newUserName;
    private String newUserPhone;
    private String newUserAuthority;

    public RequestUserInfo(String familyId, String familyPw, String oldName, String userId, String userPw, String userName, String userPhone, String authority){
        this.familyId = familyId;
        this.familyPw = familyPw;
        this.oldUserName = oldName;
        this.newUserId = userId;
        this.newUserPw = userPw;
        this.newUserName = userName;
        this.newUserPhone = userPhone;
        this.newUserAuthority = authority;
    }

    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

    public String getFamilyPw() {
        return familyPw;
    }

    public void setFamilyPw(String familyPw) {
        this.familyPw = familyPw;
    }

    public String getNewUserAuthority() {
        return newUserAuthority;
    }

    public void setNewUserAuthority(String newUserAuthority) {
        this.newUserAuthority = newUserAuthority;
    }

    public String getNewUserId() {
        return newUserId;
    }

    public void setNewUserId(String newUserId) {
        this.newUserId = newUserId;
    }

    public String getNewUserName() {
        return newUserName;
    }

    public void setNewUserName(String newUserName) {
        this.newUserName = newUserName;
    }

    public String getNewUserPhone() {
        return newUserPhone;
    }

    public void setNewUserPhone(String newUserPhone) {
        this.newUserPhone = newUserPhone;
    }

    public String getNewUserPw() {
        return newUserPw;
    }

    public void setNewUserPw(String newUserPw) {
        this.newUserPw = newUserPw;
    }

    public String getOldUserName() {
        return oldUserName;
    }

    public void setOldUserName(String oldUserName) {
        this.oldUserName = oldUserName;
    }
}
