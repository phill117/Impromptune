package io_handler;

import com.xenoage.utils.document.Document;
import com.xenoage.utils.document.command.Command;
import com.xenoage.utils.document.command.CommandListener;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.music.ColumnElement;
import com.xenoage.zong.core.music.Pitch;
import com.xenoage.zong.core.music.annotation.Annotation;
import com.xenoage.zong.core.music.annotation.Articulation;
import com.xenoage.zong.core.music.annotation.ArticulationType;
import com.xenoage.zong.core.music.chord.*;
import com.xenoage.zong.core.music.clef.Clef;
import com.xenoage.zong.core.music.clef.ClefType;
import com.xenoage.zong.core.music.key.TraditionalKey;
import com.xenoage.zong.core.music.rest.Rest;
import com.xenoage.zong.core.music.tuplet.Tuplet;
import com.xenoage.zong.io.selection.Cursor;
import com.xenoage.zong.musiclayout.notations.TimeNotation;
import com.xenoage.zong.core.music.time.Time;
import com.xenoage.zong.core.music.time.TimeType;

import org.jfugue.*;
import org.jfugue.Note;

import java.util.ArrayList;
import java.lang.Integer;
import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.music.Pitch.G;
import static com.xenoage.zong.core.music.Pitch.pi;

/**
 * Created by ben on 2/25/15.
 */
public class Quill implements CommandListener {

    String partName = null;

    public Quill(Cursor cursor, String instr) {
        this.cursor = cursor;
        this.partName = instr;
    }

    private boolean openBeam = false;
    private Cursor cursor = null;

    Cursor getCursor() {
        return this.cursor;
    }

    void writeKeySig(KeySignature key) {
        cursor.write((ColumnElement) new TraditionalKey(3, TraditionalKey.Mode.Major));
    }

    void writeClef(Clef clef) {
        cursor.write(clef);
    }

    void writeTempo(Tempo tempo) {
//        cursor.write(tempo);
    }

    void writeTime (String option) { //option selected
        switch(option) {
            case "2/2":
                cursor.write(new Time(TimeType.time_2_2));
                break;
            case "3/4":
                cursor.write(new Time(TimeType.time_3_4));
                break;
            case "4/4":
                cursor.write(new Time(TimeType.time_4_4));
                break;
            case "6/8":
                cursor.write(new Time(TimeType.time_6_8));
                break;
            default: //custom
                String values[] = option.split("/");
                int num = Integer.parseInt(values[0]);
                int den = Integer.parseInt(values[1]);
//                cursor.write(new Time(TimeType(num, den)));
                break;
        }
    }

    void writeNote(String note) {
        char p = note.charAt(0); //pitch
        char a = note.charAt(1); //alteration
        char r = note.charAt(2); //register
        char d = note.charAt(3); //duration

        Fraction fr = null;
        String s = new String();
        s += r;
        int o = Integer.parseInt(s);

//        System.out.println("p" + p + " " + a + " " + r + " " + o);

        switch (d) {
            case 'w':
                //check getPositionoffset
                fr = fr(1, 1);
                break;
            case 'h':
                fr = fr(1, 2);
                break;
            case 'q':
                fr = fr(1, 4);
                break;
            case 'i':
                fr = fr(1, 8);
                break;
            case 's':
                fr = fr(1, 16);
                break;
            case 't':
                fr = fr(1, 32);
                break;
            case 'x':
                fr = fr(1, 64);
                break;
            default:
                System.err.println("Invalid duration");
                return;
        }

        switch(a) {
            case '#'://sharp           //accents go in array as second arg to chord
                cursor.write(chord(fr, pi(p, 1, o)));//last arg is octave
                break;
            case 'f'://flat
                cursor.write(chord(fr, pi(p, -1, o)));
                break;
            case 'n': //natural
                cursor.write(chord(fr, pi(p, 0, o)));
                break;
            default:
                System.err.println("Invalid alteration");
                return;
        }
    }

    void startTie() {

//        cursor.openSlur();
    }

    void endTie() {
        cursor.closeSlur();
    }

    void writeRest(char r) {
        Fraction fr = null;
        switch (r) {
            case 'w':
                //check getPositionoffset
                fr = fr(1, 1);
                break;
            case 'h':
                fr = fr(1, 2);
                break;
            case 'q':
                fr = fr(1, 4);
                break;
            case 'i':
                fr = fr(1, 8);
                break;
            case 's':
                fr = fr(1, 16);
                break;
            case 't':
                fr = fr(1, 32);
                break;
            case 'x':
                fr = fr(1, 64);
                break;
            default:
                System.err.println("Invalid duration");
                return;
        }

        cursor.write(new Rest(fr));
    }

    void writeMeasure(Measure measure) {

    }

    void writePitchBend(PitchBend pitchBend) {

    }

    void writeInstrument(Instrument instr) {

    }
    /*
    Common Name      JFugue Name Intervals(0 = root)
    major           maj                 0, 4, 7
    minor           min                 0, 3, 7
    augmented       aug                 0, 4, 8
    diminished      dim                 0, 3, 6
    7th (dominant)  dom7                0, 4, 7, 10
    major 7th       maj7                0, 4, 7, 11
    minor 7th       min7                0, 3, 7, 10
    suspended 4th   sus4                0, 5, 7
    suspended 2nd   sus2                0, 2, 7
    6th (major)     maj6                0, 4, 7, 9
    minor 6th       min6                0, 3, 7, 9
    9th (dominant)  dom9                0, 4, 7, 10, 14
    major 9th       maj9                0, 4, 7, 11, 14
    minor 9th       min9                0, 3, 7, 10, 14
    diminished 7th  dim7                0, 3, 6, 9
    add9            add9                0, 4, 7, 14
    minor 11th      min11               0, 7, 10, 14, 15, 17
    11th (dominant) dom11               0, 7, 10, 14, 17
    13th (dominant) dom13               0, 7, 10, 14, 16, 21
    minor 13th      min13               0, 7, 10, 14, 15, 21
    major 13th      maj13               0, 7, 11, 14, 16, 21
    7-5 (dominant)  dom7<5              0, 4, 6, 10
    7+5 (dominant)  dom7>5              0, 4, 8, 10
    major 7-5       maj7<5              0, 4, 6, 11
    major 7+5       maj7>5              0, 4, 8, 11
    minor major 7   minmaj7             0, 3, 7, 11
    7-5-9 (dominant) dom7<5<9           0, 4, 6, 10, 13
    7-5+9 (dominant) dom7<5>9           0, 4, 6, 10, 15
    7+5-9 (dominant) dom7>5<9           0, 4, 8, 10, 13
    7+5+9 (dominant) dom7>5>9           0, 4, 8, 10, 15
    */
    void writeChord(Chord chord) {

    }

    void writeTriplet(Tuplet tuplet) {

    }

    private static Chord chord(Fraction fraction, Pitch... pitches) {
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

    @Override
    public void commandExecuted(Document document, Command command) {

    }

    @Override
    public void commandUndone(Document document, Command command) {

    }
}