package com.xenoage.zong.core.music.direction;

import com.xenoage.utils.annotations.NonNull;
import com.xenoage.zong.core.music.TextElement;
import com.xenoage.zong.core.text.Text;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * Direction words, that are not interpreted by this program.
 *
 * @author Andreas Wenger
 */
@Data @EqualsAndHashCode(callSuper=false)
public final class Words
	extends Direction
	implements TextElement {

	/** The text. */
	@NonNull private Text text;

}
