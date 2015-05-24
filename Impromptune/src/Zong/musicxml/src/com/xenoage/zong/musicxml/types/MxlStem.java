package com.xenoage.zong.musicxml.types;

import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.attributes.MxlColor;
import com.xenoage.zong.musicxml.types.attributes.MxlPosition;
import com.xenoage.zong.musicxml.types.enums.MxlStemValue;
import com.xenoage.zong.musicxml.types.util.MxlPositionContent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * MusicXML stem.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter @Setter
public final class MxlStem
	implements MxlPositionContent, Serializable {

	public static final String elemName = "stem";

	@NonNull private MxlStemValue value;
	private MxlPosition position;
	private MxlColor color;
//
//    public MxlStem(MxlStemValue mxlStemValue, MxlPosition mxlPosition, MxlColor mxlColor) {
//        this.value = mxlStemValue;
//        this.position = mxlPosition;
//        this.color = mxlColor;
//    }

	@NonNull public static MxlStem read(XmlReader reader) {
		MxlPosition yPosition = MxlPosition.read(reader);
		MxlColor color = MxlColor.read(reader);
		MxlStemValue stem = MxlStemValue.read(reader);
		return new MxlStem(stem, yPosition, color);
	}

	public void write(XmlWriter writer) {
		writer.writeElementStart(elemName);
		position.write(writer);
		color.write(writer);
		value.write(writer);
		writer.writeElementEnd();
	}

}
