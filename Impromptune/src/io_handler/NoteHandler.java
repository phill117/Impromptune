package io_handler;

import com.xenoage.zong.core.music.ColumnElement;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.key.TraditionalKey;
import com.xenoage.zong.core.music.rest.Rest;
import com.xenoage.zong.core.music.tuplet.Tuplet;
import com.xenoage.zong.io.selection.Cursor;
import org.jfugue.*;

/**
 * Created by ben on 2/25/15.
 */
public class NoteHandler {

    void writeKeySig(KeySignature key, Cursor cursor) {
        cursor.write((ColumnElement) new TraditionalKey(3, TraditionalKey.Mode.Major));
    }

    void writeTempo(Tempo tempo, Cursor cursor) {

    }

    void writeNote(Note note, Cursor cursor) {

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
    static void writeChord(Chord chord, Cursor cursor) {

    }

    static void writeTriplet(Tuplet tuplet, Cursor cursor) {

    }

    static void writeRest(Rest rest, Cursor cursor) {

    }
//    static String getJFugueString(Object obj) {
//        if (obj instanceof )
//            return obj.getMusicString();
//    }


//    static String extractNote();

}
