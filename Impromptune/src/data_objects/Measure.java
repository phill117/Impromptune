package data_objects;

import java.util.ArrayList;

/**
 * Created by Sean on 3/25/2015.
 */
public class Measure {

    private ArrayList<ArrayList<Note>> chords;
    private ArrayList<Note> lastChordAdded;
    private int measureNo;

    public Measure(int measureNo){
        this.measureNo = measureNo;
        chords = new ArrayList<>();
    }

    public int getMeasureNo() {
        return measureNo;
    }

    public ArrayList<ArrayList<Note>> getChords() {
        return chords;
    }

    public ArrayList<Note> chordAt(int b){
        return chords.get(b);
    }

    public void addChord(Note firstNote){
        ArrayList<Note> chord = new ArrayList<>();
        chord.add(firstNote);
        chords.add(chord);
        lastChordAdded = chord;
    }

    public void addNoteAt(int n, Note note){
        chords.get(n).add(note);
    }

    public void addNote(Note n){
        lastChordAdded.add(n);
    }
}
