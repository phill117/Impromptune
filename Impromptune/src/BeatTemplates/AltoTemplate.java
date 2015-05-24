package BeatTemplates;

import virtuouso.BlackMagicka;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by ben on 4/27/2015.
 */
public class AltoTemplate extends AbstractVoiceTemplate {
    Rhythm [] possibleRhythms = {Rhythm._211, Rhythm._112, Rhythm._121, Rhythm._1111};

    public AltoTemplate(String dominantTone, HarmonicMotion type, int repetitions, String keyTonic, String mode, boolean sharp) {
        rand = new Random();
        rhythm = possibleRhythms[rand.nextInt(possibleRhythms.length)];
        melodicType = type;
        repeats = repetitions;


        if (BlackMagicka.noteInScale(dominantTone, keyTonic, mode, sharp)) {
            int val = BlackMagicka.getDegreeIndex(keyTonic, mode, dominantTone, sharp).toInt();
            switch(mode) {
                case "major":
                    if (val == 0 || val == 4 || val == 5) {
                        this.tones = BlackMagicka.majorChord(dominantTone, sharp);
                    } else {
                        this.tones = BlackMagicka.minorChord(dominantTone, sharp);
                    }
                    break;
                case "minor":
                    if (val == 2 || val == 5 || val == 6) {
                        this.tones = BlackMagicka.majorChord(dominantTone, sharp);
                    } else {
                        this.tones = BlackMagicka.minorChord(dominantTone, sharp);
                    }
            }

        }

        if (this.tones == null) {
            tones = new ArrayList<>();
            tones.add(dominantTone);
        }

        voiceType = AxisMundi.Alto;
    }
}
