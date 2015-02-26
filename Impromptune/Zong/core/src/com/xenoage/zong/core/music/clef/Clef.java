package com.xenoage.zong.core.music.clef;

//import com.xenoage.utils.annotations.NonNull;
import com.xenoage.zong.core.music.MeasureElement;
import com.xenoage.zong.core.position.MPContainer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;


/**
 * Class for a clef.
 * 
 * @author Andreas Wenger
 */
@Data @EqualsAndHashCode(exclude="parent")
public final class Clef
	implements MeasureElement
{

	/** The type of the clef. */
	@NonNull
    private ClefType type;
	
	/** Back reference: the parent element, or null, if not part of a score. */
	private MPContainer parent;

}
