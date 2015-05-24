package com.xenoage.zong.musicxml.types.attributes;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.enums.MxlCSSFontSize;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

/**
 * MusicXML font-size.
 * 
 * @author Andreas Wenger
 */
@RequiredArgsConstructor @Getter
public final class MxlFontSize implements Serializable {

	public static final String attrName = "font-size";
	public static final MxlFontSize noFontSize = new MxlFontSize(null, null);

    public static final MxlFontSize jFontSize = new MxlFontSize(24f,  MxlCSSFontSize.Medium);
    public static final MxlFontSize j2FontSize = new MxlFontSize(14f,  MxlCSSFontSize.Small);
	/** The font-size in points. */
	@MaybeNull private final Float valuePt;
	/** The font-size as a CSS font size. */
	@MaybeNull private final MxlCSSFontSize valueCSS;

	
	public static MxlFontSize read(XmlReader reader) {
		String s = reader.getAttribute(attrName);
		if (s == null)
			return noFontSize;
		else if (Character.isDigit(s.charAt(0)))
			return new MxlFontSize(Float.parseFloat(s), null);
		else
			return new MxlFontSize(null, MxlCSSFontSize.read(s));
	}

	public void write(XmlWriter writer) {
		if (valuePt != null)
			writer.writeAttribute(attrName, "" + valuePt);
		else if (valueCSS != null)
			writer.writeAttribute(attrName, valueCSS.write());
	}

}
