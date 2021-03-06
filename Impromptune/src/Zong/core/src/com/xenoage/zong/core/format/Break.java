package com.xenoage.zong.core.format;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.core.header.ColumnHeader;
import com.xenoage.zong.core.music.ColumnElement;
import com.xenoage.zong.core.music.layout.PageBreak;
import com.xenoage.zong.core.music.layout.SystemBreak;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;


/**
 * Break information for a measure column:
 * Is there a system break or a page break forced
 * or prohibited?
 * 
 * @author Andreas Wenger
 */
@Const @Data @EqualsAndHashCode(exclude="parent")
public final class Break
	implements ColumnElement, Serializable {

	private final PageBreak pageBreak;
	private final SystemBreak systemBreak;
	
	/** Back reference: the parent measure column, or null if not part of a score. */
	private ColumnHeader parent = null;


	@Override public String toString() {
		return "Break (" + pageBreak + ", " + systemBreak + ")";
	}


}
