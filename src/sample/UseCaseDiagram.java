package sample;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Darko on 8/12/2014.
 */
public class UseCaseDiagram {
    public List<Case> cases;
    public String title;

    public UseCaseDiagram() {
        cases = new ArrayList<Case>();
    }

    public UseCaseDiagram(String title, ArrayList<Case> cases){
        this.title = title;
        this.cases = cases;
    }

    @Override
    public String toString(){
        StringBuffer result = new StringBuffer();
        result.append("@startuml \n");
        result.append("title " + title + "\n");

        for(Case temp: cases){
            result.append(temp.toString());
        }
        result.append(" @enduml \n");
        return result.toString();
    }
}
