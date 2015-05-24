package BeatTemplates;

import virtuouso.BlackMagicka;

import java.util.Collections;
import java.util.List;

/**
 * Created by ben on 4/27/2015.
 */
public class Arpeggios {
    //these should take into account what degree of the key the root of the chord is to choose major or minor
    public static List<String> arpeggiate135(String root, boolean sharp) {
        List<String> chord = BlackMagicka.majorChord(root, sharp);
        return chord;
    }

    public static List<String> arpeggiate153(String root, boolean sharp) {
        List<String> chord = BlackMagicka.majorChord(root, sharp);
        Collections.swap(chord, 2, 3);
        return chord;
    }

    public static List<String> arpeggiate531(String root, boolean sharp) {
        List<String> chord = BlackMagicka.majorChord(root, sharp);
        Collections.reverse(chord);
        return chord;
    }

    public static List<String> arpeggiate513(String root, boolean sharp) {
        List<String> chord = BlackMagicka.majorChord(root, sharp);
        Collections.swap(chord, 1,3);
        Collections.swap(chord, 2,3);
        return chord;
    }

    public static List<String> arpeggiate1357(String root, boolean sharp) {
        return BlackMagicka.major7thChord(root, sharp);
    }

    public static List<String> arpeggiate7531(String root, boolean sharp) {
        List<String> chord = BlackMagicka.major7thChord(root, sharp);
        Collections.reverse(chord);
        return chord;
    }
}
