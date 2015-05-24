package utils;

/**
 * Created by ben on 4/4/2015.
 */

//generic pairs i.e. Pair<String,Integer>
public class Pair<T, U> {
    public final T t;
    public final U u;

    public Pair(T t, U u) {
        this.t= t;
        this.u= u;
    }

    public T first(){return t;}
    public U second(){return u;}
}
