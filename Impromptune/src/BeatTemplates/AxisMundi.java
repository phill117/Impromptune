package BeatTemplates;

/**
 * Created by ben on 4/28/2015.
 */
public enum AxisMundi {
    Bass(0),
    Tenor(1),
    Alto(2),
    Soprano(3);

    int partType;

    AxisMundi(int index) {
        partType = index;
    }

    int toInt() {
        return partType;
    }
}
