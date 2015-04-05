package virtuouso;

import utils.LimitedQueue;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

/**
 * Created by ben on 4/4/2015.
 */
public class RhythmTransitionTable {
    private double probMatrix[][] = {};
    public int counts[][] = {};

    private PitchAxis axis = null;

    Random rand = new Random();

    private int order; //default
    private int statesCounter = 0;

    private ArrayList<long [][]> markov = null;
    private LinkedList<String> lastKnotes = null;

    public RhythmTransitionTable(int order) {
        this.axis = new PitchAxis();
        this.order = order;
        this.counts = new int [12][12];
        this.probMatrix = new double[12][12];
        this.lastKnotes = new LimitedQueue<>(order);
        this.markov = new ArrayList<>();

        int i = 0;
//        do {
//            markov.add(counts);
//        } while (order - i++ > 0); //add k dimensions for model
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

    public double[][] normalize() {

        for (int i = 0; i < 12; i++) {
            int sum = 0;

            for (int j = 0; j < 12; j++) {
                sum += counts[i][j];
            }

            double likelihood = 1.0 / sum;

            for (int j = 0; j < 12; j++) {
                probMatrix[i][j] = likelihood * counts[i][j];
            }
        }

        for (int i = 0; i < 12; i++) {
            double val = 0.0;

            for (int j = 0; j < 12; j++) {
                val += probMatrix[i][j];
            }

            System.out.println(val);

        }

        return probMatrix;
    }

    public void clear() {
        this.probMatrix = new double[order][order];
    }
}
