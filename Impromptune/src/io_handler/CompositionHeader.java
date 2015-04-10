package io_handler;

import com.xenoage.zong.core.music.key.Key;
import com.xenoage.zong.core.music.time.Time;
import com.xenoage.zong.documents.ScoreDoc;
import utils.Pair;

import java.util.Map;
import java.util.TreeMap;


/**
 * Created by ben on 4/4/2015.
 */
public class CompositionHeader {
    private Composition composition = null;
    private Map<String, Object> metaData = null;

    private ScoreDoc scoreDoc;
    private String title;
    private String author;

    private String keyRoot;
    private String mode;
    private String keyMod;

    private String timeSig;
    private int divisions;

    boolean isFlat = false;

    public CompositionHeader() {
        metaData = new TreeMap<String, Object>();
        composition = new Composition();
    }

    public CompositionHeader(String fileName) {
        metaData = new TreeMap<String, Object>();
        composition = new Composition(fileName);
    }

    Key getKey() {
        if (keyRoot == null || mode == null) {
            System.err.println("CompositionHeader is missing root or mode");
            return null;
        }

        return QuillUtils.getKeySig(keyRoot, mode, keyMod);
    }

    public Pair<String, String> getKeyStr() {
        return new Pair<>(keyRoot, mode);
    }

    void setKey(String key, String mode, String keyMod) {
        this.keyRoot = key;
        this.mode = mode;
        this.keyMod = keyMod;
        composition.writeKeySig(key, mode, keyMod);
    }

    Time getTime() {
        return QuillUtils.getTime(timeSig);
    }

    String getTimeStr() { return timeSig; }

    void setTime(String time) {
        this.timeSig = time;
    }

    int calculateDivisions() {
        return 0;
    }

    int getDivisions() {
        return scoreDoc.getScore().getDivisions();
    }

    boolean isFlatKey() { return isFlat; }
}
