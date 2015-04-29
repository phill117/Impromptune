package BeatTemplates;

import data_objects.Note;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by ben on 4/27/2015.
 */
public class BassTemplate extends AbstractVoiceTemplate {
    Rhythm [] possibleRhythms = {Rhythm._wh, Rhythm._11};

    public BassTemplate(String tonicTone, HarmonicMotion type, int repetions) {
        rand = new Random();
        rhythm = possibleRhythms[rand.nextInt(possibleRhythms.length)];
        melodicType = type;
        repeats = repetions;
        tones.add(tonicTone);
    }
}
