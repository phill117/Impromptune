package BeatTemplates;

/**
 * Created by ben on 4/27/2015.
 */
public enum Rhythm {
    _211(new int [] {2, 1, 1}),
    _121(new int [] {1, 2, 1}),
    _112(new int [] {1, 1, 2}),
    _1111(new int [] {1,1,1,1}),
    _wh(new int [] {1}),
    _11(new int [] {1, 1});

    Rhythm(int [] sequence) {
        beatSequence = sequence;
    }
    public int [] getBeatSequence() { return beatSequence; }
    private int [] beatSequence;

}
