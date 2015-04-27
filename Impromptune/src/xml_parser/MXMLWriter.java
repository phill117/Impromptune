package xml_parser;

import data_objects.Measure;
import data_objects.MetaData;
import data_objects.Note;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

/**
 * Created by Sean on 3/30/2015.
 */
public class MXMLWriter {

    private static File tempFile;

    public File createMXML(File outFile){
        StringWriter stringWriter = new StringWriter();
        XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
        MetaData data = MetaData.getInstance();

        try{
            XMLStreamWriter writer = xmlOutputFactory.createXMLStreamWriter(stringWriter);

            writer.writeStartDocument();
            writer.writeDTD("<!DOCTYPE score-partwise PUBLIC \"-//Recordare//DTD MusicXML 2.0 Partwise//EN\" \"http://www.musicxml.org/dtds/partwise.dtd\">");
            writer.writeStartElement("score-partwise");
            writer.writeAttribute("version","3.0");

            addTitleAndComp(writer);

            //the whole 'part-list' part
            writer.writeStartElement("part-list");
            writer.writeStartElement("score-part");
            writer.writeAttribute("id","P1");
            writer.writeStartElement("part-name");
            writer.writeCharacters("Music");
            writer.writeEndElement();
            writer.writeEndElement();
            writer.writeEndElement();


            //write the beginning crap
            writer.writeStartElement("part");
            writer.writeAttribute("id","P1");

            boolean isFirst = true;
            System.out.println("number of Measures: "+data.getMeasures().size());
            for(Measure measure : data.getMeasures()){

                int divisions = data.getDivisions();

                writer.writeStartElement("measure");
                writer.writeAttribute("number",Integer.toString(measure.getMeasureNo()));

                if(isFirst){
                    isFirst = false;
                    //attributes
                    writer.writeStartElement("attributes");
                    //divisions
                    writer.writeStartElement("divisions");
                    writer.writeCharacters(Integer.toString(divisions));
                    writer.writeEndElement();

                    //key
                    writer.writeStartElement("key");
                    //fifths
                    writer.writeStartElement("fifths");
                    writer.writeCharacters(Integer.toString(data.getSharps()));
                    writer.writeEndElement();
                    //mode
                    writer.writeStartElement("mode");
                    if(data.isMajor()) writer.writeCharacters("major");
                    else writer.writeCharacters("minor");
                    writer.writeEndElement();
                    //end of key
                    writer.writeEndElement();

                    //time
                    writer.writeStartElement("time");
                    //beats
                    writer.writeStartElement("beats");
                    writer.writeCharacters(Integer.toString(data.getBeats()));
                    writer.writeEndElement();
                    //beat-type
                    writer.writeStartElement("beat-type");
                    writer.writeCharacters(Integer.toString(data.getBeattype()));
                    writer.writeEndElement();
                    //end of time
                    writer.writeEndElement();

                    //staves
                    writer.writeStartElement("staves");
                    writer.writeCharacters("2");
                    writer.writeEndElement();

                    //first clef
                    writer.writeStartElement("clef");
                    writer.writeAttribute("number", "1");
                    //sign
                    writer.writeStartElement("sign");
                    writer.writeCharacters("G");
                    writer.writeEndElement();
                    //line
                    writer.writeStartElement("line");
                    writer.writeCharacters("2");
                    writer.writeEndElement();
                    //end of first clef
                    writer.writeEndElement();

                    //second clef
                    writer.writeStartElement("clef");
                    writer.writeAttribute("number","2");
                    //sign
                    writer.writeStartElement("sign");
                    writer.writeCharacters("F");
                    writer.writeEndElement();
                    //line
                    writer.writeStartElement("line");
                    writer.writeCharacters("4");
                    writer.writeEndElement();
                    //end of second clef
                    writer.writeEndElement();

                    //end of attributes
                    writer.writeEndElement();


                }

                int partnum = 0;
                for(ArrayList<Note> part : measure.getParts()) {
                    partnum++;

                    //set it so that it always backs up before the not first measure
                    if(partnum != 1) backup(MetaData.getInstance().getDivisionsPerMeasure(),writer);

                    for (Note note : part) {
                        //this method sets the voice number and staff number for that note
                        note.setStaffNo(partnum);

                        writer.writeStartElement("note");

                        if (!note.isRest()) {
//                            if(!putChord)putChord = true;
//                            else writer.writeEmptyElement("chord");

                            writer.writeStartElement("pitch");
                            //step
                            writer.writeStartElement("step");
                            writer.writeCharacters(Character.toString(note.getPitch()).toUpperCase());
                            writer.writeEndElement();
                            //alter
                            if(note.getAccidental() != 0) {
                                writer.writeStartElement("alter");
                                writer.writeCharacters(Integer.toString(note.getAccidental()));
                                writer.writeEndElement();
                            }
                            //octave
                            writer.writeStartElement("octave");
                            writer.writeCharacters(Integer.toString(note.getOctave()));
                            writer.writeEndElement();
                            writer.writeEndElement();
                        } else writer.writeEmptyElement("rest");

                        //duration
                        writer.writeStartElement("duration");
                        writer.writeCharacters(Integer.toString(note.getDuration()));
                        writer.writeEndElement();
                        //tie
                        if(note.isTied()) {
                            writer.writeEmptyElement("tie");
                            writer.writeAttribute("type", note.getTiedType());
                        }

                        //voice
                        writer.writeStartElement("voice");
                        writer.writeCharacters(Integer.toString(partnum));
                        writer.writeEndElement();

                        //type //TODO DODODOD it should always have a type (i guess the reader is wrong and therefore zong is wrong)
                        if(note.getType() != null && !note.getType().equals("")) {
                            writer.writeStartElement("type");
                            writer.writeCharacters(note.getType());
                            writer.writeEndElement();
                        }

                        //dot
                        if(note.isDotted()) writer.writeEmptyElement("dot");

                        //staff
                        writer.writeStartElement("staff");
                        writer.writeCharacters(Integer.toString(note.getStaffNo()));
                        writer.writeEndElement();

                        writer.writeEndElement();

                    }
                }
                //end measure
                writer.writeEndElement();
            }

            //end part
            writer.writeEndElement();

            //end score-partwise
            writer.writeEndElement();
            writer.writeEndDocument();

            String xmlString = stringWriter.getBuffer().toString();

            //Path absPath = Files.createTempFile("creation","xml", StandardOpenOption.DELETE_ON_CLOSE);

            //tempFile = File.createTempFile("creation","xml");
           //tempFile.deleteOnExit();
            FileOutputStream outputStream = new FileOutputStream(outFile);
            outputStream.write(xmlString.getBytes("UTF-8"));

            outputStream.flush();
            outputStream.close();
            writer.flush();
            writer.close();

            System.out.println(xmlString);
            return outFile;
        }catch (Exception e){
            e.printStackTrace();
        }
        return outFile;
    }


    private void backup(int duration, XMLStreamWriter writer) throws XMLStreamException{
        writer.writeStartElement("backup");
        writer.writeStartElement("duration");
        writer.writeCharacters(Integer.toString(duration));
        writer.writeEndElement();
        writer.writeEndElement();
    }

    private void addTitleAndComp(XMLStreamWriter writer) throws XMLStreamException{
        writer.writeStartElement("work"); writer.writeEndElement();
        writer.writeStartElement("credit"); writer.writeAttribute("page","1");

        writer.writeStartElement("credit-words");
        writer.writeAttribute("justify", "center");
        writer.writeAttribute("valign","top");
        writer.writeAttribute("default-x","680.0");
        writer.writeAttribute("default-y","1850.0");
        writer.writeAttribute("font-size","24.0");
        writer.writeAttribute("font-weight","bold");
        writer.writeAttribute("color","#000000");
        writer.writeCharacters(MetaData.getInstance().getTitle());
        writer.writeEndElement();

        writer.writeStartElement("credit-words");
        writer.writeAttribute("font-size","14.0");
        writer.writeAttribute("font-weight","normal");
        writer.writeAttribute("color","#000000");
        writer.writeCharacters("\n");
        writer.writeEndElement();

        writer.writeStartElement("credit-words");
        writer.writeAttribute("valign","top");
        writer.writeAttribute("font-size","14.0");
        writer.writeAttribute("font-weight","normal");
        writer.writeAttribute("color","#000000");
        writer.writeCharacters(MetaData.getInstance().getComposer());
        writer.writeEndElement();

        writer.writeEndElement();
    }
}
