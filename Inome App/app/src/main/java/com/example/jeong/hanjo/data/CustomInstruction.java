package com.example.jeong.hanjo.data;

import java.io.Serializable;

/**
 * Created by Jeong on 2016-12-07.
 */
public class CustomInstruction implements Serializable{
    String familyId;
    String familyPw;
    String userInstruction;
    String IRInstruction;

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

    public String getIRInstruction() {
        return IRInstruction;
    }

    public void setIRInstruction(String IRInstruction) {
        this.IRInstruction = IRInstruction;
    }

    public String getUserInstruction() {
        return userInstruction;
    }

    public void setUserInstruction(String userInstruction) {
        this.userInstruction = userInstruction;
    }
}
