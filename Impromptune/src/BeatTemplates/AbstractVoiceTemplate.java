package BeatTemplates;

import data_objects.Beat;
import data_objects.Note;
import virtuouso.BlackMagicka;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by ben on 4/27/2015.
 */
public abstract class AbstractVoiceTemplate {
    HarmonicMotion melodicType;
    List<String> tones;
    Rhythm rhythm;
    int repeats;
    Random rand;

    AxisMundi voiceType;

    public Beat buildBeat(int duration, Integer register) {
        Beat beat = new Beat();
        int numNotes = rhythm.getBeatSequence().length;
        int octave = register;
        duration *= 4;
//        if (duration == 1)
//            duration *= 32;
//        else if (duration == 2)
//            duration *= 16;
//        else if (duration == 4)
//            duration *= 8;
//        else if (duration == 8)
//            duration *= 4;
//        else if (duration == 16)
//            duration *= 2;
//        System.out.println(octave);
        switch(rhythm) {

            case _wh: {
                beat.addNote(new Note(stripTone(tones.get(0)), stripAccidental(tones.get(0)), octave, duration));
            }

            case _11: {
                int baseDuration = duration / 2;
                beat.addNote(new Note(stripTone(tones.get(0)), stripAccidental(tones.get(0)), octave, baseDuration));
                beat.addNote(new Note(stripTone(tones.get(0)), stripAccidental(tones.get(0)), octave, baseDuration));
//                beat.addNote(new Note(stripTone(tones.get(0)), stripAccidental(tones.get(0)), octave, duration));
            }

            case _121: {
                int baseDuration = duration / 4;
//                int baseDuration = duration;
                if (tones.size() > 2 && (voiceType == AxisMundi.Soprano || voiceType == AxisMundi.Alto)) {
                    beat.addNote(new Note(stripTone(tones.get(0)), stripAccidental(tones.get(0)), octave, baseDuration));
                    beat.addNote(new Note(stripTone(tones.get(1)), stripAccidental(tones.get(1)), octave, baseDuration * 2));
                    beat.addNote(new Note(stripTone(tones.get(2)), stripAccidental(tones.get(2)), octave, baseDuration));
                } else {
                    beat.addNote(new Note(stripTone(tones.get(0)), stripAccidental(tones.get(0)), octave, baseDuration));
                    beat.addNote(new Note(stripTone(tones.get(0)), stripAccidental(tones.get(0)), octave, baseDuration * 2));
                    beat.addNote(new Note(stripTone(tones.get(0)), stripAccidental(tones.get(0)), octave, baseDuration));
                }

            }

            case _112: {
                int baseDuration = duration / 4;
//                int baseDuration = duration;
                if (tones.size() > 2 && (voiceType == AxisMundi.Soprano || voiceType == AxisMundi.Alto)) {
                    beat.addNote(new Note(stripTone(tones.get(0)), stripAccidental(tones.get(0)), octave, baseDuration));
                    beat.addNote(new Note(stripTone(tones.get(1)), stripAccidental(tones.get(1)), octave, baseDuration));
                    beat.addNote(new Note(stripTone(tones.get(2)), stripAccidental(tones.get(2)), octave, baseDuration * 2));
                } else {
                    beat.addNote(new Note(stripTone(tones.get(0)), stripAccidental(tones.get(0)), octave, baseDuration));
                    beat.addNote(new Note(stripTone(tones.get(0)), stripAccidental(tones.get(0)), octave, baseDuration));
                    beat.addNote(new Note(stripTone(tones.get(0)), stripAccidental(tones.get(0)), octave, baseDuration * 2));
                }
            }

            case _211: {
                int baseDuration = duration / 4;
//                int baseDuration = duration;
                if (tones.size() > 2 && voiceType == AxisMundi.Soprano) {
                    beat.addNote(new Note(stripTone(tones.get(0)), stripAccidental(tones.get(0)), octave, baseDuration * 2));
                    beat.addNote(new Note(stripTone(tones.get(1)), stripAccidental(tones.get(1)), octave, baseDuration));
                    beat.addNote(new Note(stripTone(tones.get(2)), stripAccidental(tones.get(2)), octave, baseDuration));
                } else {
                    beat.addNote(new Note(stripTone(tones.get(0)), stripAccidental(tones.get(0)), octave, baseDuration * 2));
                    beat.addNote(new Note(stripTone(tones.get(0)), stripAccidental(tones.get(0)), octave, baseDuration));
                    beat.addNote(new Note(stripTone(tones.get(0)), stripAccidental(tones.get(0)), octave, baseDuration));
                }
            }

            case _1111: {
//                int baseDuration = duration;
                Random rand = new Random();

                int baseDuration = duration / 8;
                switch (rand.nextInt(4)) {
                    case 0:
                        if (tones.size() == 1) {
                            beat.addNote(new Note(stripTone(tones.get(0)), stripAccidental(tones.get(0)), octave, baseDuration));
                            beat.addNote(new Note(stripTone(tones.get(0)), stripAccidental(tones.get(0)), octave, baseDuration * 3));
                            beat.addNote(new Note(stripTone(tones.get(0)), stripAccidental(tones.get(0)), octave, baseDuration));
                            beat.addNote(new Note(stripTone(tones.get(0)), stripAccidental(tones.get(0)), octave, baseDuration * 3));
                        } else {
                            beat.addNote(new Note(stripTone(tones.get(0)), stripAccidental(tones.get(0)), octave, baseDuration * 3));
                            beat.addNote(new Note(stripTone(tones.get(1)), stripAccidental(tones.get(1)), octave, baseDuration));
                            beat.addNote(new Note(stripTone(tones.get(2)), stripAccidental(tones.get(2)), octave, baseDuration * 3));
                            beat.addNote(new Note(stripTone(tones.get(1)), stripAccidental(tones.get(1)), octave, baseDuration));
                        }
                        break;
                    case 1:
                        if (tones.size() == 1) {
                            beat.addNote(new Note(stripTone(tones.get(0)), stripAccidental(tones.get(0)), octave, baseDuration));
                            beat.addNote(new Note(stripTone(tones.get(0)), stripAccidental(tones.get(0)), octave, baseDuration * 3));
                            beat.addNote(new Note(stripTone(tones.get(0)), stripAccidental(tones.get(0)), octave, baseDuration));
                            beat.addNote(new Note(stripTone(tones.get(0)), stripAccidental(tones.get(0)), octave, baseDuration));
                            beat.addNote(new Note(stripTone(tones.get(0)), stripAccidental(tones.get(0)), octave, baseDuration));
                            beat.addNote(new Note(stripTone(tones.get(0)), stripAccidental(tones.get(0)), octave, baseDuration));
                        } else {
                            beat.addNote(new Note(stripTone(tones.get(0)), stripAccidental(tones.get(0)), octave, baseDuration * 3));
                            beat.addNote(new Note(stripTone(tones.get(1)), stripAccidental(tones.get(1)), octave, baseDuration));
                            beat.addNote(new Note(stripTone(tones.get(2)), stripAccidental(tones.get(2)), octave, baseDuration));
                            beat.addNote(new Note(stripTone(tones.get(1)), stripAccidental(tones.get(1)), octave, baseDuration));
                            beat.addNote(new Note(stripTone(tones.get(1)), stripAccidental(tones.get(1)), octave, baseDuration));
                            beat.addNote(new Note(stripTone(tones.get(2)), stripAccidental(tones.get(2)), octave, baseDuration));
                        }
                        break;
                    case 2:
                        if (tones.size() == 1) {
                            beat.addNote(new Note(stripTone(tones.get(0)), stripAccidental(tones.get(0)), octave, baseDuration));
                            beat.addNote(new Note(stripTone(tones.get(0)), stripAccidental(tones.get(0)), octave, baseDuration));
                            beat.addNote(new Note(stripTone(tones.get(0)), stripAccidental(tones.get(0)), octave, baseDuration));
                            beat.addNote(new Note(stripTone(tones.get(0)), stripAccidental(tones.get(0)), octave, baseDuration));
                            beat.addNote(new Note(stripTone(tones.get(0)), stripAccidental(tones.get(0)), octave, baseDuration));
                            beat.addNote(new Note(stripTone(tones.get(0)), stripAccidental(tones.get(0)), octave, baseDuration));
                            beat.addNote(new Note(stripTone(tones.get(0)), stripAccidental(tones.get(0)), octave, baseDuration));
                            beat.addNote(new Note(stripTone(tones.get(0)), stripAccidental(tones.get(0)), octave, baseDuration));
                        } else {
                            beat.addNote(new Note(stripTone(tones.get(0)), stripAccidental(tones.get(0)), octave, baseDuration));
                            beat.addNote(new Note(stripTone(tones.get(0)), stripAccidental(tones.get(0)), octave, baseDuration));
                            beat.addNote(new Note(stripTone(tones.get(0)), stripAccidental(tones.get(0)), octave, baseDuration));
                            beat.addNote(new Note(stripTone(tones.get(0)), stripAccidental(tones.get(0)), octave, baseDuration));
                            beat.addNote(new Note(stripTone(tones.get(2)), stripAccidental(tones.get(2)), octave, baseDuration));
                            beat.addNote(new Note(stripTone(tones.get(1)), stripAccidental(tones.get(1)), octave, baseDuration));
                            beat.addNote(new Note(stripTone(tones.get(1)), stripAccidental(tones.get(1)), octave, baseDuration));
                            beat.addNote(new Note(stripTone(tones.get(2)), stripAccidental(tones.get(2)), octave, baseDuration));
                        }
                        break;
                    case 3:
                        if (tones.size() == 1) {
                            beat.addNote(new Note(stripTone(tones.get(0)), stripAccidental(tones.get(0)), octave, baseDuration * 2));
                            beat.addNote(new Note(stripTone(tones.get(0)), stripAccidental(tones.get(0)), octave, baseDuration * 2));
                            beat.addNote(new Note(stripTone(tones.get(0)), stripAccidental(tones.get(0)), octave, baseDuration * 2));
                            beat.addNote(new Note(stripTone(tones.get(0)), stripAccidental(tones.get(0)), octave, baseDuration * 2));
                        } else {
                            beat.addNote(new Note(stripTone(tones.get(0)), stripAccidental(tones.get(0)), octave, baseDuration));
                            beat.addNote(new Note(stripTone(tones.get(2)), stripAccidental(tones.get(2)), octave, baseDuration * 2));
                            beat.addNote(new Note(stripTone(tones.get(2)), stripAccidental(tones.get(2)), octave, baseDuration));
                            beat.addNote(new Note(stripTone(tones.get(1)), stripAccidental(tones.get(1)), octave, baseDuration));
                            beat.addNote(new Note(stripTone(tones.get(1)), stripAccidental(tones.get(1)), octave, baseDuration));
                            beat.addNote(new Note(stripTone(tones.get(2)), stripAccidental(tones.get(2)), octave, baseDuration * 2));
                        }
                }
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
