package virtuouso;

/**
 * Created by Ben on 4/27/2015.
 */
public enum PartOctaveRanges {
    Soprano(new int [] {4, 5, 6}),
    Alto(new int [] {3, 4, 5}),
    Tenor(new int [] {3, 4}),
    Bass(new int [] {1, 2});

    PartOctaveRanges(int [] range) {
        voiceRange = range;
    }

    private int [] voiceRange;
}