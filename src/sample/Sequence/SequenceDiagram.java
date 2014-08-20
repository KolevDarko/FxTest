package sample.Sequence;

import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by darko on 15.8.14.
 */
public class SequenceDiagram {
    public List<Actor> actors;
    public List<Sequence> sequences;

    public SequenceDiagram(){
        actors = new ArrayList<Actor>();
        sequences = new ArrayList<Sequence>();
    }


    public void setActors(ArrayList<TextField> actorsFields) {
        for(TextField actorField: actorsFields){
            this.actors.add(new Actor(actorField.getText()));
        }
    }

    public void setSequences(List<ActionFields> actionFields){
        Actor actor1, actor2;
        String arrowType, description;

        for(ActionFields a: actionFields){
            actor1 = new Actor(String.valueOf(a.Actor1.getValue()));
            actor2 = new Actor(String.valueOf(a.Actor2.getValue()));
            arrowType = String.valueOf(a.ActionType.getValue());
            description = a.ActionText.getText();
            sequences.add(new Sequence(actor1, actor2, arrowType, description));

        }
    }
/*
* @startuml
actor Bob #red
' The only difference between actor and participant is the drawing
participant Alice
participant "I have a really\nlong name" as L #99 FF99
Alice ->Bob: Authentication Request
Bob ->Alice: Authentication Response
Bob ->L: Log transaction
@enduml
*
* */
    @Override
    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append("@startuml \n");
        for(Actor a: actors){
            result.append("participant " + a.name + "\n");
        }
        for(Sequence s: sequences){
            result.append(s.actor1 + " " +s.arrowType + " " + s.actor2 + ": " + s.description + "\n");
        }
        result.append("@enduml\n");
        return result.toString();
    }
}
