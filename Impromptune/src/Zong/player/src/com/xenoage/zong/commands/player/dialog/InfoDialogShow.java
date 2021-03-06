package com.xenoage.zong.commands.player.dialog;

import com.xenoage.utils.document.command.TransparentCommand;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.gui.dialog.InfoDialog;
import javafx.stage.Window;
import lombok.AllArgsConstructor;

import static com.xenoage.utils.jse.javafx.Dialog.dialog;

/**
 * Shows the {@link InfoDialog}.
 *
 * @author Andreas Wenger
 */
@AllArgsConstructor
public class InfoDialogShow
	extends TransparentCommand {
	
	private Score score;
	private Window owner;

	@Override public void execute() {
		InfoDialog dialog = dialog(InfoDialog.class);
		dialog.setScore(score);
		dialog.showDialog(owner);
	}

}
