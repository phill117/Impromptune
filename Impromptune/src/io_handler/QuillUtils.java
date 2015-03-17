package io_handler;

import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.music.Pitch;
import com.xenoage.zong.core.music.annotation.Annotation;
import com.xenoage.zong.core.music.annotation.Articulation;
import com.xenoage.zong.core.music.annotation.ArticulationType;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.format.BezierPoint;
import com.xenoage.zong.core.music.slur.SlurWaypoint;
import com.xenoage.zong.core.music.time.Time;
import com.xenoage.zong.core.music.time.TimeType;

import java.util.ArrayList;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.music.Pitch.pi;

/**
 * Created by ben on 3/16/2015.
 */
public class QuillUtils {



    static Fraction getFraction(String fraction) {
        switch (fraction) {
            case "1/1":
                return fr(1, 1);

            case "1/2":
                return fr(1, 2);

            case "1/4":
                return fr(1, 4);

            case "1/8":
                return fr(1, 8);

            case "1/16":
                return fr(1, 16);

            case "1/32":
                return fr(1, 32);

            case "1/64":
                return fr(1,64);
        }

        System.err.println("Invalid duration: " + fraction);
        return null;
    }

    //return fraction based on char input
    static Fraction getFraction(char d) {
        switch (d) {
            case 'w':
                return fr(1, 1);

            case 'h':
                return fr(1, 2);

            case 'q':
                return fr(1, 4);

            case 'i':
                return fr(1, 8);

            case 's':
                return fr(1, 16);

            case 't':
                return fr(1, 32);

            case 'x':
                return fr(1,64);
        }

        System.err.println("Invalid duration: " + d);
        return null;
    }

    static Pitch getPitch(char p, char a, int o) {

        switch(a) {
            case '#'://sharp
                return pi(p, 1, o);
            case 'f'://flat
                return pi(p, -1, o);
            case 'n': //natural
                return pi(p, 0, o);
        }
        System.err.println("Invalid alteration: " + p);
        return null;
    }

    static Time getTime(String timeSig) {
        switch(timeSig) {
            case "2/2":
                return new Time(TimeType.time_2_2);
            case "3/4":
                return new Time(TimeType.time_3_4);
            case "4/4":
                return new Time(TimeType.time_4_4);
            case "6/8":
                return new Time(TimeType.time_6_8);
            default: //custom
                String values[] = timeSig.split("/");
                int num = Integer.parseInt(values[0]);
                int den = Integer.parseInt(values[1]);
                return new Time(TimeType.timeType(num, den));
        }
    }


    public static Chord chord(Fraction fraction, Pitch... pitches) {
        return chord(fraction, null, pitches);
    }

    private static Chord chord(Fraction fraction, ArticulationType[] articulations, Pitch... pitches) {
        Chord chord = new Chord(com.xenoage.zong.core.music.chord.Note.notes(pitches), fraction);

        if (articulations != null) {
            ArrayList<Annotation> a = alist(articulations.length);
            for (ArticulationType at : articulations)
                a.add(new Articulation(at));
            chord.setAnnotations(a);
        }

        return chord;
    }

    public static SlurWaypoint clwp(Chord c, BezierPoint bezierPoint) {
        return new SlurWaypoint(c, null, bezierPoint);
    }
}
