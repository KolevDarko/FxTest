package sample;

/**
 * Created by Darko on 8/12/2014.
 */
public class Action {
    public String name;
    public String label1,label2;
    public String alias;

    public Action(String name, String alias) {
        this.name = name;
        this.alias = alias;
        this.label1 = "";
        this.label2 = "";
    }

    public Action(String name, String alias, String label1, String label2) {
        this.name = name;
        this.label1 = ": " + label1;
        this.label2 = ": " + label2;
        this.alias = alias;
    }
    public void setLabel1(String label){
        this.label1 = ": " + label;
    }

    public void setLabel2(String label){
        this.label2 = ": " + label;
    }

    @Override
    public String toString(){
        return "(" + this.name + ") as " + "(" + this.alias + ")\n";

    }
}
