package BeatTemplates;

import java.util.Random;

/**
 * Created by ben on 4/27/2015.
 */
public class AltoTemplate extends AbstractVoiceTemplate {
    Rhythm [] possibleRhythms = {Rhythm._211, Rhythm._112, Rhythm._121, Rhythm._1111};

    public AltoTemplate(String dominantTone, HarmonicMotion type, int repetions) {
        rand = new Random();
        melodicType = type;
        rhythm = possibleRhythms[rand.nextInt(possibleRhythms.length)];
        repeats = repetions;
        tones.add(dominantTone);
    }
}
