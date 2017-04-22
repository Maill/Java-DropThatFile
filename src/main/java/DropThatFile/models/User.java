package DropThatFile.models;

import DropThatFile.engines.RSAEngine;

import java.util.*;

/**
 * Created by Nicol on 21/03/2017.
 */
public class User {

    //region Attributs
    private int id;
    private String fName;
    private String lName;
    private RSAEngine password;
    private String mail;
    private Date lastLogin;
    private String phoneNumber;
    private List<Group> isMemberOf = new ArrayList<Group>();
    //endregion

    //region Contructeurs
    public User(int id, String fName, String lName, RSAEngine password, String mail, Date lastLogin, String phoneNumber,List<Group> isMemberOf) {
        this.id = id;
        this.fName = fName;
        this.lName = lName;
        this.password = password;
        this.mail = mail;
        this.lastLogin = lastLogin;
        this.phoneNumber = phoneNumber;
        this.isMemberOf = isMemberOf;
    }

    public User(int id, String fName, String lName, RSAEngine password, String mail, Date lastLogin, String phoneNumber,Group isMemberOf) {
        this.id = id;
        this.fName = fName;
        this.lName = lName;
        this.password = password;
        this.mail = mail;
        this.lastLogin = lastLogin;
        this.phoneNumber = phoneNumber;
        this.isMemberOf.add(isMemberOf);
    }
    //endregion

    //region Getters
    public int getId() {
        return id;
    }

    public String getfName() {
        return fName;
    }

    public String getlName() {
        return lName;
    }

    public RSAEngine getPassword() {
        return password;
    }

    public String getMail() {
        return mail;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public List<Group> getIsMemberOf() {
        return isMemberOf;
    }
    //endregion

    //region Setters
    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    //endregion


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != user.id) return false;
        if (!fName.equals(user.fName)) return false;
        if (!lName.equals(user.lName)) return false;
        if (!password.equals(user.password)) return false;
        if (!mail.equals(user.mail)) return false;
        if (lastLogin != null ? !lastLogin.equals(user.lastLogin) : user.lastLogin != null) return false;
        if (!phoneNumber.equals(user.phoneNumber)) return false;
        return isMemberOf.equals(user.isMemberOf);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + fName.hashCode();
        result = 31 * result + lName.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + mail.hashCode();
        result = 31 * result + (lastLogin != null ? lastLogin.hashCode() : 0);
        result = 31 * result + phoneNumber.hashCode();
        result = 31 * result + isMemberOf.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", fName='" + fName + '\'' +
                ", lName='" + lName + '\'' +
                ", password=" + password +
                ", mail='" + mail + '\'' +
                ", lastLogin=" + lastLogin +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", isMemberOf=" + isMemberOf +
                '}';
    }
    //endregion
}
