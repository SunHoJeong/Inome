package com.example.jeong.hanjo.data;

/**
 * Created by Jeong on 2016-11-21.
 */
public class RequestIRDevice {
    String familyId;
    String familyPw;
    String oldDeviceName;
    String newDeviceCompany;
    String newDeviceClassification;
    String newDeviceModel;
    String newDeviceName;
    String newDeviceAddress;

    public RequestIRDevice(String familyId, String familyPw, String oldDeviceName, String newDeviceCompany, String newDeviceClassification,
            String newDeviceModel, String newDeviceName, String newDeviceAddress) {
        this.familyId = familyId;
        this.familyPw = familyPw;
        this.newDeviceAddress = newDeviceAddress;
        this.newDeviceClassification = newDeviceClassification;
        this.newDeviceCompany = newDeviceCompany;
        this.newDeviceModel = newDeviceModel;
        this.newDeviceName = newDeviceName;
        this.oldDeviceName = oldDeviceName;
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

    public String getNewDeviceAddress() {
        return newDeviceAddress;
    }

    public void setNewDeviceAddress(String newDeviceAddress) {
        this.newDeviceAddress = newDeviceAddress;
    }

    public String getNewDeviceClassification() {
        return newDeviceClassification;
    }

    public void setNewDeviceClassification(String newDeviceClassification) {
        this.newDeviceClassification = newDeviceClassification;
    }

    public String getNewDeviceCompany() {
        return newDeviceCompany;
    }

    public void setNewDeviceCompany(String newDeviceCompany) {
        this.newDeviceCompany = newDeviceCompany;
    }

    public String getNewDeviceModel() {
        return newDeviceModel;
    }

    public void setNewDeviceModel(String newDeviceModel) {
        this.newDeviceModel = newDeviceModel;
    }

    public String getNewDeviceName() {
        return newDeviceName;
    }

    public void setNewDeviceName(String newDeviceName) {
        this.newDeviceName = newDeviceName;
    }

    public String getOldDeviceName() {
        return oldDeviceName;
    }

    public void setOldDeviceName(String oldDeviceName) {
        this.oldDeviceName = oldDeviceName;
    }
}
