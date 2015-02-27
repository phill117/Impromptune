package io_handler;

import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.music.ColumnElement;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.clef.Clef;
import com.xenoage.zong.core.music.clef.ClefType;
import com.xenoage.zong.core.music.key.TraditionalKey;
import com.xenoage.zong.core.music.tuplet.Tuplet;
import com.xenoage.zong.io.selection.Cursor;
import org.jfugue.*;

import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.music.Pitch.G;
import static com.xenoage.zong.core.music.Pitch.pi;

/**
 * Created by ben on 2/25/15.
 */
public class NoteHandler {
    private Fraction f1 = fr(1, 1);
    private Fraction f2 = fr(1, 2);
    private Fraction f4 = fr(1, 4);
    private Fraction f8 = fr(1, 8);
    private Fraction f16 = fr(1, 16);
    private Fraction f32 = fr(1, 32);
    private Fraction f64 = fr(1, 64);

    void writeKeySig(KeySignature key, Cursor cursor) {
        cursor.write((ColumnElement) new TraditionalKey(3, TraditionalKey.Mode.Major));
    }

    void writeClef(Clef clef, Cursor cursor) {
        cursor.write(clef);
    }

    void writeTempo(Tempo tempo, Cursor cursor) {
//        cursor.write(tempo);
    }

    void writeNote(Note note, Cursor cursor) {
        String mus = note.getMusicString();

        char p = mus.charAt(0);
        char a = mus.charAt(1);
        char d = mus.charAt(2);

        Fraction fr = null;

        if (p == 'R')
            writeRest(note, cursor);

        switch (d) {
            case 'w':
                fr = f1;
                break;
            case 'h':
                fr = f2;
                break;
            case 'q':
                fr = f4;
                break;
            case 'i':
                fr = f8;
                break;
            case 's':
                fr = f16;
                break;
            case 't':
                fr = f32;
                break;
            case 'x':
                fr = f64;
                break;
        }

        switch(a) {
            case 's'://sharp/''
                cursor.write(chord(fr, pi(p - 65, 1, 1)));
                break;
            case 'f'://flat
                cursor.write(chord(fr, pi(p - 65, -1, 1)));
                break;
            case 'n': //natural
                cursor.write(chord(fr, pi(p - 65, 0, 1)));
                break;
        }
    }

    void writeMeasure(Measure measure, Cursor cursor) {

    }

    void writePitchBend(PitchBend pitchBend, Cursor cursor) {

    }

    void writeInstrument(Instrument instr, Cursor cursor) {

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
    void writeChord(Chord chord, Cursor cursor) {

    }

    void writeTriplet(Tuplet tuplet, Cursor cursor) {

    }
}
