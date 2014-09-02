package sample.Sequence;

import javafx.scene.control.TextField;

/**
 * Created by darko on 15.8.14.
 */
public class Sequence {
    public TextField actor1, actor2, description;

    public Sequence(){}

    public Sequence(TextField actor1, TextField actor2, TextField description) {
        this.actor1 = actor1;
        this.actor2 = actor2;
        this.description = description;
    }

    public String toString(){
        return actor1.getText() + " --> " + actor1.getText() + " : " + description.getText();
    }

    public TextField getActor1() {
        return actor1;
    }

    public void setActor1(TextField actor1) {
        this.actor1 = actor1;
    }

    public TextField getActor2() {
        return actor2;
    }

    public void setActor2(TextField actor2) {
        this.actor2 = actor2;
    }

    public TextField getDescription() {
        return description;
    }

    public void setDescription(TextField description) {
        this.description = description;
    }


}
