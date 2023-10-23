package com.example.myapplication;

public class ReadWriteUserDetails {
    public String fullname,mobile,devicekey;

    public ReadWriteUserDetails(){};
    public ReadWriteUserDetails(String textFullName,String textmobile,String textdevkey){
        this.fullname=textFullName;
        this.mobile=textmobile;
        this.devicekey=textdevkey;
    }
}
