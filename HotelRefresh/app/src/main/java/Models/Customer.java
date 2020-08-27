package Models;

public class Customer {

    int id;
    String name;
    String uname;
    String pword;
    int status;

    public Customer(int id, String name, String uname, String pword, int status) {
        this.id = id;
        this.name = name;
        this.uname = uname;
        this.pword = pword;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPword() {
        return pword;
    }

    public void setPword(String pword) {
        this.pword = pword;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
