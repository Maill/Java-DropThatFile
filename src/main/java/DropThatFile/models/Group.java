package DropThatFile.models;

import DropThatFile.engines.RSAEngine;

import java.security.PublicKey;
import java.util.List;

/**
 * Created by Nicol on 21/03/2017.
 */
public class Group {

    //region Attributs
    private int id;
    private String name;
    private PublicKey public_key;
    private List<Group> childOf;
    private List<File> sharedFiles;
    //endregion

    //region Constructeurs
    public Group(int id, String name, PublicKey public_key) {
        this.id = id;
        this.name = name;
        this.public_key = public_key;
    }

    public Group(int id, String name, PublicKey public_key, List<Group> childOf) {
        this.id = id;
        this.name = name;
        this.public_key = public_key;
        this.childOf = childOf;
    }

    public Group(int id, String name, PublicKey public_key, List<Group> childOf, List<File> sharedFiles) {
        this.id = id;
        this.name = name;
        this.public_key = public_key;
        this.childOf = childOf;
        this.sharedFiles = sharedFiles;
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

    public List<Group> getChildOf() {
        return childOf;
    }

    public List<File> getSharedFiles() {
        return sharedFiles;
    }
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
        if (childOf != null ? !childOf.equals(group.childOf) : group.childOf != null) return false;
        return sharedFiles != null ? sharedFiles.equals(group.sharedFiles) : group.sharedFiles == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (public_key != null ? public_key.hashCode() : 0);
        result = 31 * result + (childOf != null ? childOf.hashCode() : 0);
        result = 31 * result + (sharedFiles != null ? sharedFiles.hashCode() : 0);
        return result;
    }


    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", public_key=" + public_key +
                ", childOf=" + childOf +
                ", sharedFiles=" + sharedFiles +
                '}';
    }
    //endregion
}
