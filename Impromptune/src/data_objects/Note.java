package data_objects;

/**
 * Created by Sean on 3/25/2015.
 */
public class Note {

    int octave;
    char pitch;
    int accidental;
    int duration;   //shall be counted as a fraction of one beat
    String type;
    String tied = null;

    public Note() {

    }

    public boolean isRest(){
        return pitch == 'r';
    }

    public int getAccidental() {
        return accidental;
    }

    public void setAccidental(int accidental) {this.accidental = accidental;}

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {this.duration = duration;}

    public int getOctave() {
        return octave;
    }

    public void setOctave(int octave) {this.octave = octave;}

    public char getPitch() {
        return pitch;
    }

    public void setPitch(char pitch) {this.pitch = pitch;}

    public boolean isTied() {
        return tied != null;
    }

    public void setTied(String tied) {this.tied = tied;}

    public String getTiedType(){return this.tied;}

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isChordTone(){

        //TODO - impement this crap later

        return true;
    }

    public int getScaleDegree(){

        //TODO - impement this crap later

        return 1;
    }

    @Override
    public String toString() {
        String str = new String();
        char c = this.getPitch();
        c -= 32;
        str += c;
        switch (this.getAccidental()) {
            case 1:
                str += '#';
                break;
            case 0:
                break;
            case -1:
                str += 'b';
                break;
        }

        return str;
    }
}
