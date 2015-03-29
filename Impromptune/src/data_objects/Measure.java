package data_objects;

import java.util.ArrayList;

/**
 * Created by Sean on 3/25/2015.
 */
public class Measure {

    private ArrayList<Note> notes;
    private int measureNo;

    public Measure(int measureNo){
        this.measureNo = measureNo;
        notes = new ArrayList<>();
    }

    public Note noteAt(int b){
        return notes.get(b);
    }

    public void addNote(Note note){
        notes.add(note);
    }

    public void addNoteAt(int n, Note note){
        notes.add(n, note);
    }
}
