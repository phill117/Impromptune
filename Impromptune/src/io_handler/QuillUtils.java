package io_handler;

import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.music.Pitch;
import com.xenoage.zong.core.music.annotation.Annotation;
import com.xenoage.zong.core.music.annotation.Articulation;
import com.xenoage.zong.core.music.annotation.ArticulationType;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.clef.Clef;
import com.xenoage.zong.core.music.clef.ClefType;
import com.xenoage.zong.core.music.format.BezierPoint;
import com.xenoage.zong.core.music.key.Key;
import com.xenoage.zong.core.music.key.TraditionalKey;
import com.xenoage.zong.core.music.key.TraditionalKey.Mode;
import com.xenoage.zong.core.music.slur.SlurWaypoint;
import com.xenoage.zong.core.music.time.Time;
import com.xenoage.zong.core.music.time.TimeType;
import piano.PianoHolder;

import java.util.ArrayList;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.music.Pitch.pi;

/**
 * Created by ben on 3/16/2015.
 */
public class QuillUtils {
    static Fraction getFraction(String fraction) {
        switch (fraction) {
            case "1/1":
                return fr(1,1);

            case "1/2":
                return fr(1,2);

            case "1/4":
                return fr(1,4);

            case "1/8":
                return fr(1,8);

            case "1/16":
                return fr(1,16);

            case "1/32":
                return fr(1,32);

            case "1/64":
                return fr(1,64);
        }

        System.err.println("Invalid duration: " + fraction);
        return null;
    }

    //return fraction based on char input
    static Fraction getFraction(char d) {
        if (PianoHolder.getDotted()) {
            return getDottedFraction(d);
        } else {
            switch (d) {
                case 'w':
                    return fr(1, 1);

                case 'h':
                    return fr(1, 2);

                case 'q':
                    return fr(1, 4);

                case 'i':
                    return fr(1, 8);

                case 's':
                    return fr(1, 16);

                case 't':
                    return fr(1, 32);

                case 'x':
                    return fr(1, 64);
            }
        }

        System.err.println("Invalid duration: " + d);
        return null;
    }

    static Fraction getDottedFraction(char d) {
        switch (d) {
            case 'w':
                return fr(1, 1).add(fr(1,2));

            case 'h':
                return fr(1, 2).add(fr(1,4));

            case 'q':
                return fr(1, 4).add(fr(1,8));

            case 'i':
                return fr(1, 8).add(fr(1,16));

            case 's':
                return fr(1, 16).add(fr(1,32));

            case 't':
                return fr(1, 32).add(fr(1, 64));

            case 'x':
                return fr(1, 64).add(fr(1,128));

            default:
                return null;
        }
    }

    static Pitch getPitch(char p, char a, int o) {

        switch(a) {
            case '#'://sharp
                return pi(p, 1, o);
            case 'f'://flat
                return pi(p, -1, o);
            case 'n': //natural
                return pi(p, 0, o);
        }
        System.err.println("Invalid alteration: " + p);
        return null;
    }

    static int[][] fifthTable =
            // natural, flat, sharp
            {{-1,-8,6},//f
            {0,-7,7},//c
            {1,-6,8},//g
            {2,-5,9},//d
            {3,-4,10},//a
            {4,-3,11},//e
            {5,-2,12},//b
            };

    static int getFifth(String keyRoot, String mode, String keyMod) {
        int base;
        int mod = 0;
        switch(keyRoot) {
            default:
            case "C":
                base = 1;
                break;
            case "F":
                base = 0;
                break;
            case "G":
                base = 2;
                break;
            case "D":
                base = 3;
                break;
            case "A":
                base = 4;
                break;
            case "E":
                base = 5;
                break;
            case "B":
                base = 6;
                break;
        }

        if(keyMod.equals("♭"))mod = 1;
        if(keyMod.equals("♯"))mod = 2;

        int fifths = fifthTable[base][mod];
        if(mode.equals("minor")) fifths -=3;

        return fifths;
    }

    public static Key getKeySig(String keyRoot, String mode, String keyMod) {
        Mode m = null;
        int fifth = 0;
        mode = mode.toLowerCase();

        switch (mode) {
            default:
            case "major":
                m = Mode.Major;
                break;
            case "minor":
                m = Mode.Minor;
                break;
            case "ionian":
                m = Mode.Ionian;
                break;
            case "dorian":
                m = Mode.Dorian;
                break;
            case "phrygian":
                m = Mode.Phrygian;
                break;
            case "lydian":
                m = Mode.Lydian;
                break;
            case "mixolydian":
                m = Mode.Mixolydian;
                break;
            case "aeolian":
                m = Mode.Aeolian;
                break;
            case "locrian":
                m = Mode.Locrian;
                break;
        }

        return new TraditionalKey(getFifth(keyRoot,mode,keyMod), m);
    }

    public static Clef getClef(String clef) {
        switch (clef) {
            case "bass":
                return new Clef(ClefType.clefBass);
            case "treble":
                return new Clef(ClefType.clefTreble);
            case "alto":
                return new Clef(ClefType.clefAlto);
            case "tenor":
                return new Clef(ClefType.clefTenor);
            default:
                return null;
        }
    }

    public static Time getTime(String timeSig) {
        switch(timeSig) {
            case "2/2":
                return new Time(TimeType.time_2_2);
            case "3/4":
                return new Time(TimeType.time_3_4);
            case "4/4":
                return new Time(TimeType.time_4_4);
            case "6/8":
                return new Time(TimeType.time_6_8);
            default: //custom
                String values[] = timeSig.split("/");
                int num = Integer.parseInt(values[0]);
                int den = Integer.parseInt(values[1]);
                return new Time(TimeType.timeType(num, den));
        }
    }

    public static Chord chord(Fraction fraction, Pitch... pitches) {
        return chord(fraction, null, pitches);
    }

    private static Chord chord(Fraction fraction, ArticulationType[] articulations, Pitch... pitches) {
        Chord chord = new Chord(com.xenoage.zong.core.music.chord.Note.notes(pitches), fraction);

        if (articulations != null) {
            ArrayList<Annotation> a = alist(articulations.length);
            for (ArticulationType at : articulations)
                a.add(new Articulation(at));
            chord.setAnnotations(a);
        }

        return chord;
    }

    public static SlurWaypoint clwp(Chord c, BezierPoint bezierPoint) {
        return new SlurWaypoint(c, null, bezierPoint);
    }

    class Beamer{
        private Fraction counter = null;
        public Beamer(Fraction initialNote) {
            counter = initialNote.mult(fr(4 * counter.getNumerator(), counter.getDenominator()));
        }
    }
}
