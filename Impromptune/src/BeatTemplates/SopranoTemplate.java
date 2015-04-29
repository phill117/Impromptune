package BeatTemplates;

import java.util.Random;

/**
 * Created by ben on 4/27/2015.
 */
public class SopranoTemplate extends AbstractVoiceTemplate {
    Rhythm [] possibleRhythms = {Rhythm._211, Rhythm._112, Rhythm._121, Rhythm._1111};

    public SopranoTemplate(String leadingTone, HarmonicMotion type, int repetions) {
        rand = new Random();
        melodicType = type;
        repeats = repetions;
        rhythm = possibleRhythms[rand.nextInt(possibleRhythms.length)];
        tones.add(leadingTone);
    }
}
