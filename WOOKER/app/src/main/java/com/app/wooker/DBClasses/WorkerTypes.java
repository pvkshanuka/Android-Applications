package com.app.wooker.DBClasses;

public class WorkerTypes {

    String type;
    String workerid;

    public WorkerTypes() {
    }

    public WorkerTypes(String type, String workerid) {
        this.type = type;
        this.workerid = workerid;
    }

    public String getType() {
        return type;
    }

    public void setType(String typeid) {
        this.type = typeid;
    }

    public String getWorkerid() {
        return workerid;
    }

    public void setWorkerid(String workerid) {
        this.workerid = workerid;
    }
}
