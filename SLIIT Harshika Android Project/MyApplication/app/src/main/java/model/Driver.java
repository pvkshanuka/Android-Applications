package model;

public class Driver {
    private int driid;
    private String fname;
    private String lname;
    private String email;
    private String password;
    private String nic;
    private int age;
    private int status;

    public Driver() {
    }

    public Driver(int driid, String fname, String lname, String email, String password, String nic, int age, int status) {
        this.setDriid(driid);
        this.setFname(fname);
        this.setLname(lname);
        this.setEmail(email);
        this.setPassword(password);
        this.setNic(nic);
        this.setAge(age);
        this.setStatus(status);
    }

    public int getDriid() {
        return driid;
    }

    public void setDriid(int driid) {
        this.driid = driid;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
