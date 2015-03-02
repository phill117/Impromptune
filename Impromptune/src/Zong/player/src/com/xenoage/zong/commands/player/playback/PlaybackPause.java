package com.xenoage.zong.commands.player.playback;

import com.xenoage.utils.document.command.TransparentCommand;

import static com.xenoage.zong.player.Player.pApp;

/**
 * This command pauses the MIDI playback.
 * 
 * @author Andreas Wenger
 */
public class PlaybackPause
	extends TransparentCommand {

	@Override public void execute() {
		pApp().getPlayer().pause();
	}

}
