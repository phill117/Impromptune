package data_objects;

/**
 * Created by Sean on 3/25/2015.
 */
public class Note {

    int octave;
    char pitch;
    int accidental;
    int duration;   //shall be counted as a fraction of one beat
    boolean tied = false;

    public Note(){

    }

    public boolean isRest(){
        return pitch == 'r';
    }

    public void setAccidental(int accidental) {this.accidental = accidental;}

    public void setDuration(int duration) {this.duration = duration;}

    public void setOctave(int octave) {this.octave = octave;}

    public void setPitch(char pitch) {this.pitch = pitch;}

    public void setTied(boolean tied) {this.tied = tied;}



    public boolean isChordTone(){

        //TODO - impement this crap later

        return true;
    }

    public int getScaleDegree(){

        //TODO - impement this crap later

        return 1;
    }
}
