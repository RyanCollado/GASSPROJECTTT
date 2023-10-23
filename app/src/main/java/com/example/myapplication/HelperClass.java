package com.example.myapplication;

public class HelperClass{
    String usrname, password,devicekey;

    public String getUsrname() {
        return usrname;
    }

    public void setUsrname(String usrname) {
        this.usrname = usrname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDevicekey() {
        return devicekey;
    }

    public void setDevicekey(String devicekey) {
        this.devicekey = devicekey;
    }

    public HelperClass(String usrname, String password, String devicekey) {
        this.usrname = usrname;
        this.password = password;
        this.devicekey = devicekey;
    }

    public HelperClass() {
    }
}
