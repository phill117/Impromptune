package io_handler;

import com.xenoage.utils.jse.xml.XMLWriter;
import com.xenoage.utils.xml.XmlWriter;

/**
 * Created by ben on 3/30/2015.
 */
public class ZongMXMLWriter extends XmlWriter {

    @Override
    public void writeStartDocument() {
        XMLWriter.createEmptyDocument();
    }

    @Override
    public void writeDTD(String dtd) {
        this.writeDTD(dtd);
    }

    @Override
    public void writeElementStart(String name) {

    }

    @Override
    public void writeElementEnd() {

    }

    @Override
    public void writeElementEmpty(String name) {

    }

    @Override
    public void writeText(String text) {

    }

    @Override
    protected void writeAttributeInternal(String name, String value) {

    }

    @Override
    public void writeComment(String text) {

    }

    @Override
    public void writeLineBreak() {

    }
}
