package virtuouso;

import utils.LimitedQueue;
import utils.MersenneTwisterFast;
import utils.Pair;

import java.util.ArrayList;
import java.util.LinkedList;


/**
 * Created by ben on 4/3/2015.
 */
public class ToneTransitionTable {

    public static void main (String args[]) {
        ToneTransitionTable ttt = new ToneTransitionTable(2);

        for (int i = 0; i < 1444; i++) {
            ttt.generateNextState(BlackMagicka.noteIndexToString(ttt.getRand(12)));
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

        int i = 0;
        while (i++ < order) //add k dimensions for model
            markov.add(new MarkovState());
    }

    public void generateNextState(String currentPitch) {
        statesCounter++;

        if (lastKnotes.size() == 0) {
            lastKnotes.add(currentPitch);
            return;
        }

        updateKOrderLayers(currentPitch);
        lastKnotes.add(currentPitch);
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
            markov.get(k).printStateHistogram();
            System.out.println("-----------------------------------------");
        }
    }

    public void printProbMatrix() {
        for (int k = 0; k < order; k++) {
            System.out.println("-----------------------------------------");
            markov.get(k).printStateProbMatrix();
            System.out.println("-----------------------------------------");
        }
    }
}