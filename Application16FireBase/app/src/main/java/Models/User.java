package Models;

public class User {

    private int id;
    private String name;
    private int mobile;
    private String pword;

    public User(){

    }

    public User(int id, String name, int mobile, String pword) {
        this.setId(id);
        this.setName(name);
        this.setMobile(mobile);
        this.setPword(pword);
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

    public int getMobile() {
        return mobile;
    }

    public void setMobile(int mobile) {
        this.mobile = mobile;
    }

    public String getPword() {
        return pword;
    }

    public void setPword(String pword) {
        this.pword = pword;
    }
}


