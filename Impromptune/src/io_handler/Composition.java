package io_handler;

import java.io.File;
import java.io.IOException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import com.xenoage.utils.collections.CollectionUtils;
import com.xenoage.utils.color.Color;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.zong.commands.core.music.PartAdd;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.format.LayoutFormat;
import com.xenoage.zong.core.format.PageFormat;
import com.xenoage.zong.core.format.ScoreFormat;
import com.xenoage.zong.core.format.StaffLayout;
import com.xenoage.zong.core.instrument.Instrument;
import com.xenoage.zong.core.music.*;

import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.core.text.FormattedText;
import com.xenoage.zong.desktop.io.ScoreDocIO;

import com.xenoage.zong.desktop.io.musicxml.in.CreditsReader;
import com.xenoage.zong.desktop.io.musicxml.in.MusicXmlScoreDocFileInput;
import com.xenoage.zong.documents.ScoreDoc;
import com.xenoage.zong.io.musiclayout.LayoutSettingsReader;
import com.xenoage.zong.io.musicxml.in.readers.LayoutFormatReader;
import com.xenoage.zong.io.selection.Cursor;
import com.xenoage.zong.layout.Layout;
import com.xenoage.zong.layout.LayoutDefaults;
import com.xenoage.zong.layout.Page;
import com.xenoage.zong.layout.frames.ScoreFrame;
import com.xenoage.zong.layout.frames.ScoreFrameChain;
import com.xenoage.zong.layout.frames.TextFrame;
import com.xenoage.zong.musiclayout.ScoreLayout;
import com.xenoage.zong.musiclayout.layouter.PlaybackLayouter;
import com.xenoage.zong.musiclayout.layouter.ScoreLayouter;
import com.xenoage.zong.musiclayout.settings.LayoutSettings;
import com.xenoage.zong.musicxml.types.MxlCredit;
import com.xenoage.zong.musicxml.types.MxlCreditWords;
import com.xenoage.zong.musicxml.types.MxlFormattedText;
import com.xenoage.zong.musicxml.types.MxlScorePartwise;
import com.xenoage.zong.musicxml.types.attributes.*;
import com.xenoage.zong.musicxml.types.enums.MxlFontStyle;
import com.xenoage.zong.musicxml.types.enums.MxlFontWeight;
import com.xenoage.zong.musicxml.types.enums.MxlLeftCenterRight;
import com.xenoage.zong.musicxml.types.enums.MxlVAlign;
import com.xenoage.zong.musicxml.types.groups.MxlScoreHeader;
import com.xenoage.zong.symbols.SymbolPool;

import static com.xenoage.utils.iterators.It.it;
import static com.xenoage.utils.math.Fraction._0;
import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.zong.core.position.MP.mp;
import static com.xenoage.zong.util.ZongPlatformUtils.zongPlatformUtils;
import static com.xenoage.utils.jse.JsePlatformUtils.jsePlatformUtils;
/**
 * Created by ben on 2/25/15.
 */
public class Composition implements Serializable{

    //internal zong score
    private Score currentComp = null;
    private ArrayList<Quill> quills = null;

    //for the piano
    private String fifthtype = "sharp";

    //for viewage
    private LayoutSettings layoutSettings = null;
    private ScoreDoc currentScoreDoc = null;
    private Layout layout = null;
    private PlaybackLayouter playbackLayouter = null;
    private LayoutDefaults layoutDefaults = null;
    private SymbolPool symbolPool = null;
    private int scoreIndex = 0;
    private int currentIndex = 0;
    private int parts = 0;

    private  MxlCredit credit;
    public String title;
    public String creator;
    public String timeSig;
    public boolean loaded = false;
     public String clef;
    //const
    private final float SPACING = 9;

    //DEADBEEF
    private static final long serialVersionUID = 3735928559L;

    public Composition() {
        quills = new ArrayList<Quill>();
        currentComp = initializeEmptyScore();
        setLayoutFormat(currentComp);
        currentScoreDoc = initializeScoreDoc(currentComp);
        layout = currentScoreDoc.getLayout();
        currentComp.getCommandPerformer().addCommandListener(quills.get(0)); //first one for now
    }

    public Composition(String fileName) {
        quills = new ArrayList<Quill>();
        currentScoreDoc = initializeScoreDocFromFile(fileName);
        currentComp = currentScoreDoc.getScore();

        currentComp.setTitle(this.title);
        currentComp.setCreator(this.creator);

        layout = currentScoreDoc.getLayout();

        MP mp = getLastMeasure();

        quills.add(parts++, new Quill(new Cursor(currentComp, mp, true), "Piano"));
        currentComp.getCommandPerformer().addCommandListener(quills.get(0)); //first one for now

    }

    public Composition(String clef, String key, String keyType, String keyMod, String Time, int bpm,String title,String composer, String fifthtype) {
        this.fifthtype = fifthtype;
        this.timeSig = Time;
        quills = new ArrayList<Quill>();
        currentComp = initializeEmptyScore(clef, key, keyType, keyMod, Time, bpm);
        setLayoutFormat(currentComp);
        currentScoreDoc = initializeScoreDoc(currentComp);
        layout = currentScoreDoc.getLayout();
        currentComp.getCommandPerformer().addCommandListener(quills.get(0)); //first one for now

        currentComp.setTitle(title);
        currentComp.setCreator(composer);

        setInfo(title,composer);


    }



    public MP getLastMeasure() {
        MP mp = MP.atVoice(0, currentComp.getMeasuresCount() - 1, 0);
        mp = mp.getWithBeat(currentComp);
        mp = mp.withElement(0);
        if (currentComp.isMPExisting(mp))
            return currentComp.clipToMeasure(currentComp.getMeasuresCount(), mp);
        else {
            System.out.println("invalid MP @ measure: " + currentComp.getMeasuresCount());
            return null;
        }
    }

    public void addNote(String str) {

        quills.get(currentIndex).writeNote(str);

        currentScoreDoc = initializeScoreDoc(currentComp);
        layout = currentScoreDoc.getLayout();
//        Barline barlineEnd = Barline.barline(BarlineStyle.LightHeavy);
//        new ColumnElementWrite(barlineEnd, currentComp.getColumnHeader(0), null, MeasureSide.Right).execute();
    }

    public void addRest(char r) {
        quills.get(currentIndex).writeRest(r);
    }

    public void resync() {
        currentScoreDoc = initializeScoreDoc(currentComp);
        layout = currentScoreDoc.getLayout();

        if(currentComp.getTitle() != null && currentComp.getCreator() != null)
            setInfo(currentComp.getTitle(),currentComp.getCreator());
        //Redraw title cause wtf
        if(this.credit != null)
           CreditsReader.addTextFrame(this.credit, this.layout, currentComp.getFormat());

    }


   // public void removeOldText()
    //{
        //FormattedText text = createFormattedText(mxlCreditWords, scoreFormat.getLyricFont());
       // textFrame.setText();
       // layout.getPages().get(0).removeFrame(textFrame);
        //textFrame.setText(text);

    //}

    public Layout getLayout() {
        layout.updateScoreLayouts(currentComp);
        return layout;
    }



    public void setInfo(String title,String composer) {
        //Build mxldoc object for title
        MxlScoreHeader mxlScoreHeader = new MxlScoreHeader();



        List<MxlCredit> credits = CollectionUtils.alist();

        //need to grab this shit from somewhere
        this.title = title;
        this.creator = composer;

        String Title = title;
        String Creator = composer;
       // currentScoreDoc.setTitle(title);
        //currentScoreDoc.setCreator(composer);
        MxlCredit credit;
        MxlCreditWords cwords;  //set credit.content to this
        List<MxlFormattedText> items = CollectionUtils.alist(); //WTF

       // MxlFontStyle fontStyle;
       // MxlFontSize fontSize2 = new MxlFontSize(14f, null);
       // MxlFont font1 = new MxlFont(null, null, fontSize1, MxlFontWeight.Bold);
        MxlFont font1 = MxlFont.jFont;
        MxlFont font2 = MxlFont.j2Font;
        MxlColor color = MxlColor.bColor;

        MxlPosition pos1 = new MxlPosition(680.0f, 1850.0f, null, null);
        MxlPrintStyle ps1 = new MxlPrintStyle(pos1, font1, color);

        MxlPosition pos2 = MxlPosition.noPosition;  //use noposition
        MxlPrintStyle ps2 = new MxlPrintStyle(pos2, font2, color);

        MxlFormattedText titleT = new MxlFormattedText(Title, MxlLeftCenterRight.Center, MxlLeftCenterRight.Unknown, MxlVAlign.Top, ps1);
        MxlFormattedText newline = new MxlFormattedText("\n", MxlLeftCenterRight.Unknown, MxlLeftCenterRight.Unknown, MxlVAlign.Unknown, ps2);
        MxlFormattedText creator = new MxlFormattedText(Creator, MxlLeftCenterRight.Unknown, MxlLeftCenterRight.Unknown, MxlVAlign.Top, ps2);

        items.add(titleT);
        items.add(newline);
        items.add(creator);

        cwords = new MxlCreditWords(items);
        credit = new MxlCredit(cwords, 1);
        this.credit = credit;
        credits.add(credit);

        //TODO stuff this in the mxlscorepartwise


       // mxlScoreHeader.setCredits(credits);
       // MxlScorePartwise mxlSP =new MxlScorePartwise( mxlScoreHeader, null, "1");
     //   CreditsReader.read(mxlSP, layout, currentComp.getFormat());

        //for (MxlCredit credit : it(mxlScorePartwise.getScoreHeader().getCredits())) {
          //  addTextFrame(credit, ret, scoreFormat);
      //  }
        //CreditsReader.addTextFrame(credit, layout, currentComp.getFormat());

      //  currentComp.setMetaData("mxldoc", mxlSP);

    }



    //Not sure what this does but we need it -- Jacob
    void setLayoutFormat(Score score) {
        LayoutFormat layoutFormat = new LayoutFormatReader(
                null, score.format.getInterlineSpace() / SPACING).read();

       score.setMetaData("layoutformat", layoutFormat); //TIDY
    }


    void loadLayout(LayoutFormat layoutFormat) {
        try {
            layoutSettings = LayoutSettingsReader.read(jsePlatformUtils().openFile(
                    "data/layout/default.xml"));

            //use default symbol pool
            symbolPool = zongPlatformUtils().getSymbolPool();

            //create layout defaults
            layoutDefaults = new LayoutDefaults(layoutFormat, symbolPool, layoutSettings);

        } catch( IOException e) { e.printStackTrace(); }
    }

    void addNewPart(String instrName, int staffSpan, Score comp) {
        Instrument instr = Instrument.defaultInstrument;
        Part part = new Part(instrName, null, staffSpan, alist(instr));
        new PartAdd(comp, part, 0, null).execute();
        quills.add(parts++, new Quill(new Cursor(currentComp, mp(1, 0, 0, _0, 0), true), instrName));
    }


    public void writeTimeSig(String time) {
        quills.get(currentIndex).writeTime(time);
    }

    public void writeKeySig(String root, String mode, String keyMod) {
        quills.get(currentIndex).writeStaffKeySig(root, mode, keyMod);
    }

    public void writeClef(String clef) {
        quills.get(currentIndex).writeClef(clef);
    }

    public void writeInstr(String instr) {
        quills.get(currentIndex).writeInstrument(instr);
    }

    public void writeTempo(String time, int bpm) {
        quills.get(currentIndex).writeTempo(time, bpm);
    }

    public void addPart(Score score, String part) {
        Instrument instr = Instrument.defaultInstrument;
        Part pianoPart = new Part("Piano", null, 1, alist(instr));
        new PartAdd(score, pianoPart, 0, null).execute();
    }

    Score initializeEmptyScore() {
        Score currentComp = new Score();

        float is = currentComp.getFormat().getInterlineSpace();
        StaffLayout staffLayout = new StaffLayout(is * SPACING); // Was 9, changes distance between staves
        currentComp.getFormat().setStaffLayoutOther(staffLayout);

        addPart(currentComp, "Piano");

        quills.add(parts++, new Quill( new Cursor(currentComp, MP.mp0, true), "Piano"));

        writeClef("treble");
        writeKeySig("B", "major", "♮");
        writeTimeSig("4/4");
        return currentComp;
    }


    Score initializeEmptyScore(String clef, String key, String keyType, String keyMod, String Time, int bpm) {
        Score currentComp = new Score();

        float is = currentComp.getFormat().getInterlineSpace();
        StaffLayout staffLayout = new StaffLayout(is * SPACING); // Was 9, changes distance between staves
        currentComp.getFormat().setStaffLayoutOther(staffLayout);

        addPart(currentComp, "Piano");

        quills.add(parts++, new Quill( new Cursor(currentComp, MP.mp0, true), "Piano"));

        writeClef(clef);
        writeKeySig(key, keyType, keyMod);
        writeTimeSig(Time);
        writeTempo(Time,bpm);
        return currentComp;
    }



    ScoreDoc initializeScoreDocFromFile(String filePath) {
        try {
            ScoreDoc scoreDoc = ScoreDocIO.read(new File(filePath), new MusicXmlScoreDocFileInput());
            setLayoutFormat(scoreDoc.getScore());
            float is = scoreDoc.getScore().getFormat().getInterlineSpace();
            scoreDoc.getScore().getFormat().setStaffLayoutOther(new StaffLayout(is * SPACING));
            scoreDoc.getLayout().updateScoreLayouts(scoreDoc.getScore());

            //Get title and creator from loaded file
            Object o = scoreDoc.getScore().getMetaData().get("mxldoc");
            if (o != null && o instanceof MxlScorePartwise) {
                MxlScorePartwise doc = (MxlScorePartwise) o;
                List<MxlCredit> cred = doc.getScoreHeader().getCredits();
                if(cred != null ) {
                    MxlCredit credit = cred.get(0);
                    MxlCreditWords cw = (MxlCreditWords) credit.getContent();
                    List<MxlFormattedText> items = cw.getItems();
                    if(items.size()  == 3)
                    {
                        this.title = items.get(0).getValue();
                        this.creator = items.get(2).getValue();
                    }
                }
            }
            return scoreDoc;
        } catch(IOException e) { e.printStackTrace(); }

        return null;
    }

    ScoreDoc initializeScoreDoc(Score score) {
        //page format
        LayoutFormat layoutFormat = null;
        Object oLayoutFormat = score.getMetaData().get("layoutformat");

        if (oLayoutFormat instanceof LayoutFormat) {
            layoutFormat = (LayoutFormat) oLayoutFormat;
        }

        //load layout settings
//        if (layoutSettings == null)
            loadLayout(layoutFormat);

        //create the document
        ScoreDoc scoreDoc = new ScoreDoc(score, layoutDefaults);
        Layout layout = scoreDoc.getLayout();

        //layout basics
        PageFormat pageFormat = layoutFormat.getPageFormat(0);  //Change page format default values for width of bar??
        Size2f frameSize = new Size2f(pageFormat.getUseableWidth(), pageFormat.getUseableHeight());
        Point2f framePos = new Point2f(pageFormat.getMargins().getLeft() + frameSize.width / 2,
                pageFormat.getMargins().getTop() + frameSize.height / 2);

        //layout the score to find out the needed space
        ScoreLayouter layouter = new ScoreLayouter(score, symbolPool, layoutSettings, true, frameSize);
        ScoreLayout scoreLayout = layouter.createScoreLayout();

        //create and fill at least one page
        ScoreFrameChain chain = null;
        //System.out.println("Frames:" + scoreLayout.frames.size());
        for (int i = 0; i < scoreLayout.frames.size(); i++) {
            Page page = new Page(pageFormat);
            layout.addPage(page);
            ScoreFrame frame = new ScoreFrame();
            frame.setPosition(framePos);
            frame.setSize(frameSize);
           // frame.
            //TEST frame = frame.withHFill(NoHorizontalSystemFillingStrategy.getInstance());
            page.addFrame(frame);
            if (chain == null) {
                chain = new ScoreFrameChain(score);
                chain.setScoreLayout(scoreLayout);
            }
            chain.add(frame);
        }

        //add credit elements - TIDY
       /* Object o = score.getMetaData().get("mxldoc");
        if (o != null && o instanceof MxlScorePartwise && !loaded) {
            MxlScorePartwise doc = (MxlScorePartwise) o;
            CreditsReader.read(doc, layout, score.getFormat());
        }*/
        loaded = true;


        return scoreDoc;
    }

    public Composition deepCopy() throws Exception{

        //Serialization of object
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(this);

        //De-serialization of object
        ByteArrayInputStream bis = new   ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream in = new ObjectInputStream(bis);
        Composition copied = (Composition) in.readObject();

        return copied;
    }


    public void removeLast() {
        if(currentComp.getCommandPerformer().isUndoPossible()) {
            currentComp.getCommandPerformer().undo();
            System.out.print(("Composition: COMMAND [IS UNDOABLE]"));
        }
    }

    public void addLast() {
        if (currentComp.getCommandPerformer().isRedoPossible()) {
            currentComp.getCommandPerformer().redo();
            System.out.print(("Composition: COMMAND [IS REDOABLE]"));
        }
    }

    public Quill getCurrentPart() {
        return quills.get(currentIndex);
    }

    public ScoreDoc getCurrentScoreDoc() {
        return currentScoreDoc;
    }

    public Score getCurrentScore() {
        return currentComp;
    }

    public void setCurrentScoreDoc(ScoreDoc scoreDoc) {
        currentScoreDoc = scoreDoc;
    }

    public void setCurrentScore(Score score) {
        currentComp = score;
    }

    public void setCurrentLayout(Layout l) {
        layout = l;
    }

    public void setLayouter(PlaybackLayouter pl) {
        playbackLayouter = pl;
    }

    public void setScoreIndex(int i) {
        scoreIndex = i;
    }

    public String getFifthtype(){return fifthtype;}

    public void setFifthtype(String fifthtype) {
        this.fifthtype = fifthtype;
    }
}

