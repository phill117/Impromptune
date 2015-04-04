package virtuouso;

import data_objects.Measure;
import data_objects.Note;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ben on 4/4/2015.
 */
public class BlackMagicka {

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

    double entropy(List<String> phrase) {
        double entropy = 0;
//        TreeSet<String> histogram = new TreeSet<String>();
        List<String> map = new ArrayList<>();
        int histogram [] = new int[phrase.size()];

        for (int i = 0; i < phrase.size(); i++) {
            String str = phrase.get(i);
            for (int j = 0; j < map.size(); i++) {
                String s = map.get(j);
                if (str.compareTo(s) == 0)
                    histogram[j]++;
            }
        }

        //finish

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

    static String getWholeStep(String note) {
        if (note.equals("B") || note.equals("E")) {
            return noteIndexToString((byte)(noteIndex(note) + 1));
        } else {
            return noteIndexToString((byte)(noteIndex(note) + 2));
        }
    }

    static String getHalfStep(String note) {
        return noteIndexToString((byte)(noteIndex(note) + 1));
    }

    static byte midiNoteIndex(byte note) {
        return (byte)(note + 60);
    }

//    static String majorScale(String note) {
//        StringBuilder scale = new StringBuilder();
//        byte noteIndex = noteIndex(note);
//        byte [] majorSteps = {2,2,1,2,2,2,1};
//
//        for (int i = 0; i < 7; i++) {
//            scale.append(noteIndexToString(noteIndex));
//            scale.append(" ");
//
//            switch (majorSteps[i]) {
//                case 1:
//                    noteIndex = noteIndex(getHalfStep(noteIndexToString(noteIndex)));
//                    break;
//                case 2:
//                    noteIndex = noteIndex(getWholeStep(noteIndexToString(noteIndex)));
//                    break;
//            }
//
//        }
//
//        return scale.toString();
//    }
//
//    static byte [] majorScaleAsBytes(String note) {
//        byte noteIndex = noteIndex(note);
//        byte [] majorSteps = {2,2,1,2,2,2,1};
//        byte [] scale = new byte[7];
//
//        for (int i = 0; i < 7; i++) {
//            scale[i] = midiNoteIndex(noteIndex);
//            noteIndex += majorSteps[i];
//        }
//
//        return scale;
//    }
//
//    static String minorScale(String note) {
//        StringBuilder scale = new StringBuilder();
//        byte noteIndex = noteIndex(note);
//        byte [] minorSteps = {2, 1, 2, 2, 1, 2, 2};
//
//        for (int i = 0; i < 7; i++) {
//            scale.append(noteIndexToString(noteIndex));
//            scale.append(" ");
//            switch (minorSteps[i]) {
//                case 1:
//                    noteIndex = noteIndex(getHalfStep(noteIndexToString(noteIndex)));
//                    break;
//                case 2:
//                    noteIndex = noteIndex(getWholeStep(noteIndexToString(noteIndex)));
//                    break;
//            }
//        }
//
//        return scale.toString();
//    }
//
//    static byte[] majorChord(String note) {
//        int i = 0;
//        byte noteIndex = noteIndex(note);
//        byte [] chord = new byte[3];
//        while (i < 3) {
//            chord[i] = noteIndex;
//            if ((noteIndex - 11 == 0) || (noteIndex - 4 == 0)) {
//                noteIndex += 3;
//            } else {
//                noteIndex += 4;
//            }
//
//            i++;
//        }
//
//        System.out.println(noteIndexToString(chord[0]) + " " + midiNoteIndex(chord[0]) + " "
//                + noteIndexToString(chord[1]) + " " + midiNoteIndex(chord[1]) + " " + noteIndexToString(chord[2]) + " " + midiNoteIndex(chord[2]));
//        return chord;
//    }
//
//    static int [] minorChord(String note) {
//        int i = 0;
//        int noteIndex = noteIndex(note);
//        int[] chord = new int[3];
//        while (i < 3) {
//            chord[i] = noteIndex;
//            // if ((noteIndex - 11 == 0) || (noteIndex - 4 == 0)) {
//            noteIndex += 1;
//            // }
//            // else {
//            noteIndex += 2;
//            // }
//
//            i++;
//        }
//
//        System.out.println(midiNoteIndex(chord[0]) + " " + midiNoteIndex(chord[1]) + " " + midiNoteIndex(chord[2]));
//        return chord;
//    }

    public static int min(int a, int b, int c) {
        return Math.min(Math.min(a, b), c);
    }
}