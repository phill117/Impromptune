package BeatTemplates;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by ben on 4/27/2015.
 */
public class SopranoTemplate extends AbstractVoiceTemplate {
    Rhythm [] possibleRhythms = {Rhythm._211, Rhythm._112, Rhythm._121, Rhythm._1111};
    String keyTonic;
    String mode;

    public SopranoTemplate(String leadingTone, HarmonicMotion type, int repetions, String keyTonic, String mode) {
        this.rand = new Random();
        this.rhythm = possibleRhythms[rand.nextInt(possibleRhythms.length)];
        this.melodicType = type;
        this.repeats = repetions;
        this.tones = new ArrayList<>();
        this.tones.add(leadingTone);
        this.keyTonic = keyTonic;
        this.mode = mode;
    }
}
