package DropThatFile.models;

import DropThatFile.engines.RSAEngine;

import java.util.Date;

/**
 * Created by Nicol on 21/03/2017.
 */
public class File {

    //region Attributs
    private int id;
    private String name;
    private String password;
    private Date created;
    private String description;
    //endregion

    //region Constructeur
    public File(int id, String name, String password, Date created, String description) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.created = created;
        this.description = description;
    }
    //endregion

    //region Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public Date getCreated() {
        return created;
    }

    public String getDescription() {
        return description;
    }
    //endregion

    //region Override Methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        File file = (File) o;

        if (id != file.id) return false;
        if (name != null ? !name.equals(file.name) : file.name != null) return false;
        if (password != null ? !password.equals(file.password) : file.password != null) return false;
        if (created != null ? !created.equals(file.created) : file.created != null) return false;
        return description != null ? description.equals(file.description) : file.description == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (created != null ? created.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "File{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password=" + password +
                ", created=" + created +
                ", description='" + description + '\'' +
                '}';
    }
    //endregion
}
