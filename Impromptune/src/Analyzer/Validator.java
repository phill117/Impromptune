package Analyzer;

import java.util.ArrayList;

/**
 * Created by Sean on 4/27/2015.
 */
public class Validator {

    int voiceCount;

    public Validator(int voiceCount){
        this.voiceCount = voiceCount;
    }

    public int validate(){
        return 0;
    }

    private void doubleOctave(ArrayList<String> first,ArrayList<String> second){
        for(int i = 0; i < first.size()-1; i++){
            if(i > second.size()-2) break;
            if(isOctave(first.get(i), second.get(i))){
                if(isOctave(first.get(i + 1), second.get(i + 1))){
                    //TODO
                }
            }
        }
    }

    private void doubleFifth(ArrayList<String> first,ArrayList<String> second){
        for(int i = 0; i < first.size()-1; i++){
            if(i > second.size()-2) break;
            if(isFifth(first.get(i),second.get(i))){
                if(isFifth(first.get(i+1),second.get(i+1))){
                    //TODO
                }
            }
        }
    }

    private void spacingError(ArrayList<String> first,ArrayList<String> second, int threshold){
        for(int i = 0; i < first.size(); i++){
            if(i > second.size()-1) break;
            if(distanceApart(first.get(i),second.get(i)) >= threshold){
                //TODO
            }
        }
    }

    //returns full octaves apart
    private int distanceApart(String s1, String s2){
        int diff = getRegister(s1) - getRegister(s2);
        if(getDegree(s1) < getDegree(s2)) return diff;
        else return diff-1;
    }

    private boolean isFifth(String s1, String s2){
        if(getRegister(s1) < getRegister(s2))
            if(Math.abs(getDegree(s2) - getDegree(s1)) == 4) return true;
        else if(Math.abs(getDegree(s1) - getDegree(s2)) == 4) return true;
        return false;
    }

    private boolean isOctave(String s1,String s2){
        if(getDegree(s1) == getDegree(s2)) return true;
        else return false;
    }

    private int getRegister(String s){
        return Integer.parseInt(s.substring(2));
    }

    private int getDegree(String s){
        return 1;//TODO
    }

    //"C[#|b|n][0-n]"
    private String createNew(String tone, String accidental, int register){
        return tone+accidental+Integer.toString(register); //TODO account for scale degree
    }

}
