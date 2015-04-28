package BeatTemplates;

/**
 * Created by ben on 4/27/2015.
 */
public class VoiceFactory {
    private HarmonicMotion[] motionTypes = HarmonicMotion.values();
    private Rhythm[] rhythmTypes = Rhythm.values();
    private String keyTonic;
    private String mode;
    private boolean sharp;

    public VoiceFactory(String keyTonic, String mode, boolean sharp) {
        this.keyTonic = keyTonic;
        this.mode = mode;
        this.sharp = sharp;
    }

    public void buildVoice() {

    }
}