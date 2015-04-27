package io_handler;

import com.xenoage.utils.document.Document;
import com.xenoage.utils.document.command.Command;
import com.xenoage.utils.document.command.CommandListener;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.commands.core.music.PartAdd;
import com.xenoage.zong.commands.core.music.slur.SlurAdd;
import com.xenoage.zong.core.instrument.Instrument;
import com.xenoage.zong.core.music.ColumnElement;
import com.xenoage.zong.core.music.MeasureElement;
import com.xenoage.zong.core.music.Part;
import com.xenoage.zong.core.music.chord.*;
import com.xenoage.zong.core.music.direction.*;
import com.xenoage.zong.core.music.format.BezierPoint;
import com.xenoage.zong.core.music.format.Position;
import com.xenoage.zong.core.music.rest.Rest;
import com.xenoage.zong.core.music.slur.SlurWaypoint;
import com.xenoage.zong.core.music.time.Time;
import com.xenoage.zong.io.selection.Cursor;

import com.xenoage.zong.core.music.slur.SlurType;
import com.xenoage.zong.core.music.slur.Slur;
import piano.PianoHolder;

import java.io.Serializable;
import java.lang.Integer;
import java.util.ArrayList;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.music.Pitch.pi;
import static com.xenoage.zong.core.music.format.SP.sp;
import static com.xenoage.zong.core.text.UnformattedText.ut;

/**
 * Created by ben on 2/25/15.
 */
public class Quill implements CommandListener, Serializable {

    String partName = null;
    private Chord firstSlurC, lastSlurC;
    private boolean openBeam = false;
    private Fraction beamCounter = null;
    private Fraction theBeat = null;

    private boolean tStart = false;
    private boolean sChord = false;
    private boolean eChord = false;
    private boolean tEnd = false;
    private int slurWP = 0;
    private Cursor cursor = null;
    private float is = 0;
    private BezierPoint startBp = null;
    private BezierPoint endBp = null;
    private ArrayList<SlurWaypoint> openSlurWaypoints = null;
    private Tempo tempo = null;

    public Quill(Cursor cursor, String instr) {
        this.cursor = cursor;
        this.partName = instr;
        this.is = cursor.getScore().getFormat().getInterlineSpace();
        this.startBp =  new BezierPoint(sp(is * 0.8f, is * 7.6f), sp(is, is * 0.8f));
        this.endBp = new BezierPoint(sp(0, is * 6f), sp(-is, is));
        this.openSlurWaypoints = new ArrayList<>();
    }

    public Cursor getCursor() {
        return this.cursor;
    }

    //starting at this measure
    void writeMeasureKeySig(String keySig, String mode, String keyMod) {
        cursor.write((MeasureElement) QuillUtils.getKeySig(keySig, mode, keyMod));
    }

    //for the entire staff, ie composition initialization
    void writeStaffKeySig(String keySig, String mode, String keyMod) {
        cursor.write((ColumnElement) QuillUtils.getKeySig(keySig, mode, keyMod));
    }

    //format of string should be "bass" || "treble" || "tenor" || "alto"
    void writeClef(String clef) {
        cursor.write(QuillUtils.getClef(clef));
    }

    void writeTempo(String timeSig, int bpm) {

        String values[] = timeSig.split("/");
        int num = Integer.parseInt(values[0]);
        int den = Integer.parseInt(values[1]);

        Fraction fr = Fraction.fr(num, den);
        Tempo tempo = new Tempo(fr, bpm);
        tempo.setText(ut(getTempoName(bpm)));
        tempo.setPositioning(new Position(null, 22f, -5f, -5f));
        this.tempo = tempo;
        cursor.write((ColumnElement) tempo);
    }

    void writeTime (String timeSig) { //time option selected, arguments should be in following format: "4/4"
        Time time =  QuillUtils.getTime(timeSig);
        theBeat = fr(1, time.getType().getDenominator());
        cursor.write(time);
        System.out.println("theBeat: " + theBeat);
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

    Fraction getRemainingBeats() {
        Fraction tot = cursor.getScore().getMeasureBeats(cursor.getScore().getMeasuresCount() - 1);
        Fraction cur = cursor.getScore().getMeasureFilledBeats(cursor.getScore().getMeasuresCount() - 1);
        return tot.sub(cur);
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


        if (PianoHolder.getTie()) {  //Set start TIE
            tStart = true;
        }

        if ( (tStart) && !PianoHolder.getTie()) //Tie has ended
        {
            tEnd = true;
            slurWP =0;
        }
        //cursor.write(firstSlurC = QuillUtils.chord(rem, QuillUtils.getPitch(p, a, o)));

       // System.out.printf("pitch %c alteration %c register %c duration %c\n",p,a,r,d);

        Fraction fr = QuillUtils.getFraction(d);
        String s = new String();
        s += r;
        int o = Integer.parseInt(s) - 1;
        Fraction rem = getRemainingBeats();

        if (rem.isGreater0() && rem.compareTo(fr) < 0) {
           // System.out.print("YES1");
//            write Chord with rem and fr - rem
            //System.out.println("[splitting note]");
          //  System.out.println("fr: " + fr + " rem: " + rem + " fr-rem: " + fr.sub(rem));
//            startSlur();

            cursor.write(firstSlurC = QuillUtils.chord(rem, QuillUtils.getPitch(p, a, o)));
            closeBeam();
            cursor.write(lastSlurC = QuillUtils.chord(fr.sub(rem), QuillUtils.getPitch(p, a, o)));
//            closeSlur();
            writeTied();
        } else if (rem.isGreater0() && rem.compareTo(fr) == 0) { //last note of the measure
            Chord curChord = QuillUtils.chord(fr, QuillUtils.getPitch(p, a, o));
           // System.out.print("YES2");
            if (d == 'w' || d == 'h' || d == 'q')
                closeBeam();

            cursor.write(curChord);
            closeBeam();
        }
        else {
            Chord curChord = QuillUtils.chord(fr, QuillUtils.getPitch(p, a, o));

            if(tStart && !sChord) {
                cursor.openSlur(SlurType.Tie);
                cursor.getOpenSlurWaypoints().add(new SlurWaypoint(curChord,  null, null));
                sChord = true;
            }
            if(tStart && sChord && !tEnd)
            {
                cursor.getOpenSlurWaypoints().add(new SlurWaypoint(curChord, null, null));
            }
            if(tEnd && !eChord) {
                cursor.getOpenSlurWaypoints().add(new SlurWaypoint(curChord, null, null));
                cursor.closeSlur();
                //eChord = true;
                tEnd = tStart = sChord = eChord = false;
                slurWP = 0;
            }

            if (d == 'w' || d == 'h' || d == 'q')
                closeBeam();
            else {
                if (openBeam) {
                    beamCounter = beamCounter.add(fr);
                    if (beamCounter.compareTo(theBeat) >= 0) {
                        cursor.write(curChord);
                        closeBeam();
                        return;
                    }

                } else {
                    openBeam = true;
                    beamCounter = fr;
                    cursor.openBeam();
                }
            }
                cursor.write(curChord);
        }
    }

    void startTie() {
        cursor.openSlur(SlurType.Tie);
    }

    void endTie() {
        cursor.closeSlur();
    }

    void writeTied() {
        new SlurAdd(new Slur(SlurType.Tie, QuillUtils.clwp(firstSlurC, startBp), QuillUtils.clwp(lastSlurC, endBp), null)).execute();
        firstSlurC = null;
        lastSlurC = null;
    }

//    void startSlur() {
//        cursor.openSlur(SlurType.Slur);
//    }
//    void attachSlur() {

//    }
//    void closeSlur(){
//        cursor.closeSlur();
//    }

    void openBeam(Fraction curNote) {

    }

    void closeBeam() {
        if (openBeam) {
            openBeam = false;
            cursor.safeCloseBeam();
        }
    }

    void safeCloseBeam() {
        openBeam = false;
        cursor.safeCloseBeam();
    }

    void writeRest(char r) {
        closeBeam();

        Fraction fr = QuillUtils.getFraction(r);
        Fraction rem = getRemainingBeats();

        if (rem.isGreater0() && rem.compareTo(fr) < 0) {
            System.out.println("[splitting note]");
            System.out.println("fr: " + fr + " rem: " + rem + " fr-rem: " + fr.sub(rem));

            cursor.write(new Rest(fr.sub(rem)));
            cursor.write(new Rest(fr));
        } else {

            cursor.write(new Rest(fr));
        }
    }

//    void writePitchBend(PitchBend pitchBend) {
//
//    }
//
    void writeInstrument(String instr) {
        Instrument instrument = Instrument.defaultInstrument;
        Part pianoPart = new Part("Piano", null, 1, alist(instrument));
        new PartAdd(cursor.getScore(), pianoPart, 0, null).execute();
//        cursor.write();
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
    void writeChord(String chord) {

    }

    void writeTriplet(String triplet) {

    }

    private String getTempoName(int bpm){
        switch (bpm) {
            case 20:
                return "Larghissimo";
            case 30:
                return "Grave";
            case 45:
                return "Largo";
            case 60:
                return "Larghetto";
            case 70:
                return "Adagio";
            case 80:
                return "Andante";
            case 95:
                return "Andante moderato";
            case 110:
                return "Moderato";
            case 120:
                return "Allegretto";
            case 135:
                return "Allegro";
            case 170:
                return "Vivace";
            case 185:
                return "Presto";
            case 200:
                return "Prestissimo";
            default:
                return "Custom";
        }
                        /*
            Larghissimo –  (24 BPM and under)
            Grave – w 30
            Largo –45
            Larghetto – 60
            Adagio – 70
            Andante – 80
            Andante moderato 95
            Moderato – 110
            Allegretto – (120
            Allegro –135
            Vivace – 170
            Presto –  185
            Prestissimo – (200
            */

    }



    @Override
    public void commandExecuted(Document document, Command command) {

    }

    @Override
    public void commandUndone(Document document, Command command) {

    }
}