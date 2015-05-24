package virtuouso;

import data_objects.Beat;
import data_objects.Note;
import utils.Pair;

/**
 * Created by ben on 4/5/2015.
 */
public class MarkovState {
    //the notes of the model, currently only considers sharp keys
    private PitchAxis pitchAxis;
    //the set of probability vectors
    private double probMatrix[][];
    //note counts
    private int histogram[][];

    public MarkovState(Pair<String, String> keySig, char a) {
        System.out.println("MarkovState keysig: " + keySig.t + " " + keySig.u + " " + a);
        this.histogram = new int [12][12];
        this.probMatrix = new double [12][12];
        this.pitchAxis = new PitchAxis(keySig, a);
    }

    private void markIndexFound(int i, int j) {
        histogram[i][j]++;
    }

    public double getIndexLikeliness(int i, int j) { normalize(); return probMatrix[i][j]; }

    //update THIS order of the model, transition tables build K of these
    public void updateLayer(Beat currentBeat, Beat lastBeat) {
        for (Note curr : currentBeat.getNotes()) {
            for (Note prev : lastBeat.getNotes()) {
                int i = pitchAxis.getIndex(curr.toString());
                int j = pitchAxis.getIndex(prev.toString());
//                System.out.println(i + " " + j);
                if (i >= 0 && j >= 0)
                    markIndexFound(i, j);
            }
        }
    }

    public void printStateHistogram() {

        System.out.printf("    ");
        String[] pitches = pitchAxis.getPitchAxis();
        for (String s : pitches) {
            if (s.length() == 2)
                System.out.printf(" %s", s);
            else
                System.out.printf(" %s ", s);
        }

        System.out.printf(" sums\n");

        for (int i = 0; i < histogram.length; i++) {
            int sum = 0;

            if (pitches[i].length() == 2)
                System.out.printf(" %s ", pitches[i]);
            else
                System.out.printf(" %s  ", pitches[i]);

            for (int j = 0; j < histogram[0].length; j++) {
                System.out.printf(" %d,", histogram[i][j]);
                sum += histogram[i][j];
            }

            System.out.printf(" " + sum + "\n");
        }
    }

    public void printStateProbMatrix() {
        normalize();

        System.out.printf("    ");
        String[] pitches = pitchAxis.getPitchAxis();
        for (String s : pitches) {
            if (s.length() == 2)
                System.out.printf(" %s  ", s);
            else
                System.out.printf(" %s   ", s);
        }

        System.out.printf("\n");

        for (int i = 0; i < probMatrix.length; i++) {
            double sum = 0.0;
            if (pitches[i].length() == 2)
                System.out.printf(" %s  ", pitches[i]);
            else
                System.out.printf(" %s   ", pitches[i]);

            for (int j = 0; j < probMatrix[0].length; j++) {
                System.out.printf("%.2f,", probMatrix[i][j]);
                sum += probMatrix[i][j];
            }

            System.out.printf(" %.2f\n", sum);
        }
    }

    //given the histogram, normalizes rows such that the sum of the row is ~= 1.0
    public void normalize() {

        for (int i = 0; i < 12; i++) {
            int sum = 0;

            for (int j = 0; j < 12; j++) {
                sum += histogram[i][j];
            }

            double likelihood = 1.0; //start with 100%
            if (sum == 0)
                likelihood = 0;
            else
                likelihood /= sum;

            for (int j = 0; j < 12; j++) {
                probMatrix[i][j] = likelihood * histogram[i][j];
            }
        }
    }

    private double sumRow(int i) {
        double val = 0.0;

        for (int j = 0; j < 12; j++)
            val += probMatrix[i][j];

        return val;
    }

    public PitchAxis getPitchAxis() {
        return pitchAxis;
    }
}
