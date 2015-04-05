package virtuouso;

/**
 * Created by ben on 4/3/2015.
 */
public class PitchAxis {
    String pitchAxis [] = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};

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
