package sample.Sequence;

import javafx.scene.control.TextField;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by darko on 15.8.14.
 */
public class SequenceDiagram {
    public List<Actor> actors;
    public List<ActionFlow> sequences;

    public SequenceDiagram(){
        actors = new ArrayList<>();
        sequences = new ArrayList<>();
    }


    public void setActors(ArrayList<TextField> actorsFields) {
        for(TextField actorField: actorsFields){
            this.actors.add(new Actor(actorField.getText()));
        }
    }

    public void setSequences(List<ActionFlow> actionFields){
        sequences = actionFields;
    }

    @Override
    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append("@startuml \n");
//        for(Actor a: actors){
//            result.append("participant " + a.name + "\n");
//        }
        for(ActionFlow a: sequences){
            if(a.getActionBox().isSelected()){
                result.append(a + "\n");
            }
        }
        result.append("@enduml\n");
        return result.toString();
    }
}
