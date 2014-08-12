package sample;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Darko on 8/12/2014.
 */
public class Case {
    private Actor left,right;
    private List<Action> actions;


    public Case(Actor left, Action action) {
        actions = new ArrayList<Action>();
        this.left = left;
        this.right = null;
    }

    public Case(Actor left, Actor right, List<Action> actions){
        this.left = left;
        this.right = right;
        this.actions = actions;
    }

    public void AddAction(Action act){
        actions.add(act);
    }

    @Override
    public String toString(){
        String rightString = (this.right != null)? this.right.toString() : "";
        StringBuffer output =new StringBuffer();

        output.append(left + rightString);
        for(Action a: actions){
            output.append(a);
        }
        output.append(left.alias + "->" + actions.get(0).alias + "\n");

        for (int i = 0; i < actions.size()-1; i++) {
            output.append(actions.get(i).alias + " -> " + actions.get(i+1).alias + "\n");
        }

        if(rightString != "") {
            output.append(actions.get(actions.size() - 1).alias + " -> " + right.alias + "\n");
        }
        /*output.append(left.alias + "->" + action.alias  + action.label1 + "\n");
        if(rightString != "") {

            output.append(action.alias + "->" + right.alias + action.label2 + "\n");
        }*/
        return output.toString();
    }


}