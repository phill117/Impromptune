package com.xenoage.zong.symbols.common;

import com.xenoage.zong.symbols.Symbol;
import com.xenoage.zong.symbols.SymbolPool;
import com.xenoage.zong.symbols.WarningSymbol;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

import static com.xenoage.utils.collections.CollectionUtils.alist;

/**
 * This class manages an array of all symbols
 * that are part of the {@link CommonSymbol} enumeration,
 * allowing access to them in constant time.
 * 
 * @author Andreas Wenger
 */
public class CommonSymbolPool implements Serializable {

	private List<Symbol> symbols;
	
	/** The warning symbol. */
	@Getter public static WarningSymbol warningSymbol;


	public CommonSymbolPool(SymbolPool pool) {
		symbols = alist();
		for (CommonSymbol commonSymbol : CommonSymbol.values()) {
			symbols.add(pool.getSymbol(commonSymbol.getID()));
		}
		warningSymbol = new WarningSymbol();
	}

	/**
	 * Gets the symbol belonging to the given CommonSymbol.
	 */
	public Symbol getSymbol(CommonSymbol commonSymbol) {
		if (commonSymbol.ordinal() < symbols.size()) {
			return symbols.get(commonSymbol.ordinal());
		}
		else {
			return warningSymbol;
		}
	}

}
