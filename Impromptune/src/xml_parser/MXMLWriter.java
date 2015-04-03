package xml_parser;

import data_objects.Measure;
import data_objects.MetaData;
import data_objects.Note;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.util.ArrayList;

/**
 * Created by Sean on 3/30/2015.
 */
public class MXMLWriter {

    public File createMXML(){
        StringWriter stringWriter = new StringWriter();
        XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
        MetaData data = MetaData.getInstance();
        File tempFile = null;

        try{
            XMLStreamWriter writer = xmlOutputFactory.createXMLStreamWriter(stringWriter);

            writer.writeStartDocument();
            writer.writeStartElement("score-partwise");
            writer.writeAttribute("version","3.0");

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

                writer.writeStartElement("measure");
                writer.writeAttribute("number",Integer.toString(measure.getMeasureNo()));

                if(isFirst){
                    isFirst = false;
                    //attributes
                    writer.writeStartElement("attributes");
                    //divisions
                    writer.writeStartElement("divisions");
                    writer.writeCharacters(Integer.toString(data.getDivisions()));
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

                    //clef
                    writer.writeStartElement("clef");
                    //sign
                    writer.writeStartElement("sign");
                    writer.writeCharacters(data.getSign());
                    writer.writeEndElement();
                    //line
                    writer.writeStartElement("line");
                    writer.writeCharacters(data.getLine());
                    writer.writeEndElement();
                    //end of clef
                    writer.writeEndElement();

                    //end of attributes
                    writer.writeEndElement();


                }

                for(ArrayList<Note> chord : measure.getChords()) {
                    boolean putChord = false;
                    for (Note note : chord) {

                        writer.writeStartElement("note");

                        if (!note.isRest()) {
                            if(!putChord)putChord = true;
                            else writer.writeEmptyElement("chord");

                            writer.writeStartElement("pitch");
                            //step
                            writer.writeStartElement("step");
                            writer.writeCharacters(Character.toString(note.getPitch()));
                            writer.writeEndElement();
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
                        //type
                        writer.writeStartElement("type");
                        writer.writeCharacters(note.getType());
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
            tempFile = File.createTempFile("creation","xml");
            FileOutputStream outputStream = new FileOutputStream(tempFile);
            outputStream.write(xmlString.getBytes("UTF-8"));

            writer.flush();
            writer.close();

            System.out.println(xmlString);
            return tempFile;
        }catch (Exception e){
            e.printStackTrace();
        }
        return tempFile;
    }

}
