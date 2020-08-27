package com.app.wooker.DBClasses;

import com.google.firebase.firestore.Exclude;

import java.util.Date;
import java.util.Map;

public class Job {

    String jid;
    String wid;
    String cid;
    String jtype;
    Date added_date;
    Date job_date;
    Date confirmed_date;
    Date start_time;
    Date end_time;
    double rating;
    String cgender;
    Map<String, Double> job_location;
    String status;
    Double payment;

    public Job() {
    }

    public Job(String wid, String cid, String jtype, Date added_date, Date job_date, String cgender, Map<String, Double> job_location, String status) {
        this.wid = wid;
        this.cid = cid;
        this.jtype = jtype;
        this.added_date = added_date;
        this.job_date = job_date;
        this.cgender = cgender;
        this.job_location = job_location;
        this.status = status;
    }

    public Double getPayment() {
        return payment;
    }

    public void setPayment(Double payment) {
        this.payment = payment;
    }

    public String getJid() {
        return jid;
    }

    @Exclude
    public void setJid(String jid) {
        this.jid = jid;
    }

    public String getWid() {
        return wid;
    }

    public void setWid(String wid) {
        this.wid = wid;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getJtype() {
        return jtype;
    }

    public void setJtype(String jtype) {
        this.jtype = jtype;
    }

    public Date getAdded_date() {
        return added_date;
    }

    public void setAdded_date(Date added_date) {
        this.added_date = added_date;
    }

    public Date getJob_date() {
        return job_date;
    }

    public void setJob_date(Date job_date) {
        this.job_date = job_date;
    }

    public Date getConfirmed_date() {
        return confirmed_date;
    }

    public void setConfirmed_date(Date confirmed_date) {
        this.confirmed_date = confirmed_date;
    }

    public Date getStart_time() {
        return start_time;
    }

    public void setStart_time(Date start_time) {
        this.start_time = start_time;
    }

    public Date getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Date end_time) {
        this.end_time = end_time;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getCgender() {
        return cgender;
    }

    public void setCgender(String cgender) {
        this.cgender = cgender;
    }

    public Map<String, Double> getJob_location() {
        return job_location;
    }

    public void setJob_location(Map<String, Double> job_location) {
        this.job_location = job_location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
