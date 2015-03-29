package data_objects;

import java.util.ArrayList;

/**
 * Created by Sean on 3/28/2015.
 */
public class MetaData {

    int tempo = 120;
    int divisions = 8;
    int sharps = 0;
    boolean major = true;
    int beats = 4;
    int beattype = 4;
    ArrayList<Measure> measures = new ArrayList<>();

    private static MetaData data;


    public static MetaData getInstance(){
        if(data == null)
            data = new MetaData();
        return data;
    }

    private MetaData(){}

    public int getDivisions() {
        return divisions;
    }

    public int getSharps() {
        return sharps;
    }

    public boolean isMajor() {
        return major;
    }

    public void setDivisions(int divisions) {
        this.divisions = divisions;
    }

    public void setMajor(boolean major) {
        this.major = major;
    }

    public void setSharps(int sharps) {
        this.sharps = sharps;
    }

    public void setBeats(int beats) {
        this.beats = beats;
    }

    public void setBeattype(int beattype) {
        this.beattype = beattype;
    }

    public void setTempo(int tempo) {
        this.tempo = tempo;
    }

    public void addMeasure(Measure m){
        measures.add(m);
    }
}
