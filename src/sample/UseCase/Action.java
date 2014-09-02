package sample.UseCase;

import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

/**
 * Created by Darko on 8/12/2014.
 */
public class Action {
    public TextField actionField;
    public CheckBox actionBox;

    public Action(String name){
        actionField = new TextField(name);
        actionBox = new CheckBox();
    }

    @Override
    public String toString(){
        if(actionBox.isSelected()) {
            return "(" + actionField.getText() + ") ";
        }
        return "";
    }
}
