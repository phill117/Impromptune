package com.xenoage.zong.commands.desktop.dialog;

import com.xenoage.utils.document.command.TransparentCommand;
import com.xenoage.zong.desktop.gui.dialogs.FeedbackDialog;
import javafx.stage.Window;

import static com.xenoage.utils.jse.javafx.Dialog.dialog;

/**
 * Shows the feedback dialog.
 *
 * @author Andreas Wenger
 */
//@AllArgsConstructor
public class FeedbackDialogShow
	extends TransparentCommand {
	
	private Window owner;



    public FeedbackDialogShow(Window w)
    {
        owner = w;
    }



    @Override public void execute() {
        dialog(FeedbackDialog.class).showDialog(owner);
    }
}
