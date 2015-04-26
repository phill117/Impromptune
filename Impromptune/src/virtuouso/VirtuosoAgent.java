package virtuouso;

import data_objects.Beat;
import data_objects.Measure;
import data_objects.MetaData;
import data_objects.Note;

import utils.LimitedQueue;
import utils.Pair;

import java.io.File;
import java.util.*;

/**
 * Created by ben on 3/29/2015.
 */

//the rational decision making agent
public class VirtuosoAgent {

    private ToneTransitionTable model;
    private String keyTonic;
    private String mode;
    private Set<String> chordProgression;
    private MetaData data;
    private File currentFile;
    private int degreeWeight[][] = new int[][] {{3,  3,    3,    3,    3,   3,    3}, //tonic to ...
                                                {0,  3,    0,    0,    4,   0,    1}, //supertonic to ...
                                                {0,  0,    2,    4,    9,   4,    0}, //mediant to ...
                                                {4,  2,    0,    2,    2,   0,    1}, //etc...
                                                {5,  0,    0,    0,    3,   2,    0},
                                                {0,  5,    0,    3,    0,   2,    0},
                                                {9,  0,    0,    0,    0,   0,    1}};

    boolean sharp = true;

    public VirtuosoAgent(File file) {

        data = new MetaData(file);

        if (MetaData.getInstance().isMajor())
            mode = "major";
        else
            mode = "minor";

        keyTonic = getKeyTonic(mode);

        System.out.println("mode: " + mode + ", tonic: " + keyTonic);
        Pair<String, String> keySig = new Pair<>(keyTonic, mode);

        if (MetaData.getInstance().getFifthType().equals("sharp")) {
//            keyTonic = getFifth(data.getSharps(),'#' );
            model = new ToneTransitionTable(2, keySig, '#');
        } else {
            sharp = false;
//            keyTonic = getFifth(data.getSharps(),'b', );
            model = new ToneTransitionTable(2, keySig, 'b');
        }

        model.trainFile(file);
//        model.trainPiece(data.getBeatList());
//        chordProgression = new HashSet<>();
//        possibleChords = new ArrayList<>();
        currentFile = file;
//        keyTonic = data.getSharps()

    }

    public ArrayList<String> getGeneratedTones() {
        ArrayList<String> tones = new ArrayList<>();
        ArrayList<ArrayList<Note>> beats = data.getBeatList();

        for (ArrayList<Note> notes : beats) {
            for (Note n : notes) {
                String s = chooseChord(n);
                if (s == null) //not in key
                    continue;
                tones.add(s);
//                System.out.print(s);
            }
        }

        System.out.println("chosen roots " + tones);
        return tones;
    }

    private Pair<String, Integer> getMaxPair(List<Pair<String, Integer>> chords) {
//        if (chords == null || chords.size() == 0) return null;

        Pair <String, Integer> max = chords.get(0);
        for (Pair<String, Integer> p : chords)
            if (p.u > max.u)
                max = p;

        return max;
    }

    public void buildChordProgression() {
        ArrayList<ArrayList<Note>> beats = new MetaData(currentFile).getBeatList();

//        VirtuosoAgent agent = new VirtuosoAgent();
        List<String> possibleChords = new ArrayList<>();
        for (ArrayList<Note> notes : beats) {
            for (Note n : notes) {
                String s = pickNote(n);
                if (s == null) //not in key
                    continue;
                possibleChords.add(s);
            }
        }
//        chordProgression = buildChordProgression();

        Set<String> tmp =  new HashSet<String>(possibleChords);
        List<Pair<String, Integer>> prog = new LimitedQueue<>(7);

        for (String s : tmp) {
            int i = Collections.frequency(possibleChords, s);
            prog.add(new Pair<>(s, i));

            System.out.println(Collections.frequency(possibleChords, s) + " " + s);
        }

        Set<String> ret = new HashSet();

        for (int i = 0; i < prog.size() && i < 4; i++) {
            Pair<String, Integer> p = getMaxPair(prog);
            ret.add(p.t);
            prog.remove(p);
        }

        chordProgression = ret;
        System.out.println("chord progression chosen: " + ret);
    }

    private void addToChordProgression(String tone) {
//        possibleChords.add(tone);
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

    private String getMaxVote(Map<String, Integer> ballot) {
        Iterator it = ballot.entrySet().iterator();

        Integer value = new Integer(0);
        String max = null;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();

            if (Integer.valueOf(value) < (Integer)pair.getValue()) {
                max = (String)pair.getKey();
                value = (Integer)pair.getValue();
            }
        }

        return max;
    }

//    BlackMagicka.getDegreeIndex(keyTonic, mode, note).toInt()
    //get tone with pairObject.t, get register with pairObject.u
    private String chooseChord(Note note) {
        if (note == null) return null;
        Pair<String, Integer> chord = null;

        String [] str = chordProgression.toArray(new String[0]);
        HashMap<String, Integer> ballot = new HashMap<>();
        for (int i = 0; i < chordProgression.size(); i++) {
            String noteVote = str[model.getRand(chordProgression.size())];
            Integer count = ballot.get(noteVote);

//            if (count != null &&
//                    BlackMagicka.noteIn7thChord(note.toString(),keyTonic,  mode) == true
//                    && BlackMagicka.noteInScale(noteVote, keyTonic, mode) == true) {
//                int m = BlackMagicka.getDegreeIndex(keyTonic, mode, note.toString()).toInt();
//                int n = BlackMagicka.getDegreeIndex(keyTonic, mode, noteVote).toInt();
//                count *= degreeWeight[m][n];
////                System.out.print(count);
//            }

            if (count == null) {
                ballot.put(noteVote, 1);
            } else {
                ballot.put(noteVote, count + 1);
            }
        }

        return getMaxVote(ballot);
    }


    private HashMap<Degree, Double> scorify(Degree degree, HashMap<Degree, Double> degrees) {
        Iterator it = degrees.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Degree d = (Degree)pair.getKey();
//            degrees.put(d, (Double)pair.getValue() * degreeWeight[degree.toInt()][d.toInt()] * 2);
            degrees.put(d, calcScore(d, degrees));
        }

        return degrees;
    }

    private String pickNote (Note note) {
        Beat beat = new Beat();
        beat.addNote(note);
        //model.pickNote(beat);
        HashMap<Degree, Double> hash = model.possibleNotestoDegree(model.generateBeatChoices(beat));

        Degree d = model.getDegree(note);

        if (d == null)
            return null;

        hash = scorify(d, hash);
        System.out.println("degree: " + d.toInt() + " scored " + calcScore(d, hash));

        Degree n = transition(d, hash);

        return degreeTone(n);
    }

    private String degreeTone(Degree degree) {
        if (degree == null)
            return null;
        return BlackMagicka.pickIthNote(keyTonic, degree.toInt(), sharp);
    }

    //this should calculate the decision weighting for a degree with respect to the probable tones
    private double calcScore(Degree degree, HashMap<Degree, Double> dist) {
        if (degree == null)
            return 0.0;

        double score = 0.0;
        double max = 0.0;
        Iterator it = dist.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Degree from = (Degree)pair.getKey();
            score = (double) pair.getValue() * (double) degreeWeight[from.toInt()][degree.toInt()] * 2;

            if (score > max)
                max = score;
        }

        return max;
    }

    private Pair<Degree, Double> getMaxWeight(HashMap<Degree, Double> distribution) {
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
    private Degree getMaxWeightDegree(HashMap<Degree, Double> distribution) {
        return getMaxWeight(distribution).t;
    }

    /**
     * returns a degree that is the root of the next chord in the progression
     * NOTE: look below for an overloaded method
     * @param degree : the degree to transition away from
     * @param order : the order (or memory) of the markov model
     * @return : the next Degree (effectively the next chord) in the progression
     */

    private Degree transition(Degree degree, Map<Degree, Double> possibilities, int order){

        // there is only one compatible chord, choose that one
        if(possibilities.size() == 1) for(Degree d : possibilities.keySet()) return d;
        HashMap<Degree, Double> notPossibilities = new HashMap<>();
        HashMap<Degree, Double> actualPossibilities = new HashMap<>();

        //separate the set of given next chords into possible (from the graph) and not possible
        //for both major and minor
        if(MetaData.getInstance().isMajor()) {
            switch (degree) {
                default:
                case Tonic: //all
                    for (Degree d : possibilities.keySet())
                        actualPossibilities.put(d, possibilities.get(d));
                    break;
                case Supertonic: // 2 5 7
                    for (Degree d : possibilities.keySet()) {
                        int i = d.toInt();
                        if (i != 2 && i != 5 && i != 7) notPossibilities.put(d, possibilities.get(d));
                        else actualPossibilities.put(d, possibilities.get(d));
                    }
                    break;
                case Mediant: // 3 4 6
                    for (Degree d : possibilities.keySet()) {
                        int i = d.toInt();
                        if (i != 3 && i != 4 && i != 6) notPossibilities.put(d, possibilities.get(d));
                        else actualPossibilities.put(d, possibilities.get(d));
                    }
                    break;
                case Subdominant: // 1 2 4 5 7
                    for (Degree d : possibilities.keySet()) {
                        int i = d.toInt();
                        if (i != 2 && i != 5 && i != 7 && i != 1 && i != 4) notPossibilities.put(d, possibilities.get(d));
                        else actualPossibilities.put(d, possibilities.get(d));
                    }
                    break;
                case Dominant: // 1 5 6
                    for (Degree d : possibilities.keySet()) {
                        int i = d.toInt();
                        if (i != 1 && i != 5 && i != 6) notPossibilities.put(d, possibilities.get(d));
                        else actualPossibilities.put(d, possibilities.get(d));
                    }
                    break;
                case Submediant: // 2 4 6
                    for (Degree d : possibilities.keySet()) {
                        int i = d.toInt();
                        if (i != 2 && i != 4 && i != 6) notPossibilities.put(d, possibilities.get(d));
                        else actualPossibilities.put(d, possibilities.get(d));
                    }
                    break;
                case Leading: // 1 7
                    for (Degree d : possibilities.keySet()) {
                        int i = d.toInt();
                        if (i != 1 && i != 7) notPossibilities.put(d, possibilities.get(d));
                        else actualPossibilities.put(d, possibilities.get(d));
                    }
                    break;
            }
        }

        //return a choice based on the resulting sets
        return getChoice(actualPossibilities, notPossibilities);
    }

    /**
     * Double argument wrapper for method above
     */
    public Degree transition(Degree degree, Map<Degree, Double> possibilities){
        return transition(degree, possibilities, 1);
    }

    private Degree getChoice(HashMap<Degree, Double> actualPossibilities, HashMap<Degree, Double> notPossibilities){
        if(actualPossibilities.size() == 1){ for(Degree d : actualPossibilities.keySet()) return d; }
        else if(actualPossibilities.size() > 0) {

//            Random random = new Random();
//            Degree[] degrees = new Degree[0];

//            actualPossibilities.keySet().toArray(degrees);
//            return degrees[random.nextInt(degrees.length)];
            return getMaxWeightDegree(actualPossibilities);
        } else { //only not possibilities has elements
//            Random random = new Random();
//            Degree[] degrees = new Degree[0];
//            notPossibilities.keySet().toArray(degrees);
            return getMaxWeightDegree(notPossibilities);
        }

        //will never happen (making the compiler happy)
        return Degree.Tonic;
    }

    /**
     * This method was finished by Sean Phillips @ 9:55 PM EST with the days left before the Final demo.
     * He was very pleased with the result, for he had remade the Measure Class structure.
     * HE BELIEVES THAT THIS GREAT METHOD NEEDS TO BE SHARED WITH THE WHOLE HUMAN RACE (at least some of it).
     * @param chordProgTones - The list of string representing notes to be added to the existing music.
     */

    public void addBackToMusic(ArrayList<String> chordProgTones) {
        addBackToMusic(chordProgTones,MetaData.getInstance().getPartCount() - 1);
    }

    public void addBackToMusic(ArrayList<String> chordProgTones, int part){
        MetaData metaData = MetaData.getInstance();
        int divsPerMeasure = metaData.getDivisionsPerMeasure();
        ArrayList<Measure> measures = metaData.getMeasures();
        int k = 0;
        int currentDuration = 0;
        int rollOver = 0;
        for(Measure measure : measures){
            currentDuration = 0;
            int duration = 0;
            for(; k < chordProgTones.size(); k++) {

                if(rollOver != 0){
                    duration = 1 * metaData.getDivisions();//TODO - dynamically change when adding rhythme
                    measure.addNoteToPart(Note.makeNote(chordProgTones.get(k), 3, duration), part);
                    currentDuration += rollOver;
                    rollOver = 0;
                    if(currentDuration == divsPerMeasure) break;
                    if(currentDuration > divsPerMeasure){
                        rollOver = currentDuration - divsPerMeasure;
                        break;
                    }
                    continue;
                }

                duration = 1 * metaData.getDivisions();//TODO - dynamically change when adding rhythme
                if(currentDuration == divsPerMeasure) break;

                if(currentDuration + duration > divsPerMeasure){
                    rollOver = currentDuration + duration - divsPerMeasure;
                }

                measure.addNoteToPart(Note.makeNote(chordProgTones.get(k), 3, duration), part);
                currentDuration += duration;
            }
        }

        for(Measure m : measures){
            System.out.println("NEW MEASURE");
            for(Note n : m.getPart(part)){
                System.out.println(n.getPitch());
            }
        }

    }











    static int[][] fifthTable =
            // natural, flat, sharp
            {{-1,-8,6},//f
            {0,-7,7},//c
            {1,-6,8},//g
            {2,-5,9},//d
            {3,-4,10},//a
            {4,-3,11},//e
            {5,-2,12},//b
            };

    static String getFifth(int fifth, char a, String mode) {
        int base;
        int mod = 0;
        String root = null;
        switch(fifth) {
            case 0:
                root = "F";
//                base = 0;
                break;
            case 1:
                root = "C";
                break;
            case 2:
                root = "G";
                break;
            case 3:
                root = "D";
                break;
            case 4:
                root = "A";
                break;
            case 5:
                root = "E";
                break;
            case 6:
                root = "B";
                break;
        }

        if(a == 'b')mod = 1;
        if(a == '#')mod = 2;

        int fifths = fifthTable[fifth][mod];
        if(mode.equals("minor")) fifths -= 3;
        System.out.println("fifths: " + fifths + " " + fifthTable[fifth][mod]);
        return root;
    }

    private String getKeyTonic(String mode) {
        MetaData data = MetaData.getInstance();
        if (data == null) return null;
        System.out.println("finding fifths " + data.getSharps());
        String tonic = null;

        if (mode.equals("major")) {
            if (data.getFifthType().equals("sharp")) {
                switch(data.getSharps()){
                    case 0:
                        tonic = "C";
                        break;
                    case 1:
                        tonic = "G";
                        break;
                    case 2:
                        tonic = "D";
                        break;
                    case 3:
                        tonic = "A";
                        break;
                    case 4:
                        tonic = "E";
                        break;
                    case 5:
                        tonic = "B";
                        break;
                    case 6:
                        tonic = "F#";
                        break;
                }
            } else {
                switch(data.getSharps()){
                    case 0:
                        tonic = "C";
                        break;
                    case -1:
                        tonic = "F";
                        break;
                    case -2:
                        tonic = "Bb";
                        break;
                    case -3:
                        tonic = "Eb";
                        break;
                    case -4:
                        tonic = "Ab";
                        break;
                    case -5:
                        tonic = "Db";
                        break;
                    case -6:
                        tonic = "Gb";
                        break;
                }
            }
        } else if (mode.equals("minor")) {
            if (data.getFifthType().equals("sharp")) {

                switch(data.getSharps()){
                    case 0:
                        tonic = "A";
                        break;
                    case 1:
                        tonic = "E";
                        break;
                    case 2:
                        tonic = "B";
                        break;
                    case 3:
                        tonic = "F";
                        break;
                    case 4:
                        tonic = "C";
                        break;
                    case 5:
                        tonic = "G";
                        break;
                    case 6:
                        tonic = "D#";
                        break;
                }
            } else {
                switch(data.getSharps()){
                    case 0:
                        tonic = "A";
                        break;
                    case 1:
                        tonic = "G";
                        break;
                    case 2:
                        tonic = "C";
                        break;
                    case 3:
                        tonic = "F";
                        break;
                    case 4:
                        tonic = "Bb";
                        break;
                    case 5:
                        tonic = "Eb";
                        break;
                    case 6:
                        tonic = "Ab";
                        break;
                }
            }
        }

        return tonic;
    }
}