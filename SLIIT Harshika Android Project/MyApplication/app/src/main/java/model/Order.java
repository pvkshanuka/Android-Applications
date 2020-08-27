package model;

import java.util.Date;

public class Order {
    private int orderid;
    private int driverid;
    private String paddress;
    private String daddress;
    private Date pdate;
    private Date deliverytime;
    private int status;

    public Order() {
    }

    public Order(int orderid, int driverid, String paddress, String daddress, Date pdate, Date deliverytime, int status) {
        this.setOrderid(orderid);
        this.setDriverid(driverid);
        this.setPaddress(paddress);
        this.setDaddress(daddress);
        this.setPdate(pdate);
        this.setDeliverytime(deliverytime);
        this.setStatus(status);
    }


    public int getOrderid() {
        return orderid;
    }

    public void setOrderid(int orderid) {
        this.orderid = orderid;
    }

    public int getDriverid() {
        return driverid;
    }

    public void setDriverid(int driverid) {
        this.driverid = driverid;
    }

    public String getPaddress() {
        return paddress;
    }

    public void setPaddress(String paddress) {
        this.paddress = paddress;
    }

    public String getDaddress() {
        return daddress;
    }

    public void setDaddress(String daddress) {
        this.daddress = daddress;
    }

    public Date getPdate() {
        return pdate;
    }

    public void setPdate(Date pdate) {
        this.pdate = pdate;
    }

    public Date getDeliverytime() {
        return deliverytime;
    }

    public void setDeliverytime(Date deliverytime) {
        this.deliverytime = deliverytime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
