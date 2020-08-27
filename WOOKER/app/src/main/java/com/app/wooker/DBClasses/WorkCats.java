package com.app.wooker.DBClasses;

public class WorkCats {


    String type;
    String status;

    public WorkCats() {
    }

    public WorkCats(String type, String status) {
        this.type = type;
        this.status = status;
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
