package com.xenoage.zong.commands.desktop.dialog;

import com.xenoage.utils.document.command.TransparentCommand;
import com.xenoage.zong.Zong;

import static com.xenoage.zong.desktop.App.app;

/**
 * Shows an about-dialog.
 *
 * @author Andreas Wenger
 */
public class AboutDialog
	extends TransparentCommand {

	@Override public void execute() {
		app().showMessageDialog(
			app().getNameAndVersion() + " (" + Zong.projectIterationName + ")\n\n" + Zong.copyright);
	}

}
