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

    //'sign' and line not implemented to change
    String sign = "G";
    String line = "2";

    ArrayList<Measure> measures = new ArrayList<>();

    private static MetaData data = null;


    public static MetaData getInstance(){
        if(data == null)
            data = new MetaData();
        return data;
    }

    private MetaData(){}

    public String getLine() {
        return line;
    }

    public String getSign() {
        return sign;
    }

    public boolean isMajor() {
        return major;
    }

    public int getDivisions() {
        return divisions;
    }

    public void setDivisions(int divisions) {
        this.divisions = divisions;
    }

    public void setMajor(boolean major) {
        this.major = major;
    }

    public int getSharps() {
        return sharps;
    }

    public void setSharps(int sharps) {
        this.sharps = sharps;
    }

    public int getBeats() {
        return beats;
    }

    public void setBeats(int beats) {
        this.beats = beats;
    }

    public int getBeattype() {
        return beattype;
    }

    public void setBeattype(int beattype) {
        this.beattype = beattype;
    }

    public int getTempo() {
        return tempo;
    }

    public void setTempo(int tempo) {
        this.tempo = tempo;
    }

    public void addMeasure(Measure m){
        measures.add(m);
    }

    public ArrayList<Measure> getMeasures() {
        return measures;
    }
}
