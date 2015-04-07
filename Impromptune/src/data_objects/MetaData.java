package data_objects;

import java.util.ArrayList;

/**
 * Created by Sean on 3/28/2015.
 */
public class MetaData {

    int tempo = 120;
    int divisions = 8; // divisions per quarter note (unit for duration)
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

    //gets the first note of every chord (Impromptune only accepts one note at a time so this is default) and puts it in a list
    public ArrayList<Note> getNoteList(){
        ArrayList notes = new ArrayList<>();

        for(Measure measure : measures){
            for(ArrayList<Note> chords : measure.getChords()){
                notes.add(chords.get(0));
            }
        }

        return notes;
    }

    public ArrayList<ArrayList<Note>> getBeatList(){

        ArrayList<Note> noteList = getNoteList();
        ArrayList<ArrayList<Note>> beats = new ArrayList<>();


        int currentDuration = 0;
        ArrayList<Note> currentBeat = null;
        int leftover = 0;

        if(beattype == 4){
            for(Note currentNote : noteList){
                if(currentDuration == 0 || leftover > 0){
                    //if got overfilled...
                    if(leftover > 0){
                        currentDuration = leftover;
                        leftover = 0;
                    }
                    //create a new beat set if the last one gets filled
                    if(currentBeat != null) beats.add(currentBeat);
                    currentBeat = new ArrayList<>();
                }

                //add the current note to the current beat
                currentBeat.add(currentNote);
                //and add that notes duration to the running total
                currentDuration += currentNote.getDuration();

                //if the running total exceeds the number of possible divisions...
                if(currentDuration > divisions){
                    //cache the last note's duration
                    int currentNoteDuration = currentNote.getDuration();
                    //loop through to add whole beats in case the note has a beat larger than the division (whole note in 4/4)
                    while(currentNoteDuration > divisions){
                        currentNoteDuration -= divisions;
                        beats.add(currentBeat);
                        currentBeat = new ArrayList<>();
                        currentBeat.add(currentNote);
                    }
                    leftover = currentDuration % divisions; //or leftover = currentNoteDuration;
                }
            }
        }else{ // its n/8 time

        }

        return beats;
    }
}
