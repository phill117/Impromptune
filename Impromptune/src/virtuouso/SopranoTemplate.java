package virtuouso;

import java.util.List;

/**
 * Created by ben on 4/27/2015.
 */
public class SopranoTemplate {
    HarmonicMotion melodicType;
    List<String> tones;
    Rhythm rhythm;

    public SopranoTemplate(String leadingTone, HarmonicMotion type, Rhythm beatType) {
        melodicType = type;
        rhythm = beatType;
        tones.add(leadingTone);
    }
}
