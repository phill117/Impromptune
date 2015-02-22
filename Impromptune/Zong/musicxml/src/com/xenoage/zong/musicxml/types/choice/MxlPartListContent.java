package com.xenoage.zong.musicxml.types.choice;

import com.xenoage.utils.xml.XmlWriter;

/**
 * Interface for all types of content that may appear within
 * a part-list element.
 * 
 * @author Andreas Wenger
 */
public interface MxlPartListContent {

	/**
	 * This enum allows using quick switch-case statements
	 * for finding out the type of the content.
	 */
	public enum PartListContentType {
		PartGroup,
		ScorePart;
	}


	public PartListContentType getPartListContentType();

	public void write(XmlWriter writer);

}
