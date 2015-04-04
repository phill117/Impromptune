package io_handler;

import com.xenoage.zong.core.music.key.Key;
import com.xenoage.zong.core.music.time.Time;
import com.xenoage.zong.documents.ScoreDoc;

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

    private String timeSig;
    private int divisions;

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

        return QuillUtils.getKeySig(keyRoot, mode);
    }

    Time getTime() {
        return QuillUtils.getTime(timeSig);
    }

    int getDivisions() {
        return scoreDoc.getScore().getDivisions();
    }
}
