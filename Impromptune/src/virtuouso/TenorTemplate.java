package virtuouso;

import java.util.List;

/**
 * Created by ben on 4/27/2015.
 */
public class TenorTemplate {
    HarmonicMotion melodicType;
    List<String> tones;
    Rhythm rhythm;

    public TenorTemplate(String leadingTone, HarmonicMotion type, Rhythm beatType) {
        melodicType = type;
        rhythm = beatType;
        tones.add(leadingTone);
    }
}
