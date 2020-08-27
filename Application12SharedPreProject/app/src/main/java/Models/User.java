package Models;

public class User {

    private int id;
    private String name;
    private String password;
    private String mobile;

    public User(int id, String name, String password, String mobile) {
        this.setId(id);
        this.setName(name);
        this.setPassword(password);
        this.setMobile(mobile);
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
