package BeatTemplates;

import data_objects.Beat;
import data_objects.Note;
import utils.Pair;

import java.util.Collections;
import java.util.List;

/**
 * Created by ben on 4/27/2015.
 */
public abstract class AbstractVoiceTemplate {
    HarmonicMotion melodicType;
    List<String> tones;
    Rhythm rhythm;

    public Beat buildBeat(int duration, Integer register) {
        Beat beat = new Beat();
        int numNotes = rhythm.getBeatSequence().length;
        int octave = register;

        switch(rhythm) {

            case _wh: {
                beat.addNote(new Note(stripTone(tones.get(0)), stripAccidental(tones.get(0)), octave, duration));
            }

            case _11: {
                beat.addNote(new Note(stripTone(tones.get(0)), stripAccidental(tones.get(0)), octave, duration / 2));
                beat.addNote(new Note(stripTone(tones.get(0)), stripAccidental(tones.get(0)), octave, duration / 2));
            }

            case _121: {
                int baseDuration = duration / 4;

                beat.addNote(new Note(stripTone(tones.get(0)), stripAccidental(tones.get(0)), octave, baseDuration));
                beat.addNote(new Note(stripTone(tones.get(0)), stripAccidental(tones.get(0)), octave, baseDuration * 2));
                beat.addNote(new Note(stripTone(tones.get(0)), stripAccidental(tones.get(0)), octave, baseDuration));
            }

            case _112: {
                int baseDuration = duration / 4;

                beat.addNote(new Note(stripTone(tones.get(0)), stripAccidental(tones.get(0)), octave, baseDuration));
                beat.addNote(new Note(stripTone(tones.get(0)), stripAccidental(tones.get(0)), octave, baseDuration));
                beat.addNote(new Note(stripTone(tones.get(0)), stripAccidental(tones.get(0)), octave, baseDuration * 2));
            }

            case _211: {
                int baseDuration = duration / 4;

                beat.addNote(new Note(stripTone(tones.get(0)), stripAccidental(tones.get(0)), octave, baseDuration * 2));
                beat.addNote(new Note(stripTone(tones.get(0)), stripAccidental(tones.get(0)), octave, baseDuration));
                beat.addNote(new Note(stripTone(tones.get(0)), stripAccidental(tones.get(0)), octave, baseDuration));
            }

            case _1111: {
                int baseDuration = duration / 4;
                beat.addNote(new Note(stripTone(tones.get(0)), stripAccidental(tones.get(0)), octave, baseDuration));
                beat.addNote(new Note(stripTone(tones.get(0)), stripAccidental(tones.get(0)), octave, baseDuration));
                beat.addNote(new Note(stripTone(tones.get(0)), stripAccidental(tones.get(0)), octave, baseDuration));
                beat.addNote(new Note(stripTone(tones.get(0)), stripAccidental(tones.get(0)), octave, baseDuration));
            }
        }

        if (melodicType == HarmonicMotion.Contrary && melodicType.toInt() % 2 == 1)
            Collections.reverse(beat.getNotes());

        return beat;
    }

    public char stripTone(String tone) {
        return tone.charAt(0);
    }

    public int stripAccidental(String tone) {
        if (tone.length() == 1)
            return 0;
        else if (tone.charAt(1) == 'b')
            return -1;
        else if (tone.charAt(1) == '#')
            return 1;

        return 0;
    }
}
