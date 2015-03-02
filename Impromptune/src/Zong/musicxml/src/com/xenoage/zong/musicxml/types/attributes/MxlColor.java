package com.xenoage.zong.musicxml.types.attributes;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.color.Color;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.xenoage.utils.color.ColorUtils.getColor;
import static com.xenoage.utils.color.ColorUtils.getHex;

/**
 * MusicXML color.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter
public final class MxlColor {

	@MaybeNull private final Color value;

	public static final String attrName = "color";
	public static final MxlColor noColor = new MxlColor(null);


	public static MxlColor read(XmlReader reader) {
		String s = reader.getAttribute(attrName);
		if (s != null) {
			try {
				return new MxlColor(getColor(s));
			} catch (NumberFormatException ex) {
				throw reader.dataException(attrName + " = " + s);
			}
		}
		else {
			return noColor;
		}
	}

	public void write(XmlWriter writer) {
		if (value != null)
			writer.writeAttribute(attrName, getHex(value));
	}

}
