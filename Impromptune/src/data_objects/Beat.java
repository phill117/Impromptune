package data_objects;

import java.util.ArrayList;

/**
 * Created by Sean on 3/25/2015.
 */
public class Beat {

    private ArrayList<Note> notes;

    public Beat(){
        notes = new ArrayList<>();
    }

    public void addNote(Note note){
        notes.add(note);
    }

    public void addNoteAt(int n, Note note){
        notes.add(n,note);
    }

    public Note noteAt(int n){
        return notes.get(n);
    }

    public void setNotes(ArrayList<Note> notes) {
        this.notes = notes;
    }
    //cause for each > life lulz
    public ArrayList<Note> getNotes() {
        return notes;
    }
}
