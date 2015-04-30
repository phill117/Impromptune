package virtuouso;

import data_objects.Measure;
import utils.LimitedQueue;

import java.util.*;

/**
 * Created by ben on 4/4/2015.
 */

//static api for generation of scales/chords/notes/IR analysis algorithms
public class BlackMagicka {
    public static void main(String[] args) {

//        assert(minorChord("A").toString().equals("[A, C, E]")) : "failed";
//        assert(minorChord("B").toString().equals("[B, D, F#]")): "failed";
//        assert(majorScale("A").toString().equals("[A, B, C#, D, E, F#, G#]")): "failed";
//        assert(majorScale("B").toString().equals("[B, C#, D#, E, F#, G#, A#]")): "failed";
//        assert(majorChord("C").toString().equals("[C, E, G]")): "failed";
//        assert(majorScale("A").toString().equals("[A, B, C#, D, E, F#, G#]")): "failed";
//        assert((entropy(majorChord("C")) - 1.58496) < 0.00001): "failed";

    }

    //build list of tones as string for generation vectors based on data objects -- TODO
    List<String> buildStringPhrase(ArrayList<Measure> list) {
        List<String> phrase = new ArrayList<>();

//        for (Measure m : list)
//            m.getChords();

        return null;
    }

    //how similar are these phrases?
    double cosineSimilarity(List<String> phrase1, List<String> phrase2) {
        double cos = 0.0f;

        return cos;
    }

    //phrase entropy
    static double entropy(List<String> phrase) {
        double entropy = 0;

        Map<String, Integer> freq = new TreeMap<>();

        for (String str : phrase)
            freq.put(str, 0);

        for (String str : phrase) {
            int count = freq.containsKey(str) ? freq.get(str) : 0;
            freq.put(str, count + 1);
        }

        for (String sequence : freq.keySet()) {
            Double frequency = (double) freq.get(sequence) / freq.size();
            entropy -= frequency * (Math.log(frequency) / Math.log(2));
        }

        return entropy;
    }

    //calculates entropy difference by performing this operation (like adding removing notes from a phrase)
    double informationGain() {
        return 0.0f;
    }

    //3 dimensional melodic motion, can get more precise with 5 -- essentially pointless beyond that iirc
    //for instance D C A A B == *UURD -- good for comparing the tonal similarity of phrases despite different keys or notes
    //use with levenshtein distance function, generate two parsons code strings and use that w/a max edit distance of
    //say 10, 25, depending on the lengths of the phrases could be much larger
    public static String parsonsCode(List<String> phrase) {
        boolean skip = true;
        StringBuilder str = new StringBuilder();
        int prevTone = -1;
        for (String n : phrase) {
            int curTone = noteIndex(n, true);

            if(skip) {

                skip = false;
                str.append("*");
//                int tone = n.getScaleDegree();

                prevTone = curTone;

            } else {

                if(curTone == prevTone)
                    str.append("R");
                else if(curTone > prevTone)
                    str.append("U");
                else
                    str.append("D");

                prevTone = curTone;
            }
        }

        return str.toString();
    }

    //hamming weight or 'naive phrase similarity'
    public static int levenshtein(String q, String db, int maxEdits){
        int n = db.length();
        int m = q.length();

        int currRow[] = new int[n + 1];
        int prevRow[] = new int[n + 1];
        int tmp[];

        int ret = 0;

        for (int i = 1; i <= m; i++) {
            tmp = currRow;
            currRow = prevRow;
            prevRow = tmp;

            int min = currRow[0] = i;
            for(int j = 1; j <= n; j++) {
                if(q.charAt(i - 1) == (db.charAt(j - 1)))
                    ret = prevRow[j - 1];
                else
                    ret = min(currRow[j - 1], prevRow[j - 1], prevRow[j] ) + 1;

                if(ret < min)
                    min = ret;

                currRow[j] = ret;
            }

            if(min > maxEdits)
                return -1;
        }

        return ret;
    }

    //given an index, return the corresponding note, scale/chord functions use these
    static String noteIndexToString(int note, boolean sharp) {
        if (note > 11) note = note % 12;

        if (sharp) {
            switch (note) {
                case 0:
                    return "C";
                case 1:
                    return "C#";
                case 2:
                    return "D";
                case 3:
                    return "D#";
                case 4:
                    return "E";
                case 5:
                    return "F";
                case 6:
                    return "F#";
                case 7:
                    return "G";
                case 8:
                    return "G#";
                case 9:
                    return "A";
                case 10:
                    return "A#";
                case 11:
                    return "B";
            }
        } else {
            switch (note) {
                case 0:
                    return "C";
                case 1:
                    return "Db";
                case 2:
                    return "D";
                case 3:
                    return "Eb";
                case 4:
                    return "E";
                case 5:
                    return "F";
                case 6:
                    return "Gb";
                case 7:
                    return "G";
                case 8:
                    return "Ab";
                case 9:
                    return "A";
                case 10:
                    return "Bb";
                case 11:
                    return "B";
            }
        }
        return null;
    }

    //reverses a string form of the note to its' corresponding index
    static int noteIndex(String note, boolean sharp) {

        if (sharp) {
            //        System.out.println("BlackMagicka.NoteIndex() " + note);
            switch(note) {
                case "C":
                    return 0;
                case "C#":
                    return 1;
                case "D":
                    return 2;
                case "D#":
                    return 3;
                case "E":
                    return 4;
                case "F":
                    return 5;
                case "F#":
                    return 6;
                case "G":
                    return 7;
                case "G#":
                    return 8;
                case "A":
                    return 9;
                case "A#":
                    return 10;
                case "B":
                    return 11;
            }

        } else {
            switch(note) {
                case "C":
                    return 0;
                case "Db":
                    return 1;
                case "D":
                    return 2;
                case "Eb":
                    return 3;
                case "E":
                    return 4;
                case "F":
                    return 5;
                case "Gb":
                    return 6;
                case "G":
                    return 7;
                case "Ab":
                    return 8;
                case "A":
                    return 9;
                case "Bb":
                    return 10;
                case "B":
                    return 11;
            }
        }
//        if (note.length() > 1) {
//            String newNote = new String();
//            newNote += note.charAt(0);
//            return noteIndex(newNote);
//        }

        return -1;
    }

    //convert midi indices to this scheme
    static int noteShift(int note, int shift) {
        int offset = 0;
        if (note + shift > 11) {
            offset = note + shift - 11;
        }

        return offset;
    }

    //whole steps or half steps in terms of the 12 notes in chromatic scale, either string or ints starting with C
    static String getWholeStepStr(String note) {
        if (note.equals("B") || note.equals("E")) {
            return noteIndexToString(noteIndex(note, true) + 1, true);
        } else {
            return noteIndexToString(noteIndex(note, true) + 2, true);
        }
    }

    static String getHalfStepStr(String note) {
        return noteIndexToString((byte)(noteIndex(note, true) + 1), true);
    }

    static int getWholeStep(String note) {
        if (note.equals("B") || note.equals("E")) {
            return noteIndex(note, true) + 1;
        } else {
            return noteIndex(note, true) + 2;
        }
    }

    static int getHalfStep(String note) {
        return noteIndex(note, true) + 1;
    }

    //the following methods return scales/chords in the form of lists of strings
    static List<String> majorScale(String note, boolean sharp) {
//        System.out.println("building major scale based on tonic: " + note);
        List<String> scale = new LimitedQueue<>(7);
        int noteIndex = noteIndex(note, sharp);
        int [] majorSteps = {2,2,1,2,2,2,1}; //maps chromatic to major diatonic scale

        for (int i = 0; i < 7; i++) {
            scale.add(noteIndexToString(noteIndex, sharp));

            switch (majorSteps[i]) {
                case 1:
                    noteIndex += 1; //getHalfStep(noteIndexToString(noteIndex));
                    break;
                case 2:
                    noteIndex += 2; //getWholeStep(noteIndexToString(noteIndex));
                    break;
            }
        }

//        System.out.println(scale);
        return scale;
    }

    static List<String> minorScale(String note, boolean sharp) {
        List<String> scale = new LimitedQueue<>(7);
        int noteIndex = noteIndex(note, sharp);
        int [] minorSteps = {2, 1, 2, 2, 1, 2, 2}; //maps chromatic to minor diatonic scale

        for (int i = 0; i < 7; i++) {
            scale.add(noteIndexToString(noteIndex, sharp));
            switch (minorSteps[i]) {
                case 1:
                    noteIndex += 1; //getHalfStep(noteIndexToString(noteIndex));
                    break;
                case 2:
                    noteIndex += 2; //getWholeStep(noteIndexToString(noteIndex));
                    break;
            }
        }

        return scale;
    }

    public static List<String> majorChord(String note, boolean sharp) {
        int i = 0;
        int noteIndex = noteIndex(note, sharp);
        List<String> chord = new LimitedQueue<>(3);
        while (i < 3) {
            chord.add(noteIndexToString(noteIndex, sharp));
            if ((noteIndex - 11 == 0) || (noteIndex - 4 == 0)) {
                noteIndex += 3;
            } else {
                noteIndex += 4;
            }

            i++;
        }

        return chord;
    }

    public static List<String> major7thChord(String note, boolean sharp) {
        int i = 0;
        int noteIndex = noteIndex(note, sharp);
        List<String> chord = new LimitedQueue<>(4);
        while (i < 4) {
            chord.add(noteIndexToString(noteIndex, sharp));
            if ((noteIndex - 11 == 0) || (noteIndex - 4 == 0)) {
                noteIndex += 3;
            } else {
                noteIndex += 4;
            }

            i++;
        }

        return chord;
    }

    public static List<String> minorChord(String note, boolean sharp) {
        int i = 0;

        int [] minorSteps = {2, 1, 2, 2, 1, 2, 2}; //maps chromatic to minor diatonic scale
        int noteIndex = noteIndex(note, sharp);
        List<String> chord = new LimitedQueue<>(3);
        while (i < 3) {
            if (i == 0) {
                chord.add(noteIndexToString(noteIndex, sharp));
                noteIndex += minorSteps[0] + minorSteps[1];
            } else if (i == 1) {
                chord.add(noteIndexToString(noteIndex, sharp));
                noteIndex += minorSteps[2] + minorSteps[3];
            } else if (i == 2) {
                chord.add(noteIndexToString(noteIndex, sharp));
                noteIndex += minorSteps[4] + minorSteps[5];
            }

            i++;
        }

        return chord;
    }

    public static List<String> minor7thChord(String note, boolean sharp) {
        int i = 0;

        int [] minorSteps = {2, 1, 2, 2, 1, 2, 2}; //maps chromatic to minor diatonic scale
        int noteIndex = noteIndex(note, sharp);
        List<String> chord = new LimitedQueue<>(4);
        while (i < 4) {
            if (i == 0) {
                chord.add(noteIndexToString(noteIndex, sharp));
                noteIndex += minorSteps[0] + minorSteps[1];
            } else if (i == 1) {
                chord.add(noteIndexToString(noteIndex, sharp));
                noteIndex += minorSteps[2] + minorSteps[3];
            } else if (i == 2) {
                chord.add(noteIndexToString(noteIndex, sharp));
                noteIndex += minorSteps[4] + minorSteps[5];
            }

            i++;
        }

        return chord;
    }
    void pickChordProgression() {

    }

    void pickBeats() {}


    //these build major scales (need to generalize for minor and others) and use the degree class to return said note
    //pick ith should be preferred as it calls these anyway
    public static String pickTonic(String root, boolean sharp) {
        List<String> scale = majorScale(root, sharp);
        String note = scale.get(Degree.Tonic.toInt());
        return note;
    }

    public static String pickSuperTonic(String root, boolean sharp) {
        List<String> scale = majorScale(root, sharp);
        String note = scale.get(Degree.Supertonic.toInt());
        return note;
    }

    public static String pickMediant(String root, boolean sharp) {
        List<String> scale = majorScale(root, sharp);
        String note = scale.get(Degree.Mediant.toInt());
        return note;
    }

    public static String pickSubdominant(String root, boolean sharp) {
        List<String> scale = majorScale(root, sharp);
        String note = scale.get(Degree.Subdominant.toInt());
        return note;
    }

    public static String pickDominant(String root, boolean sharp) {
        List<String> scale = majorScale(root, sharp);
        String note = scale.get(Degree.Dominant.toInt());
        return note;
    }

    public static String pickSubmediant(String root, boolean sharp) {
        List<String> scale = majorScale(root, sharp);
        String note = scale.get(Degree.Submediant.toInt());
        return note;
    }

    public static String pickLeading(String root, boolean sharp) {
        List<String> scale = majorScale(root, sharp);
        String note = scale.get(Degree.Leading.toInt());
        return note;
    }

    //pick ith degree note based on root, ie root = C, index = 5, return the fifth -- G
    public static String pickIthNote(String root, int index, boolean sharp) {
        String note = null;

        switch(index) {
            case 0:
                note = pickTonic(root, sharp);
                break;
            case 1:
                note = pickSuperTonic(root, sharp);
                break;
            case 2:
                note = pickMediant(root, sharp);
                break;
            case 3:
                note = pickSubdominant(root, sharp);
                break;
            case 4:
                note = pickDominant(root, sharp);
                break;
            case 5:
                note = pickSubmediant(root, sharp);
                break;
            case 6:
                note = pickLeading(root, sharp);
                break;
        }

        return note;
    }

    static private int getIndexOfScale(List<String> scale, String tone, boolean sharp) {
        int i = 0;

        for (String s : scale) {
            if (s.equals(tone))
                return i;
            i++;
        }

        return -1;
    }

//    static Degree getDegreeIndex(String keyTonic, String mode, String tone) {
////        System.out.println("getting degree index " + tone + "from scale tonic: " + keyTonic + " from mode: " + mode);
//        if (mode.equals("major")) {
//            int i = getIndexOfScale(majorScale(keyTonic, sharp), tone);
//            if (i == -1) return null;
//            return Degree.values()[i];
//        } else if (mode.equals("minor")) {
//            int i = getIndexOfScale(minorScale(keyTonic, sharp), tone);
//            if (i == -1) return null;
//            return Degree.values()[i];
//        }
//
//        return null;
//    }

    public static Degree getDegreeIndex(String keyTonic, String mode, String tone, boolean sharp) {
//        System.out.println("getting degree index " + tone + "from scale tonic: " + keyTonic + " from mode: " + mode);
        if (mode.equals("major")) {
            int i = getIndexOfScale(majorScale(keyTonic, sharp), tone, sharp);
            if (i == -1) return null;
            return Degree.values()[i];
        } else if (mode.equals("minor")) {
            int i = getIndexOfScale(minorScale(keyTonic, sharp), tone, sharp);
            if (i == -1) return null;
            return Degree.values()[i];
        }

        return null;
    }


    //is this note in this root mode chord?
    static boolean noteInChord(String testNote, String root, String mode, boolean sharp) {  //note = this note, root = root of chord, mode = mode
        List<String> chord = null;
        switch(mode) {
            case "major":
                chord = majorChord(root, sharp);
                if (chord.contains(testNote))
                    return true;
                break;
            case "minor":
                chord = minorChord(root, sharp);
                if (chord.contains(testNote))
                    return true;
                break;
            default:
                break;
        }

        return false;
    }

    static boolean noteIn7thChord(String testNote, String root, String mode, boolean sharp) {  //note = this note, root = root of chord, mode = mode
        List<String> chord = null;
        switch(mode) {
            case "major":
                chord = major7thChord(root, sharp);
//                System.out.println(chord);
                if (chord.contains(testNote))
                    return true;
                break;
            case "minor":
                chord = minor7thChord(root, sharp);
//                System.out.println(chord);
                if (chord.contains(testNote))
                    return true;
                break;
            default:
                break;
        }

        return false;
    }
    //is this note in this root mode scale?
    public static boolean noteInScale(String note, String keyTonic, String mode, boolean sharp) { //note = this note, root = root of scale, mode = mode
        List<String> scale = null;
        switch(mode) {
            case "major":
                scale = majorScale(keyTonic, sharp); //build a major scale, based on root
                if (scale.contains(note))
                    return true;
                break;
            case "minor":
                scale = minorScale(keyTonic, sharp);
                if (scale.contains(note))
                    return true;
                break;
            default:
                break;
        }

        return false;
    }

    public static int min(int a, int b, int c) {
        return Math.min(Math.min(a, b), c);
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
}