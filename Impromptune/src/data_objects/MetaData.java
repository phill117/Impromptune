package data_objects;

import virtuouso.Degree;

import java.util.*;

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

        if(beattype == 4){ // for n/4 times
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
            for(Note currentNote : noteList){

            }
        }

        return beats;
    }

    //ignore this for now
    double chordFrequencies[][] =
            //1     2       3       4       5       6       7
            {{1/7,  1/7,    1/7,    1/7,    1/7,    1/7,    1/7}, //tonic to ...
             {0,    .3,     0,      0,      .6,     0,      .1}, //supertonic to ...
             {0,    0,      .2,     .4,      0,     .4,     0}, //mediant to ...
             {.3,   .2,     0,      .2,      .2,    0,      .1}, //etc...
             {.5,   0,      0,      0,       .3,    .2,     0},
             {0,    .5,     0,      .3,      0,     .2,     0},
             {.9,   0,      0,      0,       0,     0,      .1}};

    /**
     * returns a degree that is the root of the next chord in the progression
     * NOTE: look below for an overloaded method
     * @param degree : the degree to transition away from
     * @param order : the order (or memory) of the markov model
     * @return : the next Degree (effectively the next chord) in the progression
     */

    public Degree transition(Degree degree, Set<Degree> possibilities, int order){

        // there is only one compatible chord, choose that one
        if(possibilities.size() == 1) for(Degree d : possibilities) return d;
        HashSet<Degree> notPossibilities = new HashSet<>();
        HashSet<Degree> actualPossibilities = new HashSet<>();

        //separate the set of given next chords into possible (from the graph) and not possible
        //for both major and minor
        if(MetaData.getInstance().isMajor()) {
            switch (degree) {
                default:
                case Tonic: //all
                    for (Degree d : possibilities)
                        actualPossibilities.add(d);
                    break;
                case Supertonic: // 2 5 7
                    for (Degree d : possibilities) {
                        int i = d.toInt();
                        if (i != 2 && i != 5 && i != 7) notPossibilities.add(d);
                        else actualPossibilities.add(d);
                    }
                    break;
                case Mediant: // 3 4 6
                    for (Degree d : possibilities) {
                        int i = d.toInt();
                        if (i != 3 && i != 4 && i != 6) notPossibilities.add(d);
                        else actualPossibilities.add(d);
                    }
                    break;
                case Subdominant: // 1 2 4 5 7
                    for (Degree d : possibilities) {
                        int i = d.toInt();
                        if (i != 2 && i != 5 && i != 7 && i != 1 && i != 4) notPossibilities.add(d);
                        else actualPossibilities.add(d);
                    }
                    break;
                case Dominant: // 1 5 6
                    for (Degree d : possibilities) {
                        int i = d.toInt();
                        if (i != 1 && i != 5 && i != 6) notPossibilities.add(d);
                        else actualPossibilities.add(d);
                    }
                    break;
                case Submediant: // 2 4 6
                    for (Degree d : possibilities) {
                        int i = d.toInt();
                        if (i != 2 && i != 4 && i != 6) notPossibilities.add(d);
                        else actualPossibilities.add(d);
                    }
                    break;
                case Leading: // 1 7
                    for (Degree d : possibilities) {
                        int i = d.toInt();
                        if (i != 1 && i != 7) notPossibilities.add(d);
                        else actualPossibilities.add(d);
                    }
                    break;
            }
        }

        //return a choice based on the resulting sets
        return getChoice(actualPossibilities,notPossibilities);
    }

    /**
     * Double argument wrapper for method above
     */
    public Degree transition(Degree degree, Set<Degree> possibilities){
        return transition(degree,possibilities,1);
    }

    private Degree getChoice(HashSet<Degree> actualPossibilities, HashSet<Degree> notPossibilities){
        if(actualPossibilities.size() == 1){ for(Degree d : actualPossibilities) return d;}
        else if(actualPossibilities.size() > 0){

            //TODO : make the choice of the actual possible choices weighted

            Random random = new Random();
            Degree[] degrees = new Degree[0];
            actualPossibilities.toArray(degrees);
            return degrees[random.nextInt(degrees.length)];
        }else{ //only not possibilities has elements
            Random random = new Random();
            Degree[] degrees = new Degree[0];
            notPossibilities.toArray(degrees);
            return degrees[random.nextInt(degrees.length)];
        }

        //will never happen (making the compiler happy)
        return Degree.Tonic;
    }
}
