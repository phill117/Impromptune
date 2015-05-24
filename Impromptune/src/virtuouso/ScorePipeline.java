package virtuouso;

import utils.Pair;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by ben on 4/26/2015.
 */
public class ScorePipeline {

    private static int degreeWeight[][] ={{3,  3,    3,    3,    3,   3,    3}, //tonic to ...
                                        {0,  3,    0,    0,    4,   0,    1}, //supertonic to ...
                                        {0,  0,    2,    4,    9,   4,    0}, //mediant to ...
                                        {4,  2,    0,    2,    2,   0,    1}, //etc...
                                        {5,  0,    0,    0,    3,   2,    0},
                                        {0,  5,    0,    3,    0,   2,    0},
                                        {9,  0,    0,    0,    0,   0,    1}};

    public static int getDegreeWeight(int i, int j) {
        return degreeWeight[i][j];
    }

    //this should calculate the decision weighting for a degree with respect to the probable tones
    public static double calcScore(Degree degree, HashMap<Degree, Double> dist) {
        if (degree == null)
            return 0.0;

        double max = 0.0;
        Iterator it = dist.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Degree from = (Degree)pair.getKey();
            double score = (double) pair.getValue() * (double) ScorePipeline.degreeWeight[from.toInt()][degree.toInt()];

            if (score > max)
                max = score;
        }

        return max;
    }

    public static HashMap<Degree, Double> scorify(Degree degree, HashMap<Degree, Double> degrees) {
        Iterator it = degrees.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Degree d = (Degree)pair.getKey();
//            degrees.put(d, (Double)pair.getValue() * degreeWeight[degree.toInt()][d.toInt()] * 2);
            degrees.put(d, calcScore(d, degrees));
        }

        return degrees;
    }

    public static Pair<String, Integer> getMaxPair(List<Pair<String, Integer>> chords) {
//        if (chords == null || chords.size() == 0) return null;

        Pair <String, Integer> max = chords.get(0);
        for (Pair<String, Integer> p : chords)
            if (p.u > max.u)
                max = p;

        return max;
    }

    public static String getMaxVote(Map<String, Integer> ballot) {
        Iterator it = ballot.entrySet().iterator();

        Integer value = 0;
        String max = null;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();

            if (value < (Integer)pair.getValue()) {
                max = (String)pair.getKey();
                value = (Integer)pair.getValue();
            }
        }

        return max;
    }

    public static Pair<Degree, Double> getMaxWeight(HashMap<Degree, Double> distribution) {
//        HashMap<Degree, Double> degreeDist = new HashMap<>();
        Iterator it = distribution.entrySet().iterator();

        Double value = new Double(0);
        Pair<Degree, Double> max = null;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();

            if (Double.valueOf(value) < (Double)pair.getValue()) {
                max = new Pair(pair.getKey(), pair.getValue());
                value = (Double)pair.getValue();
            }
        }

        return max;
    }

    //just a wrapper for above
    public static Degree getMaxWeightDegree(HashMap<Degree, Double> distribution) {
        return getMaxWeight(distribution).t;
    }

    private enum WeightType{Chord, StrongBeat, NeighborTone, PassingTone, Root, Inversion}

    //this should be our generic hook for different weight schemes, different weighting for choosing likely chord progression than for picking phrase notes
    int heuristicCompare(WeightType type) { //needs more parameters of course, just a sketch of using different comparisons for choosing notes
        switch (type) {
            case Chord:
                break;
            case StrongBeat:
                break;
            case NeighborTone:
                break;
            case PassingTone:
                break;
            case Root:
                break;
            case Inversion:
                break;
        }

        return -1;
    }
}
