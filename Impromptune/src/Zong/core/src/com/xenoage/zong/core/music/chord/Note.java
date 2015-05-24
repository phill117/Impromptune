package com.xenoage.zong.core.music.chord;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.zong.core.music.Pitch;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Class for a single note.
 * Within a score, it is always part of a chord.
 *
 * @author Andreas Wenger
 */
@Const @Data public final class Note implements Serializable {

	@NonNull private final Pitch pitch;


	/**
	 * Returns a list of {@link Note}s from the given {@link Pitch}es.
	 */
	public static ArrayList<Note> notes(Pitch... pitches) {
		ArrayList<Note> ret = new ArrayList<Note>(pitches.length);
		for (int i = 0; i < pitches.length; i++) {
			ret.add(new Note(pitches[i]));
		}
		return ret;
	}


	@Override public String toString() {
		return "note(" + pitch.toString() + ")";
	}


}
