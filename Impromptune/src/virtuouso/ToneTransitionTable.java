package virtuouso;

import utils.LimitedQueue;
import utils.MersenneTwisterFast;

import java.util.ArrayList;
import java.util.LinkedList;


/**
 * Created by ben on 4/3/2015.
 */
public class ToneTransitionTable {

    public static void main (String args[]) {
        ToneTransitionTable ttt = new ToneTransitionTable(1);

        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                ttt.counts[i][j] = ttt.rand.nextInt(15);
            }
        }

        double c[][] = ttt.normalize();
        for (int i = 0; i < c.length; i++) {
            for (int j = 0; j < c[0].length; j++) {
                System.out.printf("%.2f,", c[i][j]);
            }
            System.out.printf("\n");
        }
    }

    private double probMatrix[][] = {};
    public int counts[][] = {};

    private PitchAxis axis = null;

    MersenneTwisterFast rand = new MersenneTwisterFast();

    private int order; //default
    private int statesCounter = 0;

    private ArrayList<long [][]> markov = null;
    private LinkedList<String> lastKnotes = null;

    public ToneTransitionTable(int order) {
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