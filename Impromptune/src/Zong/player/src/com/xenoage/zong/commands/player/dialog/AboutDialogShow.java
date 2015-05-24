package com.xenoage.zong.commands.player.dialog;

import com.xenoage.utils.document.command.TransparentCommand;
import com.xenoage.utils.jse.javafx.Dialog;
import com.xenoage.zong.gui.dialog.AboutDialog;
import javafx.stage.Window;
import lombok.AllArgsConstructor;

/**
 * Shows the {@link AboutDialog}.
 *
 * @author Andreas Wenger
 */
@AllArgsConstructor
public class AboutDialogShow
	extends TransparentCommand {
	
	private Window owner;

	@Override public void execute() {
		Dialog.dialog(AboutDialog.class).showDialog(owner);
	}

}
