package Models;

public class Package {

    int id;
    int hours;
    double price;
    double offer;
    int pakid;

    public Package(int id, int hours, double price, double offer, int pakid) {
        this.id = id;
        this.hours = hours;
        this.price = price;
        this.offer = offer;
        this.pakid = pakid;
    }

    public int getPakid() {
        return pakid;
    }

    public void setPakid(int pakid) {
        this.pakid = pakid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getOffer() {
        return offer;
    }

    public void setOffer(double offer) {
        this.offer = offer;
    }
}
