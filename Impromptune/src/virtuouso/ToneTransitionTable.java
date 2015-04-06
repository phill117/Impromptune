package virtuouso;

import utils.LimitedQueue;
import utils.MersenneTwisterFast;

import java.util.LinkedList;
import java.util.List;


/**
 * Created by ben on 4/3/2015.
 */
public class ToneTransitionTable {

    public static void main (String args[]) {
        ToneTransitionTable ttt = new ToneTransitionTable(7);

        for (int i = 0; i < 1444; i++) {
            ttt.trainNote(BlackMagicka.noteIndexToString(ttt.getRand(12)));
        }

        ttt.printHistogram();
        ttt.printProbMatrix();
    }

    private MersenneTwisterFast rand = new MersenneTwisterFast();

    private int order;
    private int statesCounter = 0;

    private LinkedList<String> lastKnotes;
    private LinkedList<MarkovState> markov;

    public ToneTransitionTable(int order) {
        this.order = order;
        this.lastKnotes = new LimitedQueue<>(order);
        this.markov = new LimitedQueue<>(order);

        int k = 0;
        while (k++ < order) //add k dimensions for model
            markov.add(new MarkovState());
    }

    //use the mxml parser and note/measure data objects...just as a quick way to sample other docs if we want to build a stronger model
    public void trainFile(String fileName) {

    }

    public void trainPhrase(List<String> phrase) {
        for (String note : phrase)
            trainNote(note);
    }

    public void trainNote(String currentPitch) {
        statesCounter++;

        if (lastKnotes.size() == 0) {
            lastKnotes.add(currentPitch);
            return;
        }

        updateKOrderLayers(currentPitch);
        lastKnotes.add(currentPitch);
    }

    public String pickNote() {
        return null;
    }

    private void updateKOrderLayers(String currentPitch) {
        for (int i = 0; i < lastKnotes.size(); i++)
            markov.get(i).updateLayer(currentPitch, lastKnotes.get(i));
    }

    private int getRand(int i) {
        return rand.nextInt(i);
    }

    public void printHistogram() {
        for (int k = 0; k < order; k++) {
            System.out.println("-----------------------------------------");
            System.out.println("              order: "+ k +"               ");
            markov.get(k).printStateHistogram();
            System.out.println("-----------------------------------------");
        }
    }

    public void printProbMatrix() {
        for (int k = 0; k < order; k++) {
            System.out.println("-----------------------------------------");
            System.out.println("              order: "+ k +"               ");
            markov.get(k).printStateProbMatrix();
            System.out.println("-----------------------------------------");
        }
    }
}