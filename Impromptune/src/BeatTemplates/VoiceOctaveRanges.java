package BeatTemplates;

/**
 * Created by Ben on 4/27/2015.
 */
public enum VoiceOctaveRanges {
    Soprano(new Integer [] {new Integer(4), new Integer(5), new Integer(6)}),
    Alto(new Integer [] {new Integer(3), new Integer(4), new Integer(5)}),
    Tenor(new Integer [] {new Integer(3), new Integer(4)}),
    Bass(new Integer [] {new Integer(1), new Integer(2)});

    VoiceOctaveRanges(Integer [] range) {
        voiceRange = range;
    }

    private Integer [] voiceRange;
}