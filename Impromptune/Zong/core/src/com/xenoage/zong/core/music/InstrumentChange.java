package com.xenoage.zong.core.music;

import com.xenoage.zong.core.instrument.Instrument;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


/**
 * This element indicates the change of an instrument
 * within a staff.
 * 
 * @author Andreas Wenger
 */
@RequiredArgsConstructor public final class InstrumentChange
	implements MeasureElement {

	/** The instrument to switch to. */
	@Getter @Setter @NonNull private Instrument instrument;

	/** Back reference: The parent measure, or null if not part of a measure. */
	@Getter @Setter private Measure parent;

}
