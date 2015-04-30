package virtuouso;

import BeatTemplates.VoiceFactory;
import BeatTemplates.VoiceOctaveRanges;
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
    private String fifthType;

    private int voices;
    boolean sharp = true;
    public VoiceFactory voiceFactory;

    public VirtuosoAgent(File file, String fifthType, int order, int parts, int repetition) {
        voices = parts;
        this.fifthType = fifthType;

        data = new MetaData(file);

        if (MetaData.getInstance().isMajor())
            mode = "major";
        else
            mode = "minor";

        keyTonic = getKeyTonic(mode);

        System.out.println("mode: " + mode + ", tonic: " + keyTonic);
        Pair<String, String> keySig = new Pair<>(keyTonic, mode);

        if (fifthType.equals("sharp")) {
            model = new ToneTransitionTable(order, keySig, '#');
        } else {
            sharp = false;
            model = new ToneTransitionTable(order, keySig, 'b');
        }

        model.trainFile(file);
//        model.trainPiece(data.getBeatList());
        currentFile = file;

        voiceFactory = new VoiceFactory(keyTonic, mode, sharp, repetition);
    }

    public String mostHarmonicReasonation(String tonePlaying, String chosenRoot) {
        String bestChoice = null;

        if (MetaData.getInstance().isMajor()) {
            if (BlackMagicka.noteInScale(tonePlaying, keyTonic, "major", sharp)) {
                for (String tone : BlackMagicka.majorChord(chosenRoot, sharp)) {
                    if (BlackMagicka.noteInChord(tone, tonePlaying, "major", sharp))
                        bestChoice = tone;
                }
            } else {
                bestChoice = chosenRoot;
            }
        } else {
            if (BlackMagicka.noteInScale(tonePlaying, keyTonic, "minor", sharp)) {
                for (String tone : BlackMagicka.minorChord(chosenRoot, sharp)) {
                    if (BlackMagicka.noteInChord(tone, tonePlaying, "minor", sharp))
                        bestChoice = tone;
                }
            } else {
                bestChoice = chosenRoot;
            }
        }

        return bestChoice;
    }

    public Pair<Degree, Integer> chooseSoprano(String root) {
//        return new Pair(BlackMagicka.pickLeading(root, sharp), 4);
//        Random rand = new Random();
//        Integer rangeSize = VoiceOctaveRanges.Soprano.values().length;
        Degree deg = BlackMagicka.getDegreeIndex(keyTonic, mode, root, sharp);

        return new Pair(deg.getLeading(), VoiceOctaveRanges.Soprano.getOctave());
//        return new Pair(Degree.Leading, VoiceOctaveRanges.Soprano.values()[rand.nextInt(rangeSize)]);
    }

    public Pair<Degree, Integer> chooseAlto(String root) {
//        return new Pair(BlackMagicka.pickDominant(root, sharp), 0);
//        Random rand = new Random();
//        Integer rangeSize = VoiceOctaveRanges.Alto.values().length;
//        return new Pair(Degree.Dominant, VoiceOctaveRanges.Alto.values()[rand.nextInt(rangeSize)]);
        Degree deg = BlackMagicka.getDegreeIndex(keyTonic, mode, root, sharp);

        return new Pair(deg.getDominant(), VoiceOctaveRanges.Alto.getOctave());
    }

    public Pair<Degree, Integer> chooseTenor(String root) {
//        return new Pair(BlackMagicka.pickMediant(root, sharp), 0);
//        Random rand = new Random();
//        Integer rangeSize = VoiceOctaveRanges.Tenor.values().length;
//        return new Pair(Degree.Mediant, VoiceOctaveRanges.Tenor.values()[rand.nextInt(rangeSize)]);
        Degree deg = BlackMagicka.getDegreeIndex(keyTonic, mode, root, sharp);
        return new Pair(deg.getMediant(), VoiceOctaveRanges.Tenor.getOctave());
    }

    public Pair<Degree, Integer> chooseBass(String root) {
//        return new Pair(BlackMagicka.pickTonic(root, sharp), 0);
//        Random rand = new Random();
//        Integer rangeSize = VoiceOctaveRanges.Bass.values().length;
        Degree deg = BlackMagicka.getDegreeIndex(keyTonic, mode, root, sharp);
        return new Pair(deg.getTonic(), VoiceOctaveRanges.Bass.getOctave());
//        return new Pair(Degree.Tonic, VoiceOctaveRanges.Bass.values()[rand.nextInt(rangeSize)]);
    }

    public ArrayList<String> getGeneratedTones() {
        ArrayList<String> tones = new ArrayList<>();
        ArrayList<ArrayList<Note>> beats = data.getBeatList();

        for (ArrayList<Note> notes : beats) {
            for (Note n : notes) {
                String nextTone = chooseChord(n);
                if (nextTone == null) //not in key
                    continue;
                tones.add(nextTone);
            }
        }

        System.out.println("chosen roots " + tones);
        return tones;
    }

    public ArrayList<ArrayList<Pair<Degree, Integer>>> getGeneratedTonesDegrees() {
        List<String> tones = getGeneratedTones();

        ArrayList<ArrayList<Pair<Degree, Integer>>> partList = new ArrayList<ArrayList<Pair<Degree, Integer>>>();

        int i = 0;

        do {
            ArrayList<Pair<Degree, Integer>> degrees = new ArrayList<>();

            for (String tone : tones) {
                Pair<Degree, Integer> pair = null;

                if (!BlackMagicka.noteInScale(tone, keyTonic, mode, sharp))  {
                    Degree deg = Degree.NonScaleDegree;
                    deg.setNonScaleDegree(tone);
                    Integer register = 0;

                    switch(i) {
                        case 0:
                            register = VoiceOctaveRanges.Bass.getOctave();
                            break;
                        case 1:
                            register = VoiceOctaveRanges.Tenor.getOctave();
                            break;
                        case 2:
                            register = VoiceOctaveRanges.Alto.getOctave();
                            break;
                        case 3:
                            register = VoiceOctaveRanges.Soprano.getOctave();
                            break;
                    }

                    pair = new Pair<Degree, Integer>(deg, register);
                    degrees.add(pair);
                    continue;
                }

                switch (i) {
                    case 0:
                        pair = chooseBass(tone);
                        break;
                    case 1:
                        pair = chooseTenor(tone);
                        break;
                    case 2:
                        pair = chooseAlto(tone);
                        break;
                    case 3:
                        pair = chooseSoprano(tone);
                        break;
                }

                degrees.add(pair);
            }

            partList.add(degrees);
            for (Pair<Degree, Integer> deg : degrees)
                System.out.print(deg.t.toInt() + ",");
            System.out.println();
        } while (i++ < voices);

        return partList;
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

            System.out.println("freq:" + " " + i + " " + s);
        }

        Set<String> ret = new HashSet();

        int lim = prog.size() < 4 ? prog.size() : 4;

        for (int i = 0; i < lim; i++) {
            Pair<String, Integer> p = ScorePipeline.getMaxPair(prog);
            ret.add(p.t);
            prog.remove(p);
        }

        chordProgression = ret;
        System.out.println("chord progression chosen: " + ret);
    }

    private void addToChordProgression(String tone) {
//        possibleChords.add(tone);
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

            if (count != null
                    && BlackMagicka.noteInScale(noteVote, keyTonic, mode, sharp)
                    && BlackMagicka.noteInScale(note.toString(), keyTonic, mode, sharp)
                    && BlackMagicka.noteIn7thChord(note.toString(),keyTonic,  mode, sharp)) {

                int m = BlackMagicka.getDegreeIndex(keyTonic, mode, note.toString(), sharp).toInt();
                int n = BlackMagicka.getDegreeIndex(keyTonic, mode, noteVote, sharp).toInt();
                count *= ScorePipeline.getDegreeWeight(m, n) * 2;
//                System.out.print(count);
            }

            if (count == null) {
                ballot.put(noteVote, 1);
            } else {
                ballot.put(noteVote, count + 1);
            }
        }

        return ScorePipeline.getMaxVote(ballot);
    }

    private String pickNote (Note note) {
        Beat beat = new Beat();
        beat.addNote(note);
        //model.pickNote(beat);
        HashMap<Degree, Double> hash = model.possibleNotestoDegree(model.generateBeatChoices(beat));

        Degree d = model.getDegree(note);

        if (d == null)
            return null;

        hash = ScorePipeline.scorify(d, hash);
        System.out.println("degree: " + d.toInt() + " scored " + ScorePipeline.calcScore(d, hash));

        Degree n = transition(d, hash);

        return degreeTone(n);
    }

    private String degreeTone(Degree degree) {
        if (degree == null)
            return null;
        return BlackMagicka.pickIthNote(keyTonic, degree.toInt(), sharp);
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
        if(actualPossibilities.size() == 1) { for(Degree d : actualPossibilities.keySet()) return d; }
        else if(actualPossibilities.size() > 0) {
            return ScorePipeline.getMaxWeightDegree(actualPossibilities);
        } else { //only not possibilities has elements
            return ScorePipeline.getMaxWeightDegree(notPossibilities);
        }

        //will never happen (making the compiler happy)
        return Degree.Tonic;
    }

    /**
     * This method was finished by Sean Phillips @ 9:55 PM EST with the days left before the Final demo.
     * He was very pleased with the result, for he had remade the Measure Class structure.
     * HE BELIEVES THAT THIS GREAT METHOD NEEDS TO BE SHARED WITH THE WHOLE HUMAN RACE (at least some of it).
     */

    public void addBackToMusic() {
        ArrayList<ArrayList<Beat>> beats = voiceFactory.kickStart(getGeneratedTonesDegrees());//this returns the beats we need to add, maybe doesn't need to be in here, will just return all the voices in this beat list
        for(int i = 1; i < beats.size(); i++)
            addBackToMusic(beats.get(i),i);
    }

    public void addBackToMusic(ArrayList<Beat> beats, int part) {


        MetaData metaData = MetaData.getInstance();
        int divsPerMeasure = metaData.getDivisionsPerMeasure();
        ArrayList<Measure> measures = metaData.getMeasures();
        boolean doContinue = true;

        if(metaData.getBeattype() != 8){

            int beatPlace = 0;

            for(Measure measure : measures){
                for(int i = 0; i < metaData.getBeats(); i++){

                    for(Note note : beats.get(beatPlace).getNotes()) measure.addNoteToPart(note,part);
                    beatPlace+=i;
                    if(beatPlace >= beats.size()){
                        doContinue = false;
                        break;
                    }

                }

                if(!doContinue)break;

            }

        }else{
            int beatPlace = 0;
            int beatnum = 2;
            if(metaData.getBeats() == 3) beatnum = 1;

            for(Measure measure : measures){
                for(int i = 0; i < beatnum; i++){

                    for(Note note : beats.get(beatPlace).getNotes()) measure.addNoteToPart(note,part);
                    beatPlace+=i;
                    if(beatPlace >= beats.size()){
                        doContinue = false;
                        break;
                    }

                }

                if(!doContinue)break;

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

    private String getKeyTonic(String mode) {
        MetaData data = MetaData.getInstance();
        if (data == null) return null;
        System.out.println("finding fifths " + data.getSharps() + " " + fifthType);
        String tonic = null;

        int sharps = data.getSharps();
        if (mode.equals("minor")) {
            sharps += 3;
        }

        switch(sharps){
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
            case 7:
                tonic = "C#";
                break;
            case 8:
                tonic = "G#";
                break;
            case 9:
                tonic = "D#";
                break;
            case 10:
                tonic = "A#";
                break;
            case 11:
                tonic = "E#";
                break;
            case 12:
                tonic = "B#";
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
            case -7:
                tonic = "Cb";
        }

        return tonic;
    }
}