package data_objects;

import java.util.ArrayList;

/**
 * Created by Sean on 3/25/2015.
 */
public class Measure {

    private ArrayList<Beat> beats;
    private int beatNums;

    public Measure(int beatNums){
        this.beatNums = beatNums;
        beats = new ArrayList<>();
    }

    public int getBeatCount(){
        return beatNums;
    }

    public Beat beatAt(int b){
        return beats.get(b);
    }

    public void addBeat(Beat beat){
        beats.add(beat);
    }

    public void addBeatAt(int n, Beat beat){
        beats.add(n, beat);
    }
}
