package vn.phamthang.navigationbar_custom.Model;

import java.io.Serializable;

public class User implements Serializable {
    private String name;
    private String email;
    private String passWord;
    private String phoneNumber;

    public User(String name, String email, String passWord, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.passWord = passWord;
        this.phoneNumber = phoneNumber;
    }
    public User(String passWord) {

        this.passWord = passWord;

    }
    public User() {
    }

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

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
