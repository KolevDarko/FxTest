package sample.Sequence;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

/**
 * Created by Darko on 8/13/2014.
 */
public class ActionFields {
    public TextField ActionText;
    public ComboBox Actor1, Actor2, ActionType;

    public ActionFields(ComboBox actionType) {
        ActionType = actionType;
    }

    public ActionFields(TextField actionText, ComboBox actor1, ComboBox actor2, ComboBox actionType) {
        this.ActionText = actionText;
        Actor1 = actor1;
        Actor2 = actor2;
        ActionType = actionType;
    }

    @Override
    public String toString() {
        return "";
    }
}
