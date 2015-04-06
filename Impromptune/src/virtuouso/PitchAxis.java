package virtuouso;

/**
 * Created by ben on 4/3/2015.
 */

//the notes plotted in markovian transition tables
public class PitchAxis {

    String pitchAxis [];

    public PitchAxis(char a) {
        if (a == 'b')
            pitchAxis = new String[]{"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
        else if (a == '#') {
            pitchAxis = new String[]{"C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab", "A", "Bb", "B"};
        }
    }

    public PitchAxis() { //default
        pitchAxis = new String[]{"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
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
}
