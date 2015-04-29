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

    public Degree getTonic() {
        switch(degree) {
            case 0: return Tonic;
            case 1: return Supertonic;
            case 2: return Mediant;
            case 3: return Subdominant;
            case 4: return Dominant;
            case 5: return Submediant;
            case 6: return Leading;
        }

        return Tonic;
    }

    public Degree getSupertonic() {
        switch(degree) {
            case 0: return Supertonic;
            case 1: return Mediant;
            case 2: return Subdominant;
            case 3: return Dominant;
            case 4: return Submediant;
            case 5: return Leading;
            case 6: return Tonic;
        }

        return Supertonic;
    }

    public Degree getMediant() {
        switch(degree) {
            case 0: return Mediant;
            case 1: return Subdominant;
            case 2: return Dominant;
            case 3: return Submediant;
            case 4: return Leading;
            case 5: return Tonic;
            case 6: return Supertonic;
        }

        return Mediant;
    }

    public Degree getSubdominant() {
        switch(degree) {
            case 0: return Subdominant;
            case 1: return Dominant;
            case 2: return Submediant;
            case 3: return Leading;
            case 4: return Tonic;
            case 5: return Supertonic;
            case 6: return Mediant;
        }

        return Subdominant;
    }

    public Degree getDominant() {
        switch(degree) {
            case 0: return Dominant;
            case 1: return Submediant;
            case 2: return Leading;
            case 3: return Tonic;
            case 4: return Supertonic;
            case 5: return Mediant;
            case 6: return Subdominant;
        }

        return Dominant;
    }
    public Degree getSubmediant() {
        switch(degree) {
            case 0: return Submediant;
            case 1: return Leading;
            case 2: return Tonic;
            case 3: return Supertonic;
            case 4: return Mediant;
            case 5: return Subdominant;
            case 6: return Dominant;
        }

        return Submediant;
    }

    public Degree getLeading() {
        switch(degree) {
            case 0: return Leading;
            case 1: return Tonic;
            case 2: return Supertonic;
            case 3: return Mediant;
            case 4: return Subdominant;
            case 5: return Dominant;
            case 6: return Submediant;
        }

        return Leading;
    }
}
