package sample.Sequence;

/**
 * Created by darko on 15.8.14.
 */
public class Actor {
    public String name;
    public Actor(String _name){
        name = _name;
    }

    @Override
    public String toString() {
        return name;
    }
}
