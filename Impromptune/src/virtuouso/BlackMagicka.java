package virtuouso;

import data_objects.Measure;
import data_objects.Note;

import java.util.*;

/**
 * Created by ben on 4/4/2015.
 */
public class BlackMagicka {
    public static void main(String[] args) {

        assert(minorChord("A").toString().equals("[A, C, E]")) : "failed";
        assert(minorChord("B").toString().equals("[B, D, F#]")): "failed";
        assert(majorScale("A").toString().equals("[A, B, C#, D, E, F#, G#]")): "failed";
        assert(majorScale("B").toString().equals("[B, C#, D#, E, F#, G#, A#]")): "failed";
        assert(majorChord("C").toString().equals("[C, E, G]")): "failed";
        assert(majorScale("A").toString().equals("[A, B, C#, D, E, F#, G#]")): "failed";
        assert((getEntropy(majorChord("C")) - 1.58496) < 0.00001): "failed";

    }
    //build list of tones as string for utilities
    List<String> buildStringPhrase(ArrayList<Measure> list) {
        List<String> phrase = new ArrayList<>();

//        for (Measure m : list)
//            m.getChords();

        return null;
    }

    double cosineSimilarity(List<String> phrase1, List<String> phrase2) {
        double cos = 0.0f;

        return cos;
    }

    static double getEntropy(List<String> phrase) {
        double entropy = 0;

        Map<String, Integer> freq = new TreeMap<>();

        for (String str : phrase) {
            freq.put(str, 0);
        }

        int histogram [] = new int[freq.size()];

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

    double informationGain() {
        return 0.0f;
    }

    //3 dimension melodic motion, can get more precise with 5 -- essentially pointless beyond that iirc
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

        return -1;
    }

    static byte noteShift(byte note, byte shift) {
        byte offset = 0;
        if (note + shift > 11) {
            offset = (byte)(note + shift - 11);
        }

        return offset;
    }

    static String getWholeStepStr(String note) {
        if (note.equals("B") || note.equals("E")) {
            return noteIndexToString((byte)(noteIndex(note) + 1));
        } else {
            return noteIndexToString((byte)(noteIndex(note) + 2));
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

    static List<String> majorScale(String note) {
        List<String> scale = new LimitedQueue<>(7);
        int noteIndex = noteIndex(note);
        int [] majorSteps = {2,2,1,2,2,2,1};

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

        System.out.println(scale.toString());
        return scale;
    }

    static List<String> minorScale(String note) {
        List<String> scale = new LimitedQueue<>(7);
        int noteIndex = noteIndex(note);
        int [] minorSteps = {2, 1, 2, 2, 1, 2, 2};

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

        System.out.println(scale.toString());
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

        System.out.println(chord.get(0) + " " + chord.get(1) + " " + chord.get(2));
        return chord;
    }

    static List<String> minorChord(String note) {
        int i = 0;

        int [] minorSteps = {2, 1, 2, 2, 1, 2, 2};
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

        System.out.println(chord.get(0) + " " + chord.get(1) + " " + chord.get(2));
        return chord;
    }

    public static int min(int a, int b, int c) {
        return Math.min(Math.min(a, b), c);
    }
}