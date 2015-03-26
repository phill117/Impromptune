package data_objects;

/**
 * Created by Sean on 3/25/2015.
 */
public class Note {

    int ocatave;
    char pitch;
    float duration;   //shall be counted as a fraction of one beat


    public Note(){

    }

    public boolean isRest(){
        return pitch == 'r';
    }

    public boolean isChordTone(){

        //TODO - impement this crap later

        return true;
    }

    public int getScaleDegree(){

        //TODO - impement this crap later

        return 1;
    }
}
