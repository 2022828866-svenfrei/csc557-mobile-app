package my.edu.uitm.friendmanagement.models;

import java.util.Date;

public class Friend {
    private long id;
    private long loginFK;
    private String name;
    private Gender gender;
    private Date birthdate;
    private String phoneNo;
    private String email;
    private String photo;

    public Friend(long id, long loginFK, String name, Gender gender, Date birthdate, String phoneNo, String email, String photo) {
        this.id = id;
        this.loginFK = loginFK;
        this.name = name;
        this.gender = gender;
        this.birthdate = birthdate;
        this.phoneNo = phoneNo;
        this.email = email;
        this.photo = photo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getLoginFK() {
        return loginFK;
    }

    public void setLoginFK(long loginFK) {
        this.loginFK = loginFK;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
