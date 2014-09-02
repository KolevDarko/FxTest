package sample.UseCase;

import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

/**
 * Created by Darko on 8/12/2014.
 */
public class Actor {

    public TextField actorField;
    public CheckBox actorBox;


    public Actor(String name){

        this.actorField = new TextField(name);
        this.actorBox = new CheckBox();
    }


    @Override
    public String toString(){
        if(actorBox.isSelected()) {
            return " :" + actorField.getText() + ": ";
        }
        return "";
    }

}
