package io_handler;

import com.xenoage.utils.jse.io.JseOutputStream;
import com.xenoage.utils.jse.xml.JseXmlWriter;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.info.ScoreInfo;
import com.xenoage.zong.core.music.*;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.chord.Stem;
import com.xenoage.zong.core.music.clef.Clef;
import com.xenoage.zong.core.music.clef.ClefType;
import com.xenoage.zong.core.music.key.Key;
import com.xenoage.zong.core.music.key.TraditionalKey;
import com.xenoage.zong.core.music.rest.Rest;
import com.xenoage.zong.core.music.time.Time;
import com.xenoage.zong.core.music.util.BeatE;
import com.xenoage.zong.core.music.util.BeatEList;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.documents.ScoreDoc;
import com.xenoage.zong.musicxml.MusicXMLDocument;
import com.xenoage.zong.musicxml.types.*;
import com.xenoage.zong.musicxml.types.attributes.MxlColor;
import com.xenoage.zong.musicxml.types.attributes.MxlPosition;
import com.xenoage.zong.musicxml.types.choice.*;
import com.xenoage.zong.musicxml.types.enums.*;
import com.xenoage.zong.musicxml.types.groups.MxlEditorialVoice;
import com.xenoage.zong.musicxml.types.groups.MxlFullNote;
import com.xenoage.zong.musicxml.types.groups.MxlMusicData;
import com.xenoage.zong.musicxml.types.groups.MxlScoreHeader;
import com.xenoage.zong.musicxml.types.partwise.MxlMeasure;
import com.xenoage.zong.musicxml.types.partwise.MxlPart;

import static com.xenoage.zong.core.music.util.Interval.At;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ben on 3/30/2015.
 */
public class ScoreMXMLBuilder {
    private ScoreDoc scoreDoc = null;
    private JseOutputStream outputStream = null;
    private JseXmlWriter xmlWriter = null;
    private MusicXMLDocument xmlDoc = null;

    public ScoreMXMLBuilder(ScoreDoc scoreDoc, File outFile) {
//        File outFile = new File(file);
//        file = new File(System.getProperty("user.home") + System.getProperty("file.separator") + "test.xml");
        try {
            this.outputStream = new JseOutputStream(outFile);
            this.xmlWriter =  new JseXmlWriter(outputStream);
            this.scoreDoc = scoreDoc;
            buildXmlScore();
            xmlDoc.write(xmlWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void buildXmlScore() {
        List<MxlPart> parts = new ArrayList<MxlPart>();

        for (int i = 0; i < scoreDoc.getScore().getStavesCount(); i++)
            parts.add(buildPart(i));

        String version = "1.0";

        xmlDoc = new MusicXMLDocument(new MxlScorePartwise(buildMeta(), parts, version));
    }

    MxlScoreHeader buildMeta() {
        ScoreInfo scoreInfo = scoreDoc.getScore().getInfo();
        MxlScoreHeader mxlScoreHeader = new MxlScoreHeader();

        mxlScoreHeader.setWork(buildMxlWork(scoreInfo));
        mxlScoreHeader.setPartList(buildMxlPartList(scoreInfo));

        return mxlScoreHeader;
    }

    MxlWork buildMxlWork(ScoreInfo scoreInfo) {
        MxlWork work;
        String workTitle = scoreInfo.getWorkTitle();
        String workNumber = scoreInfo.getWorkNumber();

        if (workTitle == null)
            work = MxlWork.empty;
        else
            work = new MxlWork(workNumber, workTitle);

        return work;
    }

    MxlPartList buildMxlPartList(ScoreInfo scoreInfo) {

        //        mxlScoreHeader.setCredits(new ArrayList<MxlCredit>(new MxlCredit(scoreInfo.getComposer(), null)));

        String movementNumber = scoreInfo.getMovementNumber();
        String movementTitle = scoreInfo.getMovementTitle();

        if (movementNumber == null)
            movementNumber = "I";

        if (movementTitle == null)
            movementTitle = "default movement";

        MxlTypedText mxlTypedText = new MxlTypedText(movementNumber, movementTitle);
        List<MxlTypedText> mxlTypedTexts = new ArrayList<MxlTypedText>();
        mxlTypedTexts.add(mxlTypedText);
        MxlIdentification identification = new MxlIdentification(mxlTypedTexts, mxlTypedTexts);
        MxlDefaults defaults;

//        List<MxlCredit> credits = scoreInfo.getCreators();

        List<MxlScoreInstrument> scoreInstruments = new ArrayList<>();
        String instr = "Acoustic Grand Piano";
        String part = "piano";
        String abbr = "piano";
        scoreInstruments.add(new MxlScoreInstrument(instr, abbr,"P1-T1"));

        List<MxlMidiInstrument> mxlMidiInstruments = new ArrayList<>();
        mxlMidiInstruments.add(new MxlMidiInstrument(new Integer(1), new Integer(1), 1.0f, 1.0f,"P1-T1"));

        MxlScorePart mxlScorePart = new MxlScorePart(identification, part, abbr,
                scoreInstruments, mxlMidiInstruments, "P1");


        ArrayList<MxlPartListContent> mxlScoreParts = new ArrayList<>();
        mxlScoreParts.add(mxlScorePart);
        MxlPartList partList = new MxlPartList(mxlScoreParts);
        return partList;
    }

    MxlPart buildPart(int staffIndex) {

        Staff staff = scoreDoc.getScore().getStaff(staffIndex);
        List<MxlMeasure> partList = new ArrayList<MxlMeasure>();

        for (int i = 0; i < scoreDoc.getScore().getMeasuresCount(); i++)
            partList.add(buildMeasure(staff.getMeasure(i), i+1));

        MxlPart mxlPart = new MxlPart(partList, "P1");

        return mxlPart;
    }

    MxlAttributes buildAttributes(Staff staff) {
//        MusicContext context = scoreDoc.getScore().getMusicContext(getLastMeasure(),At ,At);
        Time time = scoreDoc.getScore().getHeader().getTimeAtOrBefore(0);
        boolean numed = false;
        int num = 0, den = 0;
        String stuff = time.getType().toString();
        for (char c : stuff.toCharArray()) {
            if (c > '0' && c < '9')
                if(!numed) {
                    num  = c - 48;
                    numed = true;
                } else
                    den  = c - 48;
        }

        Integer j = null;

        MxlTranspose mxlTranspose = null;

        MxlAttributes mxlAttributes = new MxlAttributes(
            //divisions
            new Integer(staff.getParent().computeDivisions()),
                buildKey(),
            new MxlTime(new MxlNormalTime(num, den), MxlTimeSymbol.Normal),
            new Integer(1),
            buildClefs(),
            mxlTranspose
        );

        return mxlAttributes;
    }

    MxlMeasure buildMeasure(Measure measure, int i) {

        MxlMusicData mxlMusicData = new MxlMusicData();

        if (i == 1) //first measure, add the attributes for beats, keys, yadda
            mxlMusicData.getContent().add(buildAttributes(measure.getParent()));

        for (Voice voice : measure.getVoices()) {
            for (VoiceElement element : voice.getElements()) {
                mxlMusicData.getContent().add(buildNote(element)); //=musicdata -- list of musicDataContent
            }
        }

        MxlMeasure mxlMeasure = new MxlMeasure(mxlMusicData, new Integer(i).toString());

        return mxlMeasure;
    }

    MxlMusicDataContent buildNote(VoiceElement element) {
        MxlNoteContent mxlNoteContent = buildNoteContent(element);
        MxlInstrument mxlInstrument = buildInstr();
        MxlEditorialVoice mxlEditorialVoice = buildEdit();
        MxlNoteTypeValue mxlNoteTypeValue = getNoteTypeValue(element);

        MxlStem mxlStem = buildStem(element);
        Integer mxlStaff = buildStaff();

        List<MxlBeam> beamList = new ArrayList<MxlBeam>();
        List<MxlNotations> notationsList = new ArrayList<MxlNotations>();
        List<MxlLyric> mxlLyrics = new ArrayList<MxlLyric>();
        MxlNote mxlNote =
                new MxlNote(mxlNoteContent,
                        mxlInstrument,
                        mxlEditorialVoice,
                        mxlNoteTypeValue,
                        0,
                        mxlStem,
                        mxlStaff,
                        beamList,
                        notationsList,
                        mxlLyrics );

        return mxlNote;
    }

    MxlNoteContent buildNoteContent(VoiceElement element) { //ignoring grace, cue
        MxlNormalNote mxlNormalNote = new MxlNormalNote( );
        mxlNormalNote.setFullNote(buildFullNote(element));
        mxlNormalNote.setDuration(getDuration(element));
        return mxlNormalNote;
    }

    MxlNoteTypeValue getNoteTypeValue(VoiceElement element) {
        Fraction fr = element.getDuration();
        MxlNoteTypeValue mxlNoteTypeValue = null;

        if (fr.compareTo(MxlNoteTypeValue.Whole.getDuration()) == 0) {
            mxlNoteTypeValue = MxlNoteTypeValue.Whole;
        } else if (fr.compareTo(MxlNoteTypeValue.Half.getDuration()) == 0) {
            mxlNoteTypeValue = MxlNoteTypeValue.Half;
        } else if (fr.compareTo(MxlNoteTypeValue.Quarter.getDuration()) == 0) {
            mxlNoteTypeValue = MxlNoteTypeValue.Quarter;
        } else if (fr.compareTo(MxlNoteTypeValue.Eighth.getDuration()) == 0) {
            mxlNoteTypeValue = MxlNoteTypeValue.Eighth;
        } else if (fr.compareTo(MxlNoteTypeValue._16th.getDuration()) == 0) {
            mxlNoteTypeValue = MxlNoteTypeValue._16th;
        } else if (fr.compareTo(MxlNoteTypeValue._32nd.getDuration()) == 0) {
            mxlNoteTypeValue = MxlNoteTypeValue._32nd;
        }

        return mxlNoteTypeValue;
    }

    int getDuration(VoiceElement element) {
        MxlNoteTypeValue f = null;
        int div = scoreDoc.getScore().getDivisions();

        Fraction fr = element.getDuration();
        if (fr.compareTo(MxlNoteTypeValue.Whole.getDuration()) == 0) {
           div *= 4;
        } else if (fr.compareTo(MxlNoteTypeValue.Half.getDuration()) == 0) {
           div *= 2;
        } else if (fr.compareTo(MxlNoteTypeValue.Quarter.getDuration()) == 0) {
           div *= 1;
        } else if (fr.compareTo(MxlNoteTypeValue.Eighth.getDuration()) == 0) {
           div /= 2;
        } else if (fr.compareTo(MxlNoteTypeValue._16th.getDuration()) == 0) {
           div /= 4;
        } else if (fr.compareTo(MxlNoteTypeValue._32nd.getDuration()) == 0) {
           div /= 8;
        }

        if (div == 0)
            div = 1;
//        System.out.println();
        return div;
    }


    MxlFullNote buildFullNote(VoiceElement element) {
        MxlFullNote mxlFullNote = new MxlFullNote();
        if (element instanceof Chord) {
            mxlFullNote.setChord(false);
            mxlFullNote.setContent(buildFullNoteContent(element));
            System.out.println("building chord");
        } else {
            mxlFullNote.setChord(false);
            mxlFullNote.setContent(buildFullNoteContent(element));
            System.out.println("building note");
        }

        return mxlFullNote;
    }

    MxlFullNoteContent buildFullNoteContent(VoiceElement element) {
        if (element instanceof Chord) {
            MxlPitch pitch = new MxlPitch(((Chord)element).getPitches().get(0));
            return pitch;
        } else if (element instanceof Rest) {
            MxlRest rest = new MxlRest();
            return rest;
        }
        return null;
    }

    MxlInstrument buildInstr() {
        MxlInstrument mxlInstrument = new MxlInstrument("piano");
//        mxlInstrument.setId("piano");
//        return mxlInstrument;
        return mxlInstrument;
    }

    MxlEditorialVoice buildEdit() {
        MxlEditorialVoice mxlEditorialVoice = new MxlEditorialVoice();
        mxlEditorialVoice.setVoice("1");
        return mxlEditorialVoice;
    }

    MxlStem buildStem(VoiceElement element) {
        MxlStemValue mxlStemValue = null;
        MxlPosition position = null;
        Stem stem = null;
        if (element instanceof Chord)
//            stem = QuillUtils.getClef("treble").get;

        if (stem == null) {
            mxlStemValue = MxlStemValue.Up;
        } else if (stem.getDirection().getSign() == -1) {
            mxlStemValue = MxlStemValue.Down;
        } else if ( stem.getDirection().getSign() == 0) {
            mxlStemValue = MxlStemValue.None;
        } else if (stem.getDirection().getSign() == 1) {
            mxlStemValue = MxlStemValue.Up;
        }

        MxlStem mxlStem = new MxlStem(mxlStemValue, MxlPosition.noPosition, MxlColor.noColor);
        return mxlStem;
    }

    Integer buildStaff() {
        return new Integer(1);
    }

    java.util.List<MxlBeam> buildBeams() {
        return null;
    }

    List<MxlNotations> buildNotations() {
        return null;
    }

    List<MxlLyric> buildLyrics() {
        return null;
    }

    List<MxlClef> buildClefs() {
        Clef clef =  scoreDoc.getScore().getClef(getLastMeasure(), At);

        MxlClef mxlClef = null;

        if (clef.getType() == ClefType.clefBass)
            mxlClef = new MxlClef(MxlClefSign.F, new Integer(clef.getType().getLp()), 0, 1);
        else if (clef.getType() == ClefType.clefTreble)
            mxlClef = new MxlClef(MxlClefSign.G, new Integer(clef.getType().getLp()), 0, 1);
        else if (clef.getType() == ClefType.clefAlto)
            mxlClef = new MxlClef(MxlClefSign.C, new Integer(clef.getType().getLp()), 0, 1);
        else if (clef.getType() == ClefType.clefTenor)
            mxlClef = new MxlClef(MxlClefSign.C, new Integer(clef.getType().getLp()), 0, 1);
        else
            System.err.println("bad clef");

        List<MxlClef> clefs = new ArrayList<MxlClef>();
        clefs.add(mxlClef);
        return clefs;
    }

    MxlKey buildKey() {
        BeatE<Key> keyBeatE;
        BeatEList<Key> list = scoreDoc.getScore().getHeader().getColumnHeader(0).getKeys();
        keyBeatE = list.getFirst();
        MxlKey mxlKey = new MxlKey(((TraditionalKey)keyBeatE.element).getFifths(), MxlMode.Major);
        return mxlKey;
    }

    MP getLastMeasure() {
        MP mp = MP.atVoice(0, scoreDoc.getScore().getMeasuresCount()-1, 0);
        mp = mp.getWithBeat(scoreDoc.getScore());
        mp = mp.withElement(0);

        if (scoreDoc.getScore().isMPExisting(mp))
            return mp;
//            return scoreDoc.getScore().clipToMeasure(scoreDoc.getScore().getMeasuresCount(), mp);
        else {
            System.out.println("invalid MP @ measure: " + scoreDoc.getScore().getMeasuresCount());
            return null;
        }
    }
}