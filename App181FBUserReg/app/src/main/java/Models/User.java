package Models;

public class User {

    private String name;
    private String mobileno;
    private String email;
    private String pword;
    private String gender;
    private String img;

    public User(String name, String mobileno, String email, String pword, String gender) {
        this.name = name;
        this.mobileno = mobileno;
        this.email = email;
        this.pword = pword;
        this.gender = gender;
    }

    public User(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPword() {
        return pword;
    }

    public void setPword(String pword) {
        this.pword = pword;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
