package Analyzer;

import com.itextpdf.text.Meta;
import data_objects.MetaData;
import utils.Pair;
import virtuouso.Degree;

import java.util.ArrayList;

/**
 * Created by Sean on 4/27/2015.
 */
public class Validator {

    ArrayList<ArrayList<Pair<Degree, Integer>>> parts;
    int voiceCount;

    public Validator(ArrayList<ArrayList<Pair<Degree, Integer>>> parts){
        this.parts = parts;
        this.voiceCount = parts.size();
    }

    public ArrayList<ArrayList<Pair<Degree, Integer>>> validate(){

        for(int i = 0; i < parts.size()-1; i++){
            for(int k = i + 1; k < parts.size(); k++) {
                System.out.println("k = "+k+" i = "+i);
                doubleFifth(parts.get(k),parts.get(i));
                doubleOctave(parts.get(k),parts.get(i));

                if(k-1 == i){
                    int thresh = 1;
                    if(i == 0 && k == 1) thresh++;
                    spacingError(parts.get(i),parts.get(k),thresh);
                }
            }
        }
        return parts;
    }

    private void doubleOctave(ArrayList<Pair<Degree, Integer>> first,ArrayList<Pair<Degree, Integer>> second){
        for(int i = 0; i < first.size()-1; i++){
            if(i > second.size()-2) break;
            if(isOctave(first.get(i), second.get(i))){
                if(isOctave(first.get(i + 1), second.get(i + 1))){
                    second.set(i,createNew(second.get(i).first(),second.get(i).second(),2));
                    i--;
                }
            }
        }
    }

    private void doubleFifth(ArrayList<Pair<Degree, Integer>> first,ArrayList<Pair<Degree, Integer>> second){
        for(int i = 0; i < first.size()-1; i++){
            if(i > second.size()-2) break;
            if(isFifth(first.get(i),second.get(i))){
                if(isFifth(first.get(i+1),second.get(i+1))){
                    second.set(i,createNew(second.get(i).first(),second.get(i).second(),2));
                    i--;
                }
            }
        }
    }

    private void spacingError(ArrayList<Pair<Degree, Integer>> first,ArrayList<Pair<Degree, Integer>> second, int threshold){
        for(int i = 0; i < first.size(); i++){
            if(i > second.size()-1) break;
            if(distanceApart(first.get(i), second.get(i)) >= threshold) {
                second.set(i,createNew(second.get(i).first(),second.get(i).second(),7));
                i--;
            }
        }
    }

    //returns full octaves apart
    private int distanceApart(Pair<Degree, Integer> s1, Pair<Degree, Integer> s2){
        int diff = s1.second() - s2.second();
        if(getDegree(s1.first()) < getDegree(s2.first())) return diff;
        else return diff-1;
    }

    private boolean isFifth(Pair<Degree, Integer> s1, Pair<Degree, Integer> s2){
        if(s1.second() < s2.second())
            if(Math.abs(getDegree(s2.first()) - getDegree(s1.first())) == 4) return true;
        else if(Math.abs(getDegree(s1.first()) - getDegree(s2.first())) == 4) return true;
        return false;
    }

    private boolean isOctave(Pair<Degree, Integer> s1,Pair<Degree, Integer> s2){
        if(getDegree(s1.first()) == getDegree(s2.first())) return true;
        else return false;
    }

    private int getDegree(Degree d){
        return d.toInt()+1;
    }

    private Pair<Degree, Integer> createNew(Degree d, int reg, int inc){
        int newD = (d.toInt() + inc);
        if(newD >= 7) reg++;
        if(newD < 0) reg--;
        newD = newD % 7;
        Degree D = numToDegree(newD);
        return new Pair<>(D, reg);
    }

    private Degree numToDegree(int i){
        switch (i){
            default:
            case 0:return Degree.Tonic;
            case 1:return Degree.Supertonic;
            case 2:return Degree.Mediant;
            case 3:return Degree.Subdominant;
            case 4:return Degree.Dominant;
            case 5:return Degree.Submediant;
            case 6:return Degree.Leading;
        }
    }

    public int getDegreeDuration(){
        return MetaData.getInstance().getDivisions();
    }
    //"C[#|b|n][0-n]"

}
