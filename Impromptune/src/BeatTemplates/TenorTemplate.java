package BeatTemplates;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by ben on 4/27/2015.
 */
public class TenorTemplate extends AbstractVoiceTemplate {
    Rhythm [] possibleRhythms = {Rhythm._wh, Rhythm._11};

    public TenorTemplate(String leadingTone, HarmonicMotion type, int repetions) {
        rand = new Random();
        rhythm = possibleRhythms[rand.nextInt(possibleRhythms.length)];
        melodicType = type;
        repeats = repetions;
        tones = new ArrayList<>();
        tones.add(leadingTone);
        voiceType = AxisMundi.Tenor;
    }
}
