package virtuouso;

/**
 * Created by ben on 4/5/2015.
 */
public class MarkovState {
    private PitchAxis pitchAxis;
    private double probMatrix[][];
    private int histogram[][];

    public MarkovState() {
        this.histogram = new int [12][12];
        this.probMatrix = new double [12][12];
        this.pitchAxis = new PitchAxis();
    }

    private void markIndexFound(int i, int j) {
        histogram[i][j]++;
    }

    public void updateLayer(String currentPitch, String lastPitch) {
        markIndexFound(pitchAxis.getIndex(currentPitch), pitchAxis.getIndex(lastPitch));
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
}
