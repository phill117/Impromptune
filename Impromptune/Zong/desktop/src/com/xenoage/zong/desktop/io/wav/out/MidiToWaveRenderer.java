package com.xenoage.zong.desktop.io.wav.out;

/*
 * Copyright (c) 2007 by Karl Helgason All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following
 * conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * disclaimer. - Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import com.sun.media.sound.AudioSynthesizer;

import javax.sound.midi.*;
import javax.sound.midi.MidiDevice.Info;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;

/**
 * Converts a Midi {@link Sequence} to a sampled audio file (.wav) stream.
 * 
 * Based on code from the Gervill project, see
 * the above copyright notice.
 * 
 * @author Andreas Wenger
 */
public class MidiToWaveRenderer {

	/**
	 * Render sequence using selected or default soundbank into wave audio file.
	 * If activeTracks is not null, only the tracks with the given indices are rendered.
	 */
	public static void render(Soundbank soundbank, Sequence sequence, Set<Integer> activeTracks,
		OutputStream wavOutputStream)
		throws IOException, MidiUnavailableException {
		// Find available AudioSynthesizer.
		AudioSynthesizer synth = findAudioSynthesizer();
		if (synth == null) {
			System.out.println("No AudioSynhtesizer was found!");
			System.exit(1);
		}

		// Open AudioStream from AudioSynthesizer.
		AudioInputStream stream = synth.openStream(null, null);

		// Load user-selected Soundbank into AudioSynthesizer.
		if (soundbank != null) {
			Soundbank defsbk = synth.getDefaultSoundbank();
			if (defsbk != null)
				synth.unloadAllInstruments(defsbk);
			synth.loadAllInstruments(soundbank);
		}

		// Play Sequence into AudioSynthesizer Receiver.
		double total = send(sequence, activeTracks, synth.getReceiver());

		// Calculate how long the WAVE file needs to be.
		long len = (long) (stream.getFormat().getFrameRate() * (total + 4));
		stream = new AudioInputStream(stream, stream.getFormat(), len);

		// Write WAVE file to disk.
		AudioSystem.write(stream, AudioFileFormat.Type.WAVE, wavOutputStream);

		// We are finished, close synthesizer.
		synth.close();
	}

	/*
	 * Find available AudioSynthesizer.
	 */
	public static AudioSynthesizer findAudioSynthesizer()
		throws MidiUnavailableException {
		// First check if default synthesizer is AudioSynthesizer.
		Synthesizer synth = MidiSystem.getSynthesizer();
		if (synth instanceof AudioSynthesizer)
			return (AudioSynthesizer) synth;

		// If default synhtesizer is not AudioSynthesizer, check others.
		Info[] infos = MidiSystem.getMidiDeviceInfo();
		for (int i = 0; i < infos.length; i++) {
			MidiDevice dev = MidiSystem.getMidiDevice(infos[i]);
			if (dev instanceof AudioSynthesizer)
				return (AudioSynthesizer) dev;
		}

		// No AudioSynthesizer was found, return null.
		return null;
	}

	/**
	 * Send entry MIDI Sequence into Receiver using timestamps.
	 * If activeTracks is not null, only the tracks with the given indices are rendered.
	 */
	private static double send(Sequence seq, Set<Integer> activeTracks, Receiver recv) {
		float divtype = seq.getDivisionType();
		assert (seq.getDivisionType() == Sequence.PPQ);
		Track[] tracks = seq.getTracks();
		int[] trackspos = new int[tracks.length];
		int mpq = 500000;
		int seqres = seq.getResolution();
		long lasttick = 0;
		long curtime = 0;
		while (true) {
			MidiEvent selevent = null;
			int seltrack = -1;
			for (int i = 0; i < tracks.length; i++) {
				if (activeTracks != null && !activeTracks.contains(i))
					continue;
				int trackpos = trackspos[i];
				Track track = tracks[i];
				if (trackpos < track.size()) {
					MidiEvent event = track.get(trackpos);
					if (selevent == null || event.getTick() < selevent.getTick()) {
						selevent = event;
						seltrack = i;
					}
				}
			}
			if (seltrack == -1)
				break;
			trackspos[seltrack]++;
			long tick = selevent.getTick();
			if (divtype == Sequence.PPQ)
				curtime += ((tick - lasttick) * mpq) / seqres;
			else
				curtime = (long) ((tick * 1000000.0 * divtype) / seqres);
			lasttick = tick;
			MidiMessage msg = selevent.getMessage();
			if (msg instanceof MetaMessage) {
				if (divtype == Sequence.PPQ)
					if (((MetaMessage) msg).getType() == 0x51) {
						byte[] data = ((MetaMessage) msg).getData();
						mpq = ((data[0] & 0xff) << 16) | ((data[1] & 0xff) << 8) | (data[2] & 0xff);
					}
			}
			else {
				if (recv != null)
					recv.send(msg, curtime);
			}
		}
		return curtime / 1000000.0;
	}

}
