package xml_parser;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.FileReader;

/**
 * Created by Sean on 4/24/2015.
 */
public class MXMLDocUtils extends DefaultHandler {

    String docName = "";
    boolean readTitle = false;

    public String getPieceTitle(String absFilePath){

        //create the parser
        SAXParser mxp;
        try {
            mxp = SAXParserFactory.newInstance().newSAXParser();
        }catch(Exception e){
            System.out.println("Could not make parser");
            e.printStackTrace();
            return "Unknown";
        }

        //get the file
        InputSource inputSource;
        try {
            inputSource = new InputSource(new FileReader(absFilePath));
        }catch (Exception e){
            System.out.println("Could not find the file to read from ");
            return "Unknown";
        }

        //parse it
        try {
            mxp.parse(inputSource, this);
        }catch(StopParsingException spe){
            return docName;
        }catch(Exception e){
            System.out.println("NOOO");
            return "Unknown";
        }

        System.out.println("WAIT WHAT! parsing...");

        return "Unknown"; //Nothing worked

    }

    //TODO
    public static boolean isValidFile(String absFilePath){

        return true;
    }


    /**
     * Handling Methods
     */

    @Override
    public void startDocument() throws org.xml.sax.SAXException {
        super.startDocument();
    }

    @Override
    public void endDocument() throws org.xml.sax.SAXException {
        super.endDocument();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws org.xml.sax.SAXException {
        if(qName.equals("credit-words")) readTitle = true;
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws org.xml.sax.SAXException {
        super.endElement(uri, localName, qName);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws org.xml.sax.SAXException {
        if(readTitle){
            String data = new String(ch,start,length);
            docName = data;
            throw new StopParsingException();
        }
    }


    public class StopParsingException extends SAXException{

    }

}
