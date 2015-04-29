package data_objects;

import java.util.ArrayList;

/**
 * Created by Sean on 3/25/2015.
 */
public class Measure {

    private int measureNo;

    private ArrayList<ArrayList<Note>> parts;

    public Measure(int measureNo){
        int partnum = MetaData.getInstance().getPartCount();
        this.measureNo = measureNo;
        //chords = new ArrayList<>();
        parts = new ArrayList<>(partnum);

        for(int i = 0; i < partnum; i++){
            parts.add(new ArrayList<>());
        }
    }

    //new methods
    public ArrayList<Note> getPart(int n){
        return parts.get(n);
    }

    public void addNoteToPart(Note note, int n){
        parts.get(n).add(note);
    }

    public ArrayList<ArrayList<Note>> getParts() {
        return parts;
    }

    public int getMeasureNo() {
        return measureNo;
    }

}
