package com.xenoage.zong.io.midi.out;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.core.position.MP;
import lombok.AllArgsConstructor;

/**
 * This class maps a MIDI tick to a {@link MP} and an
 * time position in ms.
 * 
 * @author Andreas Wenger
 */
@Const @AllArgsConstructor public class MidiTime {

	public final long tick;
	public final MP mp;
	public final long ms;

}
