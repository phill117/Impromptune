package com.xenoage.zong.musiclayout.layouter.scoreframelayout.util;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.collections.IList;
import com.xenoage.zong.musiclayout.layouter.cache.util.OpenBeamMiddleStem;
import com.xenoage.zong.musiclayout.stampings.*;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * The stampings belonging to a chord: a list of noteheads,
 * leger lines, dots, accidentals and a stem and flag.
 * 
 * @author Andreas Wenger
 */
@Const @AllArgsConstructor public final class ChordStampings {

	public final float positionX;
	public final StaffStamping staffStamping;

	public final IList<NoteheadStamping> noteheads;
	public final IList<LegerLineStamping> legerLines;
	public final IList<ProlongationDotStamping> dots;
	public final IList<AccidentalStamping> accidentals;
	public final IList<ArticulationStamping> articulations;
	public final FlagsStamping flags;

	//stamped or open stem
	public final StemStamping stem;
	public final OpenBeamMiddleStem openStem;


	public void addAllTo(List<Stamping> list) {
		list.addAll(noteheads);
		list.addAll(legerLines);
		list.addAll(dots);
		list.addAll(accidentals);
		list.addAll(articulations);
		if (stem != null)
			list.add(stem);
		if (flags != null)
			list.add(flags);
	}

}
