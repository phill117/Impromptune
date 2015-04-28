package virtuouso;

import java.util.List;

/**
 * Created by ben on 4/27/2015.
 */
public class BassTemplate extends AbstractVoiceTemplate {

    public BassTemplate(String tonicTone, HarmonicMotion type, Rhythm beatType) {
        melodicType = type;
        rhythm = beatType;
        tones.add(tonicTone);
    }
}
