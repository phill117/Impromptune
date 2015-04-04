package virtuouso;

/**
 * Created by ben on 4/3/2015.
 */
public enum Degree {
    Tonic(0),
    Supertonic(1),
    Mediant(2),
    Subdominant(3),
    Dominant(4),
    Submediant(5),
    Leading(6);

    private int degree;

    private Degree(int degree) { this.degree = degree; }

    public int toInt() { return this.degree; }
}
