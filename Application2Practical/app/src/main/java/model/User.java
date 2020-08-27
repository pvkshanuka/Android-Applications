package model;

import java.util.ArrayList;

public class User {

    public User(String name, String email, String mobile, String password, String gender) {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.password = password;
        this.gender = gender;
        userlist = new ArrayList<>();
    }

    private String name;
    private String email;
    private String mobile;
    private String password;
    private  String gender;

    public static ArrayList<User> userlist;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

}
