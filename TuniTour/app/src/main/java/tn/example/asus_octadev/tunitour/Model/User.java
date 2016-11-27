package tn.example.asus_octadev.tunitour.Model;

import java.util.HashMap;

/**
 * Created by ASUS-OCTADEV on 2016-08-19.
 */

public class User {
    String email,full_name,mobile,profile_image;
    String password;
    String android_not_token;
    String fcm ;


    public User() {
    }

    public User(String email, String login, String tel, String photo,String password,String fcm) {
        this.email = email;
        this.full_name = login;
        this.mobile = tel;
        this.profile_image = photo;
        this.password = password;
        this.fcm=fcm;

    }

    public String getFcm() {
        return fcm;
    }

    public void setFcm(String fcm) {
        this.fcm = fcm;
    }

    public String getAndroid_not_token() {
        return android_not_token;
    }

    public void setAndroid_not_token(String android_not_token) {
        this.android_not_token = android_not_token;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
