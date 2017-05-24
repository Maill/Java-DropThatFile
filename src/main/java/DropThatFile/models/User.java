package DropThatFile.models;

import DropThatFile.engines.RSAEngine;

import java.security.KeyPair;
import java.sql.Time;
import java.time.Instant;
import java.util.*;

/**
 * Created by Nicol on 21/03/2017.
 */
public class   User {

    //region Attributs
    private int id;
    private String fName;
    private String lName;
    private String password;
    private KeyPair userKeys;
    private String email;
    private Date lastLogin;
    private String phoneNumber;
    private List<Group> isMemberOf = new ArrayList<Group>();
    private UUID token;
    //endregion

    //region Contructeurs
    public User(int id, String email, String password, KeyPair userKeys,String fName, String lName, Date lastLogin,
                String phoneNumber, List<Group> isMemberOf, UUID token) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.userKeys = userKeys;
        this.fName = fName;
        this.lName = lName;
        this.lastLogin = lastLogin;
        this.phoneNumber = phoneNumber;
        this.isMemberOf = isMemberOf;
        this.token = token;
    }

    public User(int id, String email, String password, KeyPair userKeys,String fName, String lName, Date lastLogin,
                String phoneNumber, Group isMemberOf, UUID token) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.userKeys = userKeys;
        this.fName = fName;
        this.lName = lName;
        this.lastLogin = lastLogin;
        this.phoneNumber = phoneNumber;
        this.isMemberOf.add(isMemberOf);
        this.token = token;
    }

    //Constructeur de test sans BDD
    public User(int id, String email, String password, KeyPair userKeys,String fName, String lName, Date lastLogin,
                String phoneNumber, UUID token) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.userKeys = userKeys;
        this.fName = fName;
        this.lName = lName;
        this.lastLogin = lastLogin;
        this.phoneNumber = phoneNumber;
        this.token = token;
    }
    //endregion

    //region A supprimer plus tard
    private static final Map<String, User> users = new HashMap<>();

    /*public static User of(int id, String email, KeyPair password) {
        User user = users.get(id);
        if (user == null) {
            user = new User(id, email, password, null, null,
                    Time.from(Instant.now()), null, UUID.randomUUID());
            users.put(Integer.toString(id), user);
        }
        return user;
    }*/
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

    public String getPassword() {
        return password;
    }

    public KeyPair getUserKeys() { return userKeys; }

    public String getEmail() {
        return email;
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

    public UUID getToken() { return token; }
    //endregion

    //region Setters
    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPassword(String password) {
         this.password = password;
    }
    //endregion

    //region Overrided methods
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", fName='" + fName + '\'' +
                ", lName='" + lName + '\'' +
                ", password=" + password +
                ", email='" + email + '\'' +
                ", lastLogin=" + lastLogin +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", isMemberOf=" + isMemberOf + '\'' +
                ", token=" + token +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != user.id) return false;
        if (fName != null ? !fName.equals(user.fName) : user.fName != null) return false;
        if (lName != null ? !lName.equals(user.lName) : user.lName != null) return false;
        if (!password.equals(user.password)) return false;
        if (!email.equals(user.email)) return false;
        if (lastLogin != null ? !lastLogin.equals(user.lastLogin) : user.lastLogin != null) return false;
        if (phoneNumber != null ? !phoneNumber.equals(user.phoneNumber) : user.phoneNumber != null) return false;
        if (isMemberOf != null ? !isMemberOf.equals(user.isMemberOf) : user.isMemberOf != null) return false;
        return token.equals(user.token);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (fName != null ? fName.hashCode() : 0);
        result = 31 * result + (lName != null ? lName.hashCode() : 0);
        result = 31 * result + password.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + (lastLogin != null ? lastLogin.hashCode() : 0);
        result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        result = 31 * result + (isMemberOf != null ? isMemberOf.hashCode() : 0);
        result = 31 * result + token.hashCode();
        return result;
    }
    //endregion
}
