package DropThatFile.engines.windowsManager;

import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.input.KeyCode;

import java.io.File;

/**
 * Created by Olivier on 18/06/2017.
 */
public class TreeTextFieldEditor extends TreeCell<File> {
    // TextField generated on a TreeItem to edit
     private TextField textField;

    /**
     * Called after a double-click on a TreeItem
     */
    @Override
    public void startEdit() {
        super.startEdit();

        if (textField == null) {
            createTextField();
        }
        setText(null);
        setGraphic(textField);
        textField.selectAll();
    }

    /**
     * Cancel the textField and the TreeItem's name updating
     */
    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(getItem().getName());
        setGraphic(getTreeItem().getGraphic());
    }

    /**
     * Update the TreeItem's name
     * @param item TreeItem to update
     * @param empty If the node is empty or not (?)
     */
    @Override
    public void updateItem(File item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                // If the new name is null, we revert back to the old name
                if (textField != null) {
                    textField.setText(getString());
                }
                setText(null);
                setGraphic(textField);
            } else {
                setText(getString());
                setGraphic(getTreeItem().getGraphic());
            }
        }
    }

    /**
     * Create the TextField to edit the name of a TreeItem
     */
    private void createTextField() {
        /*textField = new TextField(getString());
        textField.setOnKeyReleased(t -> {
            if (t.getCode() == KeyCode.ENTER) {
                commitEdit(new File(textField.getText()));
            } else if (t.getCode() == KeyCode.ESCAPE) {
                cancelEdit();
            }
        });*/
    }

    /**
     * Get the name of the TreeItem
     * @return the name of the TreeItem
     */
    private String getString() {
        return getItem() == null ? "" : getItem().getName();
    }
}