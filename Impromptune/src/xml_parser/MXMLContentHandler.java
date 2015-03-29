package xml_parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by Sean on 3/25/2015.
 */
public class MXMLContentHandler extends DefaultHandler{

    int tab;



    //called at the start of the doc
    @Override
    public void startDocument() throws SAXException {
        tab = 0;
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


    }

    //called when inner characters are read
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {

    }

    //called at the end of and element
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        tab--;
        String s = "";
        for(int i = 0; i < tab; i++) s+="\t";
        System.out.println(s+"End Element: "+qName);
    }

    //called at the end of document
    @Override
    public void endDocument() throws SAXException {
        System.out.println("End");
        super.endDocument();
    }
}
