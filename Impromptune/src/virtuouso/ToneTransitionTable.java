package virtuouso;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by ben on 4/3/2015.
 */
public class ToneTransitionTable {

    private double probMatrix[][] = {};
    private long counts[][] = {};

    private PitchAxis axis = null;

    private int order = 1; //default
    private int statesCounter = 0;

    private ArrayList<long [][]> markov = null;
    private LinkedList<String> lastKnotes = null;

    public ToneTransitionTable(int order) {
        this.axis = new PitchAxis();
        this.order = order;
        this.counts = new long [order][order];
        this.lastKnotes = new LimitedQueue<>(order);
        this.markov = new ArrayList<>();

        int i = 0;
        do {
            markov.add(counts);
        } while (order - i++ > 0); //add k dimensions for model
    }

    public void generateNextState(String currentPitch) {
        statesCounter++;

        String lastPitch = lastKnotes.peekFirst();

        if (lastKnotes.size() == 0)
            return;

//        markIndexFound(axis.getIndex(lastPitch), axis.getIndex(currentPitch), 0);
        updateKOrderLayers(currentPitch);
        lastKnotes.add(currentPitch);
    }

    private void updateKOrderLayers(String currentPitch) {
        for (int i = 0; i < order - 1; i++) //skip first one
            markIndexFound(axis.getIndex(lastKnotes.get(i)),
                    axis.getIndex(lastKnotes.get(i + 1)), i);
    }

    private void markIndexFound(int i, int j, int k) { //i = m, j = n, k = constant
        markov.get(k)[i][j]++;
    }

    public void clear() {
        this.probMatrix = new double[order][order];
    }
}