package utils;

/**
 * Created by ben on 4/3/2015.
 */
import java.util.LinkedList;

//size limited queue, auto removes elements at the end of the list as you pass the limit
public class LimitedQueue<E> extends LinkedList<E> {
    private int limit;

    public LimitedQueue(int limit) {
        this.limit = limit;
    }

    @Override
    public boolean add(E o) {
        super.add(o);
        while (size() > limit) { super.remove(); }
        return true;
    }
}
