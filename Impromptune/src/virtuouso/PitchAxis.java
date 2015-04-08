package virtuouso;

import data_objects.Note;
import utils.Pair;

/**
 * Created by ben on 4/3/2015.
 */

//the notes plotted in markovian transition tables
public class PitchAxis {

    String pitchAxis [];
    String tonic = null;
    String mode = null;

    boolean sharp = true; //sharp by default

    public PitchAxis(char a, Pair<String, String> keySig) {
        if (a == '#')
            pitchAxis = new String[]{"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
        else if (a == 'b')
            pitchAxis = new String[]{"C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab", "A", "Bb", "B"};
    }

    public PitchAxis(Pair<String, String> keySig) { //default
        pitchAxis = new String[]{"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
        tonic = keySig.t;
        mode = keySig.u;
    }

    int getIndex(String pitch) {
        for (int i = 0; i < pitchAxis.length; i++)
            if (pitch.compareTo(pitchAxis[i]) == 0)

                return i;

        return -1; //failed
    }

    String [] getPitchAxis() {
        return pitchAxis;
    }

    //reverses a string form of the note to its' corresponding index
    int noteIndex(Note note) {
        String noteBuff = new String();
        noteBuff += note.getPitch();
        noteBuff.toUpperCase();
        switch (note.getAccidental()) {
            case 1:
                noteBuff += '#';
                break;
            case 0:
                break;
            case -1:
                noteBuff += 'b';
                sharp = false;
                break;
        }

        if (sharp) {
            switch(noteBuff) {
                case "C":
                    return 0;
                case "C#":
                    return 1;
                case "D":
                    return 2;
                case "D#":
                    return 3;
                case "E":
                    return 4;
                case "F":
                    return 5;
                case "F#":
                    return 6;
                case "G":
                    return 7;
                case "G#":
                    return 8;
                case "A":
                    return 9;
                case "A#":
                    return 10;
                case "B":
                    return 11;
            }
        } else {
            switch(noteBuff) {
                case "C":
                    return 0;
                case "Db":
                    return 1;
                case "D":
                    return 2;
                case "Eb":
                    return 3;
                case "E":
                    return 4;
                case "F":
                    return 5;
                case "Gb":
                    return 6;
                case "G":
                    return 7;
                case "Ab":
                    return 8;
                case "A":
                    return 9;
                case "Bb":
                    return 10;
                case "B":
                    return 11;
            }
        }

        return -1;
    }

    Degree degreeIndex(Note note) {
        return BlackMagicka.getDegreeIndex(tonic, mode, note.toString());

//        return degree;
    }
}