package com.xenoage.zong.musiclayout.spacing.horizontal;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.annotations.MaybeEmpty;
import com.xenoage.utils.collections.CList;
import com.xenoage.utils.collections.IList;
import com.xenoage.zong.core.music.ColumnElement;
import com.xenoage.zong.core.music.MeasureElement;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * This class contains the spacings of the {@link MeasureElement}s
 * and {@link ColumnElement}s of a single measure, like clefs
 * and key signatures.
 * 
 * @author Andreas Wenger
 */
@Const @Getter @AllArgsConstructor public class MeasureElementsSpacings implements Serializable{

	@MaybeEmpty private final IList<SpacingElement> elements;

	public static final MeasureElementsSpacings empty = new MeasureElementsSpacings(
		CList.<SpacingElement>ilist());

}
