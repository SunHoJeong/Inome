package com.example.jeong.hanjo.data;

import java.io.Serializable;

/**
 * Created by Jeong on 2016-11-14.
 */
public class ResponseUserInfo implements Serializable{
    private String id;
    private String pw;
    private String name;
    private String phone;
    private String authority;

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getId(){
        return id;
    }
}
