package virtuouso;

import data_objects.Beat;
import data_objects.MetaData;
import data_objects.Note;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import utils.LimitedQueue;
import utils.Pair;
import xml_parser.MXMLContentHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.*;

/**
 * Created by ben on 3/29/2015.
 */

//the rational decision making agent
public class VirtuosoAgent {

    public static void main(String args[]) {

//        System.out.println(BlackMagicka.pickDominant("A"));
        VirtuosoAgent agent = new VirtuosoAgent();
        agent.build("gen_settings/MozartPianoSonata.xml");
        System.out.println(agent.chordProgression);

        MetaData data = MetaData.getInstance();
        ArrayList<ArrayList<Note>> beats = data.getBeatList();

//        VirtuosoAgent agent = new VirtuosoAgent();
        List<String> possibleChords = new ArrayList<>();
        for (ArrayList<Note> notes : beats) {
            for (Note n : notes) {
                String s = agent.getInstance().pickNote(n);
                if (s == null) //not in key
                    continue;
                possibleChords.add(s);
            }
        }

        agent.chordProgression = agent.buildChordProgression(possibleChords);
        System.out.println(agent.chordProgression);
    }

    private VirtuosoAgent rationalAgent;
    private ToneTransitionTable model;
    private String keyTonic;
    private String mode;
    private Set<String> chordProgression;


    private VirtuosoAgent() {
        keyTonic = "C";
        mode = "major";
        Pair<String, String> keySig = new Pair<>(keyTonic, mode);
        model = new ToneTransitionTable(2, keySig);
        model.trainPiece(MetaData.getInstance().getBeatList());
//        chordProgression = new HashSet<>();
//        possibleChords = new ArrayList<>();

    }

    public VirtuosoAgent getInstance() {
        if (rationalAgent == null)
            rationalAgent = new VirtuosoAgent();
        return rationalAgent;
    }

    public Pair<String, Integer> getMaxPair(List<Pair<String, Integer>> chords) {
        Pair <String, Integer> max = chords.get(0);
        for (Pair<String, Integer> p : chords)
            if (p.u > max.u)
                max = p;

        return max;
    }

    public Set<String> buildChordProgression(List<String> chords) {
        Set<String> tmp =  new HashSet<String>(chords);
        List<Pair<String, Integer>> prog = new LimitedQueue<>(7);

        for (String s : tmp) {
            int i = Collections.frequency(chords, s);
            prog.add(new Pair<>(s, i));

            System.out.println(Collections.frequency(chords, s) + " " + s);
        }

        Set<String> ret = new HashSet();
        for (int i = 0; i < 3; i++) {
            Pair<String, Integer> p = getMaxPair(prog);
            ret.add(p.t);
            prog.remove(p);
        }

        return ret;
    }

    private void addToChordProgression(String tone) {
//        possibleChords.add(tone);
    }

    private enum WeightType{Chord, Beat, PassingTone, Root, Inversion}
    //this should be our generic hook for different weight schemes, different weighting for choosing likely chord progression than for picking phrase notes
    int heuristicCompare(WeightType type) { //needs more parameters of course, just a sketch of using different comparisons for choosing notes
        switch (type) {
            case Chord:
                break;
            case Beat:
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

//    public StateNode minCost() {
//        StateNode buff = fringe.removeFirst();
//
//        for (StateNode node : fringe) {
//            if ((node.compareTo(buff)) < 0) {
//                // if (node.heuristicCompare(buff) < 0) {
//                buff = node;
//            }
//        }
//
//        // fringe.remove(buff);
//        return buff;
//    }
//
//    public StateNode DLS(int limit) {
//
//        while (!fringe.isEmpty()) {
//            ArrayDeque<StateNode> successors = generateStateSet(minCost(), limit);
//
//            if (successors.isEmpty() || successors.getLast().depth > limit) //failed, go deeper
//                return null;
//
//            if (goalPath(successors))
//                return successors.getLast();
//
//            //for more blahblah, check if visited...
//            for (StateNode node : successors)
//                fringe.addLast(node);
//        }
//
//        return null;
//    }

//    public static void AStarSearch(int maxDepth) {
//        for (int i = 0; i < maxDepth; i++) {
////            StateNode result = DLS(i);
//            // System.out.println("limit hit -------------------");
//        }
//    }

//    viterbi path
//    public void viterbiPath() {
//
//    }
//
//    void gibbsSampling() {
//
//    }

    String pickNote (Note note) {
        Beat beat = new Beat();
        beat.addNote(note);
        //model.pickNote(beat);
        HashMap<Degree, Double> hash = model.possibleNotestoDegree(model.pickNote(beat));
        Degree d = model.getDegree(note);
        Degree n = transition(d, hash);
//        System.out.println(degreeTone(n));
        return degreeTone(n);
    }

    String degreeTone(Degree degree) {
        if (degree == null)
            return null;
        return BlackMagicka.pickIthNote(keyTonic, degree.toInt());
    }

    //this should calculate the decision weighting for a degree with respect to the probable tones
    double calcScore(Degree degree, HashMap<Degree, Double> dist) {
        return 0.0;
    }

    Pair<Degree, Double> getMaxWeight(HashMap<Degree, Double> distribution) {
        HashMap<Degree, Double> degreeDist = new HashMap<>();
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

    Degree getMaxWeightDegree(HashMap<Degree, Double> distribution) {
        return getMaxWeight(distribution).t;
    }

    /*******
     * Moved from MetaData:
     *******/
    //ignore this for now
    double chordFrequencies[][] =
            //1     2       3       4       5       6       7
            {{1/7,  1/7,    1/7,    1/7,    1/7,    1/7,    1/7}, //tonic to ...
            {0,    .3,     0,      0,      .6,     0,      .1}, //supertonic to ...
            {0,    0,      .2,     .4,      0,     .4,     0}, //mediant to ...
            {.3,   .2,     0,      .2,      .2,    0,      .1}, //etc...
            {.5,   0,      0,      0,       .3,    .2,     0},
            {0,    .5,     0,      .3,      0,     .2,     0},
            {.9,   0,      0,      0,       0,     0,      .1}};

    /**
     * returns a degree that is the root of the next chord in the progression
     * NOTE: look below for an overloaded method
     * @param degree : the degree to transition away from
     * @param order : the order (or memory) of the markov model
     * @return : the next Degree (effectively the next chord) in the progression
     */

    public Degree transition(Degree degree, Map<Degree, Double> possibilities, int order){

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

            //TODO : make the choice of the actual possible choices weighted

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

    private void build(String fileName) {
        SAXParser mxp;
        try {
            mxp = SAXParserFactory.newInstance().newSAXParser();
        }catch(Exception e){
            System.out.println("Could not make parser");
            e.printStackTrace();
            return;
        }
        try {

            System.out.println("started");
            DefaultHandler handler = new MXMLContentHandler();
            //InputSource inputSource = new InputSource(new FileReader((file)));
            //      THIS IS A TEMP INPUT SOURCE
            InputSource inputSource = new InputSource(getClass().getClassLoader().getResourceAsStream(fileName));
            mxp.parse(inputSource, handler);

            //analyze

        } catch (SAXException e) {
            System.out.println("SAX");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IO");
            e.printStackTrace();
        }
    }
}