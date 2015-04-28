package BeatTemplates;

/**
 * Created by ben on 4/27/2015.
 */
public class SopranoTemplate extends AbstractVoiceTemplate {

    public SopranoTemplate(String leadingTone, HarmonicMotion type, Rhythm beatType) {
        melodicType = type;
        rhythm = beatType;
        tones.add(leadingTone);
    }
}
