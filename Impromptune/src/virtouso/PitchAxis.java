package virtouso;

/**
 * Created by ben on 4/3/2015.
 */
public class PitchAxis {
    String pitchAxis [] = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};

    int getIndex(String pitch) {
        int i = 0;

        do {
            if (pitch.compareTo(pitchAxis[i]) == 0)
                return i;
        } while (i-- > 0);

        return -1; //failed
    }
}
