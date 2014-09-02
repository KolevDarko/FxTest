package sample.Sequence;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

/**
 * Created by Darko on 8/13/2014.
 */
public class ActionFlow {
    public TextField ActionText, aLeft, aRight;
    public CheckBox actionBox;

    public TextField getActionText() {
        return ActionText;
    }

    public void setActionText(TextField actionText) {
        ActionText = actionText;
    }

    public TextField getaLeft() {
        return aLeft;
    }

    public void setaLeft(TextField aLeft) {
        this.aLeft = aLeft;
    }

    public TextField getaRight() {
        return aRight;
    }

    public void setaRight(TextField aRight) {
        this.aRight = aRight;
    }

    public CheckBox getActionBox() {
        return actionBox;
    }

    public void setActionBox(CheckBox actionBox) {
        this.actionBox = actionBox;
    }

    public ActionFlow()
    {
        actionBox = new CheckBox();
        actionBox.setSelected(false);
    }


    public ActionFlow(TextField actionText, TextField aLeft, TextField aRight) {
        ActionText = actionText;
        this.aLeft = aLeft;
        this.aRight = aRight;
        actionBox = new CheckBox();
        actionBox.setSelected(false);
    }

    public ActionFlow(String actionText, String aLeft, String aRight) {
        ActionText = new TextField(actionText);
        this.aLeft = new TextField(aLeft);
        this.aRight = new TextField(aRight);
        actionBox = new CheckBox();
        actionBox.setSelected(false);
    }



    @Override
    public String toString() {
        String rez = "\"" + aLeft.getText() + "\"" + " -> " + "\"" + aRight.getText() + "\" : " + "\"" + ActionText.getText() + "\"";
        System.out.println(rez);
        return rez;
    }
}
