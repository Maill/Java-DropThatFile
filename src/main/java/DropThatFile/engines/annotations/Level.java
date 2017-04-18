package DropThatFile.engines.annotations;

/**
 * Created by olefebvre on 18/04/2017.
 */
public enum Level {
    IMPROVEMENT("IMPROVEMENT"),
    EVOLUTION("EVOLUTION"),
    BUG("BUG"),
    CRITICAL("CRITICAL");

    private String description;

    Level(String desc){
        description = desc;
    }

    public String toString(){
        return description;
    }
}
