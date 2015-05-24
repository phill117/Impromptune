package Midi;

import org.jfugue.DeviceThatWillReceiveMidi;
import org.jfugue.DeviceThatWillTransmitMidi;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;

/**
 * Created by Sean on 4/26/2015.
 */
public class MidiHandler {

    //creates and returns a listening device froim the Mididevice.info
    public DeviceThatWillTransmitMidi readMidi(MidiDevice.Info info){

        DeviceThatWillTransmitMidi device = null;
        try {
            device = new DeviceThatWillTransmitMidi(info);
        }catch (MidiUnavailableException e){
            e.printStackTrace();
            return null;
        }

        device.startListening();
        return device;

    }

    //takes a listening device, stops it, then reads the pattern
    public void finishReadingFromMidi(DeviceThatWillTransmitMidi device){

    }

}
