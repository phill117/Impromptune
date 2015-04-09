package virtuouso;

import data_objects.Beat;
import data_objects.MetaData;
import data_objects.Note;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
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

        SAXParser mxp;
        try {
            mxp = SAXParserFactory.newInstance().newSAXParser();
        }catch(Exception e){
            System.out.println("Could not make parser");
            e.printStackTrace();
            return;
        }


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    System.out.println("started");
                    DefaultHandler handler = new MXMLContentHandler();
                    //InputSource inputSource = new InputSource(new FileReader((file)));
                    //      THIS IS A TEMP INPUT SOURCE
                    InputSource inputSource = new InputSource(getClass().getClassLoader().getResourceAsStream("gen_settings/MozartPianoSonata.xml"));
                    mxp.parse(inputSource, handler);

                    //analyze
                    MetaData data = MetaData.getInstance();
                    ArrayList<ArrayList<Note>> beats = data.getBeatList();

                    VirtuosoAgent agent = new VirtuosoAgent();
                    agent.getInstance().pickNote(MetaData.getInstance().getNoteList().get(0));

                } catch (SAXException e) {
                    System.out.println("SAX");
                    e.printStackTrace();
                } catch (IOException e) {
                    System.out.println("IO");
                    e.printStackTrace();
                }
            }
        }).start();



    }

    private VirtuosoAgent rationalAgent;
    private ToneTransitionTable model;
    private String keyTonic;
    private String mode;

    private VirtuosoAgent() {
        Pair<String, String> keySig = new Pair<>(keyTonic, mode);
        model = new ToneTransitionTable(1, keySig);
        model.trainPiece(MetaData.getInstance().getBeatList());


    }

    public VirtuosoAgent getInstance() {
        if (rationalAgent == null)
            rationalAgent = new VirtuosoAgent();
        return rationalAgent;
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

    void pickNote (Note note) {
        Beat beat = new Beat();
        beat.addNote(note);
        //model.pickNote(beat);
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
                        actualPossibilities.put(d, possibilities.get((Degree)d));
                    break;
                case Supertonic: // 2 5 7
                    for (Degree d : possibilities.keySet()) {
                        int i = d.toInt();
                        if (i != 2 && i != 5 && i != 7) notPossibilities.put(d, possibilities.get((Degree) d));
                        else actualPossibilities.put(d, possibilities.get((Degree) d));
                    }
                    break;
                case Mediant: // 3 4 6
                    for (Degree d : possibilities.keySet()) {
                        int i = d.toInt();
                        if (i != 3 && i != 4 && i != 6) notPossibilities.put(d, possibilities.get((Degree) d));
                        else actualPossibilities.put(d, possibilities.get((Degree) d));
                    }
                    break;
                case Subdominant: // 1 2 4 5 7
                    for (Degree d : possibilities.keySet()) {
                        int i = d.toInt();
                        if (i != 2 && i != 5 && i != 7 && i != 1 && i != 4) notPossibilities.put(d, possibilities.get((Degree) d));
                        else actualPossibilities.put(d, possibilities.get((Degree) d));
                    }
                    break;
                case Dominant: // 1 5 6
                    for (Degree d : possibilities.keySet()) {
                        int i = d.toInt();
                        if (i != 1 && i != 5 && i != 6) notPossibilities.put(d, possibilities.get((Degree) d));
                        else actualPossibilities.put(d, possibilities.get((Degree) d));
                    }
                    break;
                case Submediant: // 2 4 6
                    for (Degree d : possibilities.keySet()) {
                        int i = d.toInt();
                        if (i != 2 && i != 4 && i != 6) notPossibilities.put(d, possibilities.get((Degree) d));
                        else actualPossibilities.put(d, possibilities.get((Degree) d));
                    }
                    break;
                case Leading: // 1 7
                    for (Degree d : possibilities.keySet()) {
                        int i = d.toInt();
                        if (i != 1 && i != 7) notPossibilities.put(d, possibilities.get((Degree) d));
                        else actualPossibilities.put(d, possibilities.get((Degree) d));
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

            Random random = new Random();
            Degree[] degrees = new Degree[0];
            actualPossibilities.keySet().toArray(degrees);
            return degrees[random.nextInt(degrees.length)];
        } else { //only not possibilities has elements
            Random random = new Random();
            Degree[] degrees = new Degree[0];
            notPossibilities.keySet().toArray(degrees);
            return degrees[random.nextInt(degrees.length)];
        }

        //will never happen (making the compiler happy)
        return Degree.Tonic;
    }
}