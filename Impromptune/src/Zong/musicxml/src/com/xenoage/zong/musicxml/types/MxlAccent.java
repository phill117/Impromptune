package com.xenoage.zong.musicxml.types;

import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.attributes.MxlEmptyPlacement;
import com.xenoage.zong.musicxml.types.choice.MxlArticulationsContent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * MusicXML accent.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter @Setter
public class MxlAccent
	implements MxlArticulationsContent {

	public static final String elemName = "accent";

	private MxlEmptyPlacement emptyPlacement;

	
	@Override public MxlArticulationsContentType getArticulationsContentType() {
		return MxlArticulationsContentType.Accent;
	}

	public static MxlAccent read(XmlReader reader) {
		MxlEmptyPlacement emptyPlacement = MxlEmptyPlacement.read(reader);
		return new MxlAccent(emptyPlacement);
	}

	@Override public void write(XmlWriter writer) {
		writer.writeElementStart(elemName);
		emptyPlacement.write(writer);
		writer.writeElementEnd();
	}

}
