package virtuouso;

/**
 * Created by ben on 4/3/2015.
 */

//scale degrees used by scale/chord building functions
public enum Degree {
//    NonScaleDegree(-1),
    Tonic(0),
    Supertonic(1),
    Mediant(2),
    Subdominant(3),
    Dominant(4),
    Submediant(5),
    Leading(6),
    NonScaleDegree(7);

    private int degree;
    private String nonScaleTone;
//    Degree(String tone) { this.nonScaleTone = tone; }
    Degree(int degree) { this.degree = degree; }

    public int toInt() { return this.degree; }
    public void setNonScaleDegree(String note) { nonScaleTone = note; }
    public String getNonScaleTone() { return nonScaleTone; }
}
