package com.xenoage.zong.core.music.direction;

import com.xenoage.zong.core.header.ColumnHeader;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * Class for a DaCapo.
 * 
 * It must always be written to the {@link ColumnHeader}.
 * 
 * @Andreas Wenger
 */
@Data @EqualsAndHashCode(callSuper=false)
public final class DaCapo
	extends Direction
	implements NavigationMarker {

}
