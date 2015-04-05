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

        for (int i = 0; i < 144; i++) {
            ttt.generateNextState(BlackMagicka.noteIndexToString(ttt.rand.nextInt(12)));
        }

        ttt.printHistogram();
        ttt.printProbMatrix();
    }

    private double probMatrix[][] = {};
    public int counts[][] = {};

    private PitchAxis axis = null;

    MersenneTwisterFast rand = new MersenneTwisterFast();

    private int order; //default
    private int statesCounter = 0;

    private ArrayList<int [][]> markov = null;
    private LinkedList<String> lastKnotes = null;

    public ToneTransitionTable(int order) {
        this.axis = new PitchAxis();
        this.order = order;
        this.counts = new int [12][12];
        this.probMatrix = new double[12][12];
        this.lastKnotes = new LimitedQueue<>(order);
        this.markov = new ArrayList<>();

        int i = 0;
        while (i++ < order) //add k dimensions for model
            markov.add(counts);

    }

    public void generateNextState(String currentPitch) {
        statesCounter++;

        String lastPitch = lastKnotes.peekFirst();

        if (lastKnotes.size() == 0) {
            lastKnotes.add(currentPitch);
            return;
        }

        updateKOrderLayers(currentPitch);
        lastKnotes.add(currentPitch);
    }

    private void updateKOrderLayers(String currentPitch) {
        for (int i = 0; i < lastKnotes.size(); i++)
            markIndexFound(axis.getIndex(lastKnotes.get(i)),
                    axis.getIndex(currentPitch),
                    i);
    }

    private void markIndexFound(int i, int j, int k) { //i = m, j = n, k = constant
        markov.get(k)[i][j]++;
    }

    public void normalize() {

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

            for (int j = 0; j < 12; j++)
                val += probMatrix[i][j];

//            System.out.println(val);

        }

//        return probMatrix;
    }

    public void clear() {
        this.probMatrix = new double[order][order];
    }

    public void printHistogram() {
        System.out.printf("    ");
        String [] pitches = axis.getPitchAxis();
        for (String s : pitches) {
            if (s.length() == 2)
                System.out.printf(" %s", s);
            else
                System.out.printf(" %s ", s);
        }

        System.out.printf("\n");

        for (int i = 0; i < counts.length; i++) {
            if (pitches[i].length() == 2)
                System.out.printf(" %s ", pitches[i]);
            else
                System.out.printf(" %s  ", pitches[i]);

            for (int j = 0; j < counts[0].length; j++)
                System.out.printf(" %d,", counts[i][j]);

            System.out.printf("\n");
        }
    }

    public void printProbMatrix() {
        normalize();

        System.out.printf("    ");
        String [] pitches = axis.getPitchAxis();
        for (String s : pitches) {
            if (s.length() == 2)
                System.out.printf(" %s  ", s);
            else
                System.out.printf(" %s   ", s);
        }

        System.out.printf("\n");

        for (int i = 0; i < probMatrix.length; i++) {
            if (pitches[i].length() == 2)
                System.out.printf(" %s  ", pitches[i]);
            else
                System.out.printf(" %s   ", pitches[i]);

            for (int j = 0; j < probMatrix[0].length; j++) {
                System.out.printf("%.2f,", probMatrix[i][j]);
            }
            System.out.printf("\n");
        }
    }
}