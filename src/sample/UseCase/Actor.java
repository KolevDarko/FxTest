package sample.UseCase;

/**
 * Created by Darko on 8/12/2014.
 */
public class Actor {
    public String name;
    public String stereotype;
    public String alias;
    public Actor parent;

    public Actor(String name, String alias, String stereotype) {
        this.name = name;
        this.stereotype = stereotype;
        this.alias = alias;
    }

    public Actor(String name, String alias) {
        this.name = name;
        this.alias = alias;
        this.stereotype = "";
    }

    public void setParent(Actor parent){
        this.parent = parent;
    }

    @Override
    public String toString(){
        return ":"+this.name+":" + " as " + this.alias + "\n";
    }

}
