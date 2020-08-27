package Models;

public class Venue {

    int id;
    String name;
    int occ;
    double area;
    double heignt;
    String image;
    int status;

    public Venue(int id, String name, int occ, double area, double heignt, String image, int status) {
        this.id = id;
        this.name = name;
        this.occ = occ;
        this.area = area;
        this.heignt = heignt;
        this.image = image;
        this.status = status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOcc(int occ) {
        this.occ = occ;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public void setHeignt(double heignt) {
        this.heignt = heignt;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getOcc() {
        return occ;
    }

    public double getArea() {
        return area;
    }

    public double getHeignt() {
        return heignt;
    }

    public String getImage() {
        return image;
    }

    public int getStatus() {
        return status;
    }
}
