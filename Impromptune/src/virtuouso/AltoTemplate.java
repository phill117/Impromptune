package virtuouso;

import java.util.List;

/**
 * Created by ben on 4/27/2015.
 */
public class AltoTemplate {
    HarmonicMotion melodicType;
    List<String> tones;
    Rhythm rhythm;

    public AltoTemplate(String dominantTone, HarmonicMotion type, Rhythm beatType) {
        melodicType = type;
        rhythm = beatType;
        tones.add(dominantTone);
    }


}
