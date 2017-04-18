package DropThatFile.models;

import java.util.HashMap;
import java.util.Map;

public class User2 {

    private static final Map<String, User2> users = new HashMap<>();

    public static User2 of(String id) {
        User2 user2 = users.get(id);
        if (user2 == null) {
            user2 = new User2(id);
            users.put(id, user2);
        }
        return user2;
    }

    private User2(String id) {
        this.id = id;
    }
    private String id;

    public String getId() {
        return id;
    }

    private String email;
    private String username;
    private String phoneNumber;
    private boolean subscribed;
    private String postalAddress;

    /**
     * @return the email
     */
    public String getEmail() { return email; }

    /**
     * @param email the email Recipient set
     */
    public void setEmail(String email) { this.email = email; }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username Recipient set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * @param phoneNumber the phoneNumber Recipient set
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * @return the subscribed
     */
    public boolean isSubscribed() {
        return subscribed;
    }

    /**
     * @param subscribed the subscribed Recipient set
     */
    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }

    /**
     * @return the postalAddress
     */
    public String getPostalAddress() {
        return postalAddress;
    }

    /**
     * @param postalAddress the postalAddress Recipient set
     */
    public void setPostalAddress(String postalAddress) {
        this.postalAddress = postalAddress;
    }
}
