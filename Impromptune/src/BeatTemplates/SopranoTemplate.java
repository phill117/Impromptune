package BeatTemplates;

import virtuouso.BlackMagicka;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by ben on 4/27/2015.
 */
public class SopranoTemplate extends AbstractVoiceTemplate {
    Rhythm [] possibleRhythms = {Rhythm._211, Rhythm._112, Rhythm._121, Rhythm._1111};
    String keyTonic;
    String mode;

    public SopranoTemplate(String leadingTone, HarmonicMotion type, int repetitions, String keyTonic, String mode, boolean sharp) {
        this.rand = new Random();
        this.rhythm = possibleRhythms[rand.nextInt(possibleRhythms.length)];
        this.melodicType = type;
        this.repeats = repetitions;
//        this.tones = new ArrayList<>();
//        this.tones.add(leadingTone);
        this.keyTonic = keyTonic;
        this.mode = mode;
        if (BlackMagicka.noteInScale(leadingTone, keyTonic, mode, sharp)) {
            int val = BlackMagicka.getDegreeIndex(keyTonic, mode, leadingTone, sharp).toInt();
            switch(mode) {
                case "major":
                    if (val == 0 || val == 4 || val == 5) {
                        this.tones = BlackMagicka.majorChord(leadingTone, sharp);
                    } else {
                        this.tones = BlackMagicka.minorChord(leadingTone, sharp);
                    }
                    break;
                case "minor":
                    if (val == 2 || val == 5 || val == 6) {
                        this.tones = BlackMagicka.majorChord(leadingTone, sharp);
                    } else {
                        this.tones = BlackMagicka.minorChord(leadingTone, sharp);
                    }
            }

        }

        if (this.tones == null) {
            tones = new ArrayList<>();
            tones.add(leadingTone);
        }
        voiceType = AxisMundi.Soprano;
    }
}
