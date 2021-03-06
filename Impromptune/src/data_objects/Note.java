package data_objects;

/**
 * Created by Sean on 3/25/2015.
 */
public class Note {

    int octave = 4;
    char pitch = 'C';
    int accidental = 0;
    int duration;   //shall be counted as a fraction of one beat
    String type;
    String tied = null;

    boolean dotted = false;

    int staffNo = 1; //default treble

    public Note() {}

    public Note(char pitch, int accidental , int octave, int duration){
        this.pitch = pitch;
        this.accidental = accidental;
        this.octave = octave;
        this.duration = duration;
    }



    public static Note makeNote(String pitch ,int octave,int duration){
        System.out.println("pitch : "+pitch);
        if(pitch.length() == 1)
            return new Note(pitch.charAt(0),0,octave,duration);

        int acc = 0;
        if(pitch.charAt(1) == 'b') acc--;
        else if(pitch.charAt(1) == '#') acc++;
        return new Note(pitch.charAt(0),acc,octave,duration);
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

//    public int getOctaveGivenPart(int partnum, int totalparts){
//        if(partnum == 1) return octave;
//        if(totalparts == 3){
//            if(partnum == 2)
//        }
//    }

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

    public boolean isDotted() {
        return dotted;
    }

    public void setDotted(boolean dotted) {
        this.dotted = dotted;
    }

    public void setStaffNo(int part){
        int partcount = MetaData.getInstance().getPartCount();
        if(partcount < 3){
            staffNo = part;
            return;
        }

        if(partcount == 3){
            switch (part){
                default:
                case 1: staffNo = 1; return;
                case 2: staffNo = 2;return;
                case 3: if(octave>4) staffNo = 1; else staffNo = 2;return;
            }
        }

        if(partcount == 4){
            switch (part){
                default:
                case 1: staffNo = 1;return;
                case 2: staffNo = 2;return;
                case 3:
                case 4: if(octave>4) staffNo = 1; else staffNo = 2;return;
            }
        }

    }

    public int getStaffNo() {
        return staffNo;
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

//    public enum Type{
//        Soprano,Alto,Tenor,Bass
//    }
}
