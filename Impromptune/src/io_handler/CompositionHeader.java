package io_handler;

import com.xenoage.zong.core.music.key.Key;
import com.xenoage.zong.core.music.time.Time;
import com.xenoage.zong.documents.ScoreDoc;

import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Created by ben on 4/4/2015.
 */
public class CompositionHeader {
    Composition composition = null;
    Map<String, Object> metaData = null;

    ScoreDoc scoreDoc;
    String title;
    String author;

    String keyRoot;
    String mode;

    String timeSig;
    int divisions;

    public CompositionHeader() {
        metaData = new TreeMap<String, Object>();
        composition = new Composition();
    }

    public CompositionHeader(String fileName) {
        
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
