package DropThatFile.models;

import DropThatFile.engines.RSAEngine;

import java.security.PublicKey;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Nicol on 21/03/2017.
 */
public class Group {

    //region Attributs
    private int id;
    private String name;
    private PublicKey public_key;
    private HashMap<String, File> sharedFiles;
    //endregion

    //region Constructeurs
    public Group(int id, String name, PublicKey public_key) {
        this.id = id;
        this.name = name;
        this.public_key = public_key;
    }
    //endregion

    //region Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public PublicKey getPublic_key() {
        return public_key;
    }

    public HashMap<String, File> getSharedFiles() {
        return sharedFiles;
    }
    //endregion

    //region Setters
    public void setSharedFiles(HashMap<String, File> sharedFiles) { this.sharedFiles = sharedFiles; }
    //endregion

    //region Overrided Methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Group group = (Group) o;

        if (id != group.id) return false;
        if (name != null ? !name.equals(group.name) : group.name != null) return false;
        if (public_key != null ? !public_key.equals(group.public_key) : group.public_key != null) return false;
        return sharedFiles != null ? sharedFiles.equals(group.sharedFiles) : group.sharedFiles == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (public_key != null ? public_key.hashCode() : 0);
        result = 31 * result + (sharedFiles != null ? sharedFiles.hashCode() : 0);
        return result;
    }


    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", public_key=" + public_key +
                ", sharedFiles=" + sharedFiles +
                '}';
    }
    //endregion
}
