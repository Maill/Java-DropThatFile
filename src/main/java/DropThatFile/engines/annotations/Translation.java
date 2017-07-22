package DropThatFile.engines.annotations;

/**
 * Created by Olivier on 25/04/2017.
 */
public enum Translation {
    ENGLISH("ENGLISH"),
    FRENCH("FRENCH");

    private String translatedText;

    Translation(String newText){
        translatedText = newText;
    }

    public String toString(){
        return translatedText;
    }
}
