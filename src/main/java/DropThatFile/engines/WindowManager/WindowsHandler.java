package DropThatFile.engines.WindowManager;

import javafx.stage.Stage;

/**
 * Created by Nicolas on 02/03/2017.
 */

public abstract class WindowsHandler {


    protected Stage jfxStage;

    public WindowsHandler(Stage jfxStage){
        this.jfxStage = jfxStage;
    }

    public Stage getJfxStage() {
        return jfxStage;
    }

}
