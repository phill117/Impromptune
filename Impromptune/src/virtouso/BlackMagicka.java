package virtouso;

import data_objects.Measure;
import data_objects.Note;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.Vector;

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

    double cosineSimilarity(List<String> phrase, List<String> phrase) {
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

    //melodic motion
    public static String parsonsCode(List<String> phrase) {
        boolean skip = true;
        StringBuilder str = new StringBuilder();
        int prevTone = -1;
        for (Note n : phrase) {
            int curTone = noteIndex(new String(n.getPitch() + n.getAccidental()));

            if(skip) {

                skip = false;
                str.append("*");
                int key = n.getScaleDegree();

                prevTone = key;

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

    public static int min(int a, int b, int c) {
        return Math.min(Math.min(a, b), c);
    }
}