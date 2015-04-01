package xml_parser;

import data_objects.Measure;
import data_objects.MetaData;
import data_objects.Note;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by Sean on 3/25/2015.
 */
public class MXMLContentHandler extends DefaultHandler{

    int tab;
    int measureNo;
    Measure currentMeasure;
    Note currentNote;

    MetaData metaData;
    MXML currentFlag = MXML.Measure;

    private enum MXML{
        //Used
        Measure, Divisions, Fifths, Mode, Note, Pitch, Rest, Step, Alter, Octave, Duration, Tie, Actual_Notes, Normal_Notes, Beats, Beat_Type, Type,
        //Not Used
        Chord, Barline, Repeat, NONE

        //NOTE MODE IS IMPLEMENTED IN FINALE, I BELIVE WE SHOULD ALSO USE IT, BECAUSE ANALYSIS
    }

    //called at the start of the doc
    @Override
    public void startDocument() throws SAXException {
        tab = 0;
        metaData = MetaData.getInstance();
        System.out.println("Start");
        super.startDocument();
    }

    //called at the start of each element
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        String s = "";
        for(int i = 0; i < tab; i++) s+="\t";
        tab++;
        System.out.println(s+"Start Element: "+qName);



        if(qName.equals("note")){currentNote = new Note(); return;}
        if(qName.equals("step")){currentFlag = MXML.Step; return;}
        if(qName.equals("octave")){currentFlag = MXML.Octave; return;}
        if(qName.equals("alter")){currentFlag = MXML.Alter; return;}
        if(qName.equals("duration")){currentFlag = MXML.Duration; return;}
        if(qName.equals("type")){currentFlag = MXML.Type; return;}
        if(qName.equals("rest")){currentNote.setPitch('r');return;}

        if(qName.equals("measure")){
            currentFlag = MXML.Measure;
            measureNo = Integer.parseInt(attributes.getValue("number"));
            currentMeasure = new Measure(measureNo);
            return;
        }

        if(qName.equals("mode")){currentFlag = MXML.Mode; return;}
        if(qName.equals("divisions")){currentFlag = MXML.Divisions; return;}
        if(qName.equals("fifths")){currentFlag = MXML.Fifths; return;}
        if(qName.equals("sound")){metaData.setTempo(Integer.parseInt(attributes.getValue("tempo")));return;}
        currentFlag = MXML.NONE;

    }

    //called when inner characters are read
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String data = new String(ch,start,length);
        System.out.println(data);

        switch (currentFlag){
            case Divisions:
                metaData.setDivisions(Integer.parseInt(data));
                break;
            case Fifths:
                metaData.setSharps(Integer.parseInt(data));
                break;
            case Mode:
                metaData.setMajor(data.equals("major"));
                break;
            case Step:
                currentNote.setPitch(data.toLowerCase().charAt(0));
                break;
            case Alter:
                currentNote.setAccidental(Integer.parseInt(data));
                break;
            case Octave:
                currentNote.setOctave(Integer.parseInt(data));
                break;
            case Duration:
                currentNote.setDuration(Integer.parseInt(data));
                break;
            case Type:
                currentNote.setType(data);
                break;
            case Actual_Notes:
                break;
            case Normal_Notes:
                break;
            case Beats:
                metaData.setBeats(Integer.parseInt(data));
                break;
            case Beat_Type:
                metaData.setBeattype(Integer.parseInt(data));
                break;

            default:
                if(currentFlag != MXML.NONE)
                    System.out.println("\ndefault case? : " + currentFlag);
                break;
        }
    }

    //called at the end of and element
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        tab--;
        String s = "";
        for(int i = 0; i < tab; i++) s+="\t";
        System.out.println(s+"End Element: "+qName);

        if (qName.equals("note")){currentMeasure.addNote(currentNote); return;}
        if (qName.equals("measue")){metaData.addMeasure(currentMeasure);return;}
    }

    //called at the end of document
    @Override
    public void endDocument() throws SAXException {
        System.out.println("End");
        super.endDocument();
    }
}
