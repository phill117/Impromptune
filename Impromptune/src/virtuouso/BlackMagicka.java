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

        assert(minorChord("A").toString().equals("[A, C, E]")) : "failed";
        assert(minorChord("B").toString().equals("[B, D, F#]")): "failed";
        assert(majorScale("A").toString().equals("[A, B, C#, D, E, F#, G#]")): "failed";
        assert(majorScale("B").toString().equals("[B, C#, D#, E, F#, G#, A#]")): "failed";
        assert(majorChord("C").toString().equals("[C, E, G]")): "failed";
        assert(majorScale("A").toString().equals("[A, B, C#, D, E, F#, G#]")): "failed";
        assert((entropy(majorChord("C")) - 1.58496) < 0.00001): "failed";

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
            int curTone = noteIndex(n);

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
    static String noteIndexToString(int note) {
        if (note > 11) note = note % 12;

        switch(note) {
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

        return null;
    }

    //reverses a string form of the note to its' corresponding index
    static int noteIndex(String note) {
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
            return noteIndexToString(noteIndex(note) + 1);
        } else {
            return noteIndexToString(noteIndex(note) + 2);
        }
    }

    static String getHalfStepStr(String note) {
        return noteIndexToString((byte)(noteIndex(note) + 1));
    }

    static int getWholeStep(String note) {
        if (note.equals("B") || note.equals("E")) {
            return noteIndex(note) + 1;
        } else {
            return noteIndex(note) + 2;
        }
    }

    static int getHalfStep(String note) {
        return noteIndex(note) + 1;
    }

    //the following methods return scales/chords in the form of lists of strings
    static List<String> majorScale(String note) {
        List<String> scale = new LimitedQueue<>(7);
        int noteIndex = noteIndex(note);
        int [] majorSteps = {2,2,1,2,2,2,1}; //maps chromatic to major diatonic scale

        for (int i = 0; i < 7; i++) {
            scale.add(noteIndexToString(noteIndex));

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

    static List<String> minorScale(String note) {
        List<String> scale = new LimitedQueue<>(7);
        int noteIndex = noteIndex(note);
        int [] minorSteps = {2, 1, 2, 2, 1, 2, 2}; //maps chromatic to minor diatonic scale

        for (int i = 0; i < 7; i++) {
            scale.add(noteIndexToString(noteIndex));
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

    static List<String> majorChord(String note) {
        int i = 0;
        int noteIndex = noteIndex(note);
        List<String> chord = new LimitedQueue<>(3);
        while (i < 3) {
            chord.add(noteIndexToString(noteIndex));
            if ((noteIndex - 11 == 0) || (noteIndex - 4 == 0)) {
                noteIndex += 3;
            } else {
                noteIndex += 4;
            }

            i++;
        }

        return chord;
    }

    static List<String> minorChord(String note) {
        int i = 0;

        int [] minorSteps = {2, 1, 2, 2, 1, 2, 2}; //maps chromatic to minor diatonic scale
        int noteIndex = noteIndex(note);
        List<String> chord = new LimitedQueue<>(3);
        while (i < 3) {
            if (i == 0) {
                chord.add(noteIndexToString(noteIndex));
                noteIndex += minorSteps[0] + minorSteps[1];
            } else if (i == 1) {
                chord.add(noteIndexToString(noteIndex));
                noteIndex += minorSteps[2] + minorSteps[3];
            } else if (i == 2) {
                chord.add(noteIndexToString(noteIndex));
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
    static String pickTonic(String root) {
        List<String> scale = majorScale(root);
        String note = scale.get(Degree.Tonic.toInt());
        return note;
    }

    static String pickSuperTonic(String root) {
        List<String> scale = majorScale(root);
        String note = scale.get(Degree.Supertonic.toInt());
        return note;
    }

    static String pickMediant(String root) {
        List<String> scale = majorScale(root);
        String note = scale.get(Degree.Mediant.toInt());
        return note;
    }

    static String pickSubdominant(String root) {
        List<String> scale = majorScale(root);
        String note = scale.get(Degree.Subdominant.toInt());
        return note;
    }

    static String pickDominant(String root) {
        List<String> scale = majorScale(root);
        String note = scale.get(Degree.Dominant.toInt());
        return note;
    }

    static String pickSubmediant(String root) {
        List<String> scale = majorScale(root);
        String note = scale.get(Degree.Submediant.toInt());
        return note;
    }

    static String pickLeading(String root) {
        List<String> scale = majorScale(root);
        String note = scale.get(Degree.Leading.toInt());
        return note;
    }


    //pick ith degree note based on root, ie root = C, index = 5, return the fifth -- G
    static String pickIthNote(String root, int index) {
        String note = null;

        switch(index) {
            case 0:
                note = pickTonic(root);
                break;
            case 1:
                note = pickSuperTonic(root);
                break;
            case 2:
                note = pickMediant(root);
                break;
            case 3:
                note = pickSubdominant(root);
                break;
            case 4:
                note = pickDominant(root);
                break;
            case 5:
                note = pickSubmediant(root);
                break;
            case 6:
                note = pickLeading(root);
                break;
        }

        return note;
    }

    static private int getIndexOfScale(List<String> scale, String tone) {
        int i = 0;

        for (String s : scale) {
            if (s.equals(tone))
                return i;
            i++;
        }

        return -1;
    }

    static Degree getDegreeIndex(String tonic, String mode, String tone) {
//        System.out.println("getting degree index" + tone);
        if (mode.equals("major")) {
            int i = getIndexOfScale(majorScale(tonic), tone);
            if (i == -1) return null;
            return Degree.values()[i];
        } else if (mode.equals("minor")) {
            int i = getIndexOfScale(minorScale(tonic), tone);
            if (i == -1) return null;
            return Degree.values()[i];
        }

        return null;
    }

    //is this note in this root mode chord?
    static boolean noteInChord(String root, String testNote, String mode) {  //note = this note, root = root of chord, mode = mode
        List<String> chord = null;
        switch(mode) {
            case "major":
                chord = majorChord(root);
                if (chord.contains(testNote))
                    return true;
                break;
            case "minor":
                chord = minorChord(root);
                if (chord.contains(testNote))
                    return true;
                break;
            default:
                break;
        }

        return false;
    }

    //is this note in this root mode scale?
    static boolean noteInScale(String note, String root, String mode) { //note = this note, root = root of scale, mode = mode
        List<String> scale = null;
        switch(mode) {
            case "major":
                scale = majorScale(root); //build a major scale, based on root
                if (scale.contains(note))
                    return true;
                break;
            case "minor":
                scale = minorScale(root);
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
}