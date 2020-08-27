package com.app.wooker.DBClasses;

import com.google.firebase.firestore.Exclude;

import java.util.Date;

public class Notification {
    String notid;
    String to;
    String title;
    String message;
    String type;
    String data;
    String status;
    Date date;

    public Notification() {
    }

    public Notification(String to,String title, String message, String type, String data, String status, Date date) {
        this.to = to;
        this.title=title;
        this.message = message;
        this.type = type;
        this.data = data;
        this.status = status;
        this.date = date;
    }

    public String getNotid() {
        return notid;
    }

    @Exclude
    public void setNotid(String notid) {
        this.notid = notid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
