package sample.Sequence;

/**
 * Created by darko on 15.8.14.
 */
public class Sequence {
    public Actor actor1, actor2;
    public String arrowType;
    public String description;

    public Sequence(){}

    public Sequence(Actor actor1, Actor actor2, String arrowType, String description) {
        this.actor1 = actor1;
        this.actor2 = actor2;
        this.arrowType = arrowType;
        this.description = description;
    }
}
