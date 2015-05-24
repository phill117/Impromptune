package com.xenoage.zong.io.musicxml.in.readers;

import com.xenoage.zong.core.instrument.Transpose;
import com.xenoage.zong.musicxml.types.MxlTranspose;
import lombok.RequiredArgsConstructor;

import static com.xenoage.utils.NullUtils.notNull;

/**
 * Reads a {@link Transpose} from a {@link MxlTranspose}.
 * 
 * @author Andreas Wenger
 */
@RequiredArgsConstructor
public class TransposeReader {
	
	private final MxlTranspose mxlTranspose;
	
	public Transpose read() {
		if (mxlTranspose == null)
			return Transpose.noTranspose;
		return new Transpose(mxlTranspose.getChromatic(), mxlTranspose.getDiatonic(),
			notNull(mxlTranspose.getOctaveChange(), 0), mxlTranspose.isDoubleValue());
	}

}
