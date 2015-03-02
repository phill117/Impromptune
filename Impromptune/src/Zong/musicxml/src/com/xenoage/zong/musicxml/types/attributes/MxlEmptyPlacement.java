package com.xenoage.zong.musicxml.types.attributes;

import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.enums.MxlPlacement;
import com.xenoage.zong.musicxml.types.util.MxlPlacementContent;
import com.xenoage.zong.musicxml.types.util.MxlPrintStyleContent;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.xenoage.zong.musicxml.types.attributes.MxlPrintStyle.noPrintStyle;
import static com.xenoage.zong.musicxml.types.enums.MxlPlacement.Unknown;

/**
 * MusicXML empty-placement.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter
public final class MxlEmptyPlacement
	implements MxlPrintStyleContent, MxlPlacementContent {
	
	public static final MxlEmptyPlacement noEmptyPlacement = new MxlEmptyPlacement(
		noPrintStyle, Unknown);

	private final MxlPrintStyle printStyle;
	private final MxlPlacement placement;


	public static MxlEmptyPlacement read(XmlReader reader) {
		MxlPrintStyle printStyle = MxlPrintStyle.read(reader);
		MxlPlacement placement = MxlPlacement.read(reader);
		if (printStyle != noPrintStyle || placement != Unknown)
			return new MxlEmptyPlacement(printStyle, placement);
		else
			return noEmptyPlacement;
	}

	public void write(XmlWriter writer) {
		if (this != noEmptyPlacement) {
			printStyle.write(writer);
			placement.write(writer);
		}
	}

}
