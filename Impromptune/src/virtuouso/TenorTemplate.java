package virtuouso;

import java.util.List;

/**
 * Created by ben on 4/27/2015.
 */
public class TenorTemplate extends AbstractVoiceTemplate {

    public TenorTemplate(String leadingTone, HarmonicMotion type, Rhythm beatType) {
        melodicType = type;
        rhythm = beatType;
        tones.add(leadingTone);
    }
}
