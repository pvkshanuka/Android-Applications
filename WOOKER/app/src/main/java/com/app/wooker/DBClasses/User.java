package com.app.wooker.DBClasses;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;

import java.util.Map;

public class User {
    String uid;
    String fname;
    String lname;
    String cno;
    String gender;
    int isEng;
    Map<String,Double> location;
    String city;
    String type;
    String status;
    String imageUrl;
    String imageName;
    boolean isOnline;
    Map<String,Double> live_location;
    String live_loc_city;
    String weburl;

    public User() {
    }

    public User(String uid,String fname, String lname, String cno,String gender, int isEng, Map<String, Double> location, String city, String type, String status, String imageUrl, String imageName, boolean isOnline, Map<String, Double> live_location) {
        this.uid = uid;
        this.fname = fname;
        this.lname = lname;
        this.cno = cno;
        this.gender = gender;
        this.isEng = isEng;
        this.location = location;
        this.city = city;
        this.type = type;
        this.status = status;
        this.imageUrl = imageUrl;
        this.imageName = imageName;
        this.isOnline = isOnline;
        this.live_location = live_location;
    }

    public User(String uid,String fname, String lname, String cno,String gender, String type , String status, String imageUrl, String imageName) {
        this.uid = uid;
        this.fname = fname;
        this.lname = lname;
        this.cno = cno;
        this.gender = gender;
        this.type = type;
        this.status = status;
        this.imageUrl = imageUrl;
        this.imageName = imageName;
    }


    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getWeburl() {
        return weburl;
    }

    public void setWeburl(String weburl) {
        this.weburl = weburl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getLive_loc_city() {
        return live_loc_city;
    }

    public void setLive_loc_city(String live_loc_city) {
        this.live_loc_city = live_loc_city;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public Map<String, Double> getLive_location() {
        return live_location;
    }

    public void setLive_location(Map<String, Double> live_location) {
        this.live_location = live_location;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getCno() {
        return cno;
    }

    public void setCno(String cno) {
        this.cno = cno;
    }

    public int getIsEng() {
        return isEng;
    }

    public void setIsEng(int isEng) {
        this.isEng = isEng;
    }

    public Map<String,Double> getLocation() {
        return location;
    }

    public void setLocation(Map<String,Double> location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
