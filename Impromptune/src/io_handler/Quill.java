package io_handler;

import com.xenoage.utils.document.Document;
import com.xenoage.utils.document.command.Command;
import com.xenoage.utils.document.command.CommandListener;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.commands.core.music.slur.SlurAdd;
import com.xenoage.zong.core.music.ColumnElement;
import com.xenoage.zong.core.music.chord.*;
import com.xenoage.zong.core.music.direction.*;
import com.xenoage.zong.core.music.format.BezierPoint;
import com.xenoage.zong.core.music.key.TraditionalKey;
import com.xenoage.zong.core.music.rest.Rest;
import com.xenoage.zong.core.music.time.TimeSymbol;
import com.xenoage.zong.core.music.tuplet.Tuplet;
import com.xenoage.zong.io.selection.Cursor;

import com.xenoage.zong.core.music.slur.SlurType;
import com.xenoage.zong.core.music.slur.Slur;

import java.lang.Integer;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.music.Pitch.pi;
import static com.xenoage.zong.core.music.format.SP.sp;

/**
 * Created by ben on 2/25/15.
 */
public class Quill implements CommandListener {

    String partName = null;

    private boolean openBeam = false;
    private Cursor cursor = null;

    public Quill(Cursor cursor, String instr) {
        this.cursor = cursor;
        this.partName = instr;
    }

    Cursor getCursor() {
        return this.cursor;
    }
    Fraction getRemainingBeats() {
        Fraction tot = cursor.getScore().getMeasureBeats(cursor.getScore().getMeasuresCount() - 1);
        Fraction cur = cursor.getScore().getMeasureFilledBeats(cursor.getScore().getMeasuresCount() - 1);
        return tot.sub(cur);
    }
//    void writeKeySig(KeySignature key) {
//        cursor.write((ColumnElement) new TraditionalKey(3, TraditionalKey.Mode.Major));
//    }

    //format of string should be "bass" || "treble" || "tenor" || "alto"
    void writeClef(String clef) {
        cursor.write(QuillUtils.getClef(clef));
    }

    void writeTempo(String timeSig, int bpm) {
        cursor.write((ColumnElement) new Tempo(QuillUtils.getFraction(timeSig), bpm));
    }

    void writeTime (String timeSig) { //time option selected, arguments should be in following format: "4/4"
        cursor.write(QuillUtils.getTime(timeSig));
    }

    boolean checkMeasure(Fraction f) {
        Fraction tot = cursor.getScore().getMeasureBeats(cursor.getScore().getMeasuresCount() - 1);
        Fraction cur = cursor.getScore().getMeasureFilledBeats(cursor.getScore().getMeasuresCount() - 1);
        Fraction rem = tot.sub(cur);

        System.err.println("beats remaining in measure: " + rem);
        System.err.println("duration to be inserted: " + f);

        if (rem.compareTo(f) >= 0)
            return true;

        return false;
    }


    Fraction chomp(Fraction fr, Fraction rem, char p, char a, char o) {
        int num = fr.getNumerator();
//        cursor.write(chord(fr, pi(p, 1, o)));//last arg is octave
        if (num != 1) {//fraction is > 1/2...chomp half of it
            Fraction chunk =  fr(1, fr.getDenominator());
            switch(a) {
                case '#'://sharp           //accents go in array as second arg to chord
                    cursor.write(QuillUtils.chord(chunk, pi(p, 1, o)));//last arg is octave
                    break;
                case 'f'://flat
                    cursor.write(QuillUtils.chord(chunk, pi(p, -1, o)));
                    break;
                case 'n': //natural
                    cursor.write(QuillUtils.chord(chunk, pi(p, 0, o)));
                    break;
                default:
                    System.err.println("Invalid alteration");
                    return null;
            }
        }
        return    fr(--num, fr.getDenominator());
    }

    void writeNote(String note) {
        //Score.getMeasureBeats()
        char p = note.charAt(0); //pitch
        char a = note.charAt(1); //alteration
        char r = note.charAt(2); //register
        char d = note.charAt(3); //duration

        Fraction fr =  QuillUtils.getFraction(d);
        String s = new String();
        s += r;
        int o = Integer.parseInt(s) - 1;
//        o--;
        //accents go in array as second arg to chord
//        cursor.write(chord(fr, getPitch(p, a , o)));
//        System.out.println("p" + p + " " + a + " " + r + " " + o);

        System.out.println(checkMeasure(fr));
        Fraction tot = cursor.getScore().getMeasureBeats(cursor.getScore().getMeasuresCount() - 1);
        Fraction cur = cursor.getScore().getMeasureFilledBeats(cursor.getScore().getMeasuresCount() - 1);
        Fraction rem = tot.sub(cur);

        if (rem.isGreater0() && rem.compareTo(fr) < 0) {
            Chord attachC, firstSlurC, lastSlurC;
            BezierPoint firstSlurB, lastSlurB;
//            write Chord with rem and fr - rem
            System.out.println("fr: "  + fr + " rem: " + rem + " fr-rem: " + fr.sub(rem));
//            startSlur();
            cursor.write(firstSlurC = QuillUtils.chord(rem, QuillUtils.getPitch(p, a, o)));
            cursor.write(lastSlurC = QuillUtils.chord(fr.sub(rem), QuillUtils.getPitch(p, a, o)));
//            closeSlur();
            float is = getCursor().getScore().getFormat().getInterlineSpace();
            firstSlurB = new BezierPoint(sp(is * 0.8f, is * 7.6f), sp(is, is * 0.8f));
            lastSlurB = new BezierPoint(sp(0, is * 6f), sp(-is, is));
            new SlurAdd(new Slur(SlurType.Tie, QuillUtils.clwp(firstSlurC, firstSlurB), QuillUtils.clwp(lastSlurC, lastSlurB), null)).execute();
        } else {
            cursor.write(QuillUtils.chord(fr, QuillUtils.getPitch(p, a, o)));
        }
    }

    void startTie() {
        cursor.openSlur(SlurType.Tie);
    }

    void endTie() {
        cursor.closeSlur();
    }

    void startSlur() {
        cursor.openSlur(SlurType.Slur);
    }

    void closeSlur(){
        cursor.closeSlur();
    }

    void openBeam() {
        openBeam = true;
        cursor.openBeam();
    }

    void closeBeam() {
        openBeam = false;
        cursor.closeBeam();
    }

    void writeRest(char r) {
        closeBeam();
        cursor.write(new Rest(QuillUtils.getFraction(r)));
    }

//    void writePitchBend(PitchBend pitchBend) {
//
//    }
//
//    void writeInstrument(Instrument instr) {
//
//    }
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

    @Override
    public void commandExecuted(Document document, Command command) {

    }

    @Override
    public void commandUndone(Document document, Command command) {

    }
}