package player;

import org.jfugue.*;

/**
 * Created by Sean on 2/22/2015.
 */
public class MusicStringCreater extends ParserListenerAdapter {

    StringBuilder stringBuilder;
    int startingMeasure = 0;

    public MusicStringCreater(){
        stringBuilder = new StringBuilder();
    }

    public String getCreatedMusicString(){
        return stringBuilder.toString();
    }

    public void setStartingMeasure(int num){
        startingMeasure = num;
    }

    @Override
    public void instrumentEvent(Instrument instrument) {
        stringBuilder.append(instrument.getMusicString()).append(" ");
    }

    @Override
    public void keySignatureEvent(KeySignature keySignature) {
        stringBuilder.append(keySignature.getMusicString()).append(" ");
    }

    @Override
    public void measureEvent(Measure measure) {
        stringBuilder.append(measure.getMusicString()).append(" ");
    }

    @Override
    public void noteEvent(Note note) {
        stringBuilder.append(note.getMusicString()).append(" ");
    }

    @Override
    public void parallelNoteEvent(Note note) {
        stringBuilder.append(note.getMusicString()).append(" ");
    }

    @Override
    public void sequentialNoteEvent(Note note) {
        stringBuilder.append(note.getMusicString()).append(" ");
    }

    @Override
    public void tempoEvent(Tempo tempo) {
        stringBuilder.append(tempo.getMusicString()).append(" ");
    }

    @Override
    public void timeEvent(Time time) {
        stringBuilder.append(time.getMusicString()).append(" ");
    }

    @Override
    public void voiceEvent(Voice voice) {
        stringBuilder.append(voice.getMusicString()).append(" ");
    }
}
