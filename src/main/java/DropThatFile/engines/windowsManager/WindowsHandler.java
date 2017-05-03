package DropThatFile.engines.windowsManager;

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

    public void closeForm(){ jfxStage.close(); }
}
