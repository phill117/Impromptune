package com.xenoage.zong.layout;

import com.xenoage.utils.annotations.NonNull;
import com.xenoage.zong.core.format.LayoutFormat;
import com.xenoage.zong.musiclayout.settings.LayoutSettings;
import com.xenoage.zong.symbols.SymbolPool;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * Default settings within a {@link Layout}.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter public class LayoutDefaults implements Serializable {

	/** The default page formats. */
	@NonNull private LayoutFormat format;
	/** The pool of musical symbols. */
	@NonNull private SymbolPool symbolPool;
	/** The default musical layout settings. */
	@NonNull private LayoutSettings layoutSettings;
	
}
