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
    private List<Group> isMemberOf = new List<Group>() {
        public int size() {
            return 0;
        }

        public boolean isEmpty() {
            return false;
        }

        public boolean contains(Object o) {
            return false;
        }

        public Iterator<Group> iterator() {
            return null;
        }

        public Object[] toArray() {
            return new Object[0];
        }

        public <T> T[] toArray(T[] a) {
            return null;
        }

        public boolean add(Group group) {
            return false;
        }

        public boolean remove(Object o) {
            return false;
        }

        public boolean containsAll(Collection<?> c) {
            return false;
        }

        public boolean addAll(Collection<? extends Group> c) {
            return false;
        }

        public boolean addAll(int index, Collection<? extends Group> c) {
            return false;
        }

        public boolean removeAll(Collection<?> c) {
            return false;
        }

        public boolean retainAll(Collection<?> c) {
            return false;
        }

        public void clear() {

        }

        public Group get(int index) {
            return null;
        }

        public Group set(int index, Group element) {
            return null;
        }

        public void add(int index, Group element) {

        }

        public Group remove(int index) {
            return null;
        }

        public int indexOf(Object o) {
            return 0;
        }

        public int lastIndexOf(Object o) {
            return 0;
        }

        public ListIterator<Group> listIterator() {
            return null;
        }

        public ListIterator<Group> listIterator(int index) {
            return null;
        }

        public List<Group> subList(int fromIndex, int toIndex) {
            return null;
        }
    };
    //endregion

    //region Contructeurs
    public User(int id, String fName, String lName, RSAEngine password, String mail, Date lastLogin, List<Group> isMemberOf) {
        this.id = id;
        this.fName = fName;
        this.lName = lName;
        this.password = password;
        this.mail = mail;
        this.lastLogin = lastLogin;
        this.isMemberOf = isMemberOf;
    }

    public User(int id, String fName, String lName, RSAEngine password, String mail, Date lastLogin, Group isMemberOf) {
        this.id = id;
        this.fName = fName;
        this.lName = lName;
        this.password = password;
        this.mail = mail;
        this.lastLogin = lastLogin;
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

    public List<Group> getIsMemberOf() {
        return isMemberOf;
    }
    //endregion

    //region Overrided Methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != user.id) return false;
        if (fName != null ? !fName.equals(user.fName) : user.fName != null) return false;
        if (lName != null ? !lName.equals(user.lName) : user.lName != null) return false;
        if (password != null ? !password.equals(user.password) : user.password != null) return false;
        if (mail != null ? !mail.equals(user.mail) : user.mail != null) return false;
        if (lastLogin != null ? !lastLogin.equals(user.lastLogin) : user.lastLogin != null) return false;
        return isMemberOf != null ? isMemberOf.equals(user.isMemberOf) : user.isMemberOf == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (fName != null ? fName.hashCode() : 0);
        result = 31 * result + (lName != null ? lName.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (mail != null ? mail.hashCode() : 0);
        result = 31 * result + (lastLogin != null ? lastLogin.hashCode() : 0);
        result = 31 * result + (isMemberOf != null ? isMemberOf.hashCode() : 0);
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
                ", isMemberOf=" + isMemberOf +
                '}';
    }
    //endregion
}
