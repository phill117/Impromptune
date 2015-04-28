package BeatTemplates;

import data_objects.Beat;

import java.util.List;

/**
 * Created by ben on 4/27/2015.
 */
public abstract class AbstractVoiceTemplate {
    HarmonicMotion melodicType;
    List<String> tones;
    Rhythm rhythm;

    public Beat buildBeat(int duration) {


        return null;
    }
}
