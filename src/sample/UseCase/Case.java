package sample.UseCase;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Darko on 8/12/2014.
 */
public class Case {
    public final Actor leftA,rightA;
    public List<Action> actionsL;
    public CheckBox caseBox;


    public Case(Actor left, Action action) {
        actionsL = new ArrayList<Action>();
        this.leftA = left;
        this.rightA = null;
    }

    public Case(String left, String right, final List<String> actions){
        this.actionsL = new ArrayList<>();
        this.leftA = new Actor(left);
        this.rightA = new Actor(right);
        for(String s: actions){
            this.actionsL.add(new Action(s));
        }
        caseBox = new CheckBox();
        caseBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue){
                    leftA.actorBox.setSelected(true);
                    rightA.actorBox.setSelected(true);
                    for(Action a: actionsL){
                        a.actionBox.setSelected(true);
                    }
                }
            }
        });
    }

    public void AddAction(Action act){
        actionsL.add(act);
    }

    @Override
    public String toString(){
        if(!caseBox.isSelected()){
            return "";
        }
        StringBuffer output =new StringBuffer();

        output.append(leftA + " -> ");

        for(Action a: actionsL){
            if(a.actionBox.isSelected()) {
                output.append(a + "\n");
                output.append(a + " -> ");
            }
        }
        output.append(rightA + "\n");


        return output.toString();
    }


}