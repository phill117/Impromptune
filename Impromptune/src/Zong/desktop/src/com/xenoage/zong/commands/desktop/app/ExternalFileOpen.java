package com.xenoage.zong.commands.desktop.app;

import com.xenoage.utils.document.command.TransparentCommand;

import java.awt.*;

import static com.xenoage.utils.jse.JsePlatformUtils.io;
import static com.xenoage.utils.log.Log.log;
import static com.xenoage.utils.log.Report.warning;

/**
 * Opens the given file in the default program.
 * 
 * The default program for the file type is defined by the OS.
 *
 * @author Andreas Wenger
 */
//@AllArgsConstructor

public class ExternalFileOpen
	extends TransparentCommand {
	
	private String filePath;

	@Override public void execute() {
		try {
			Desktop.getDesktop().open(io().findNormalFile(filePath));
		} catch (Exception ex) {
			log(warning("Could not open file: " + filePath + " (" + ex.getMessage() + ")"));
		}
	}


    public ExternalFileOpen(String fp)
    {
        filePath = fp;
    }

}
