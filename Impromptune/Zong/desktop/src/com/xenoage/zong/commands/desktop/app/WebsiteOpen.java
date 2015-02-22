package com.xenoage.zong.commands.desktop.app;

import com.xenoage.utils.document.command.TransparentCommand;

import java.awt.*;
import java.net.URI;

import static com.xenoage.utils.log.Log.log;
import static com.xenoage.utils.log.Report.warning;

/**
 * Opens the given website in the default browser.
 *
 * @author Andreas Wenger
 */

public class WebsiteOpen
	extends TransparentCommand {
	
	private String uri;

	/**
	 * Execute or redo the command.
	 */
	@Override public void execute() {
		try {
			if (false == uri.contains("://"))
				uri = "http://" + uri;
			Desktop.getDesktop().browse(new URI(uri));
		} catch (Exception ex) {
			log(warning("Could not open URI: " + uri + " (" + ex.getMessage() + ")"));
		}
	}

    public WebsiteOpen(String u)
    {uri = u;}


    public void execute(String u) {
        uri = u;
        try {
            if (false == uri.contains("://"))
                uri = "http://" + uri;
            Desktop.getDesktop().browse(new URI(uri));
        } catch (Exception ex) {
            log(warning("Could not open URI: " + uri + " (" + ex.getMessage() + ")"));
        }
    }



}
