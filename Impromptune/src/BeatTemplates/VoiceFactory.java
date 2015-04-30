package BeatTemplates;

import Analyzer.Validator;
import data_objects.Beat;
import data_objects.MetaData;

import utils.Pair;
import virtuouso.BlackMagicka;
import virtuouso.Degree;

import java.util.ArrayList;

/**
 * Created by ben on 4/27/2015.
 */
public class VoiceFactory {

    private HarmonicMotion[] motionTypes = HarmonicMotion.values();
    private Rhythm[] rhythmTypes = Rhythm.values();
    private String keyTonic;
    private String mode;
    private boolean sharp;
    private ArrayList<ArrayList<Pair<String, Integer>>> voices;
    private int repetition = 0;

    public VoiceFactory(String keyTonic, String mode, boolean sharp, int repetition) {
        this.keyTonic = keyTonic;
        this.mode = mode;
        this.sharp = sharp;
        this.repetition = repetition;
    }

    private ArrayList<ArrayList<Beat>> buildVoices() {
        ArrayList<ArrayList<Beat>> voiceBeats = new ArrayList<>();
        AxisMundi axis [] = AxisMundi.values();

        for (int i = 0; i < voices.size(); i++)
            voiceBeats.add(buildVoice(axis[i]));

        return voiceBeats;
    }

    public ArrayList<Beat> buildVoice(AxisMundi index) {
        ArrayList<Beat> beats = new ArrayList<>();
        ArrayList<Pair<String, Integer>> arr = voices.get(index.toInt());

        switch (index) {

            case Bass:

                for (Pair<String, Integer> pair : arr) {
                    BassTemplate bass = new BassTemplate(pair.first(), HarmonicMotion.Parallel, repetition);
                    beats.add(bass.buildBeat(MetaData.getInstance().getDivisions(), pair.second()));
                }

                break;


            case Tenor:

                for (Pair<String, Integer> pair : arr) {
                    TenorTemplate tenor = new TenorTemplate(pair.first(), HarmonicMotion.Parallel, repetition);
                    beats.add(tenor.buildBeat(MetaData.getInstance().getDivisions(), pair.second()));
                }

                break;

            case Alto:

                for (Pair<String, Integer> pair : arr) {
                    AltoTemplate alto = new AltoTemplate(pair.first(), HarmonicMotion.Parallel, repetition, keyTonic, mode, sharp);
                    beats.add(alto.buildBeat(MetaData.getInstance().getDivisions(), pair.second()));
                }

                break;

            case Soprano:

                for (Pair<String, Integer> pair : arr) {
                    SopranoTemplate soprano = new SopranoTemplate(pair.first(), HarmonicMotion.Contrary, repetition, keyTonic, mode, sharp);
                    beats.add(soprano.buildBeat(MetaData.getInstance().getDivisions(), pair.second()));
                }

                break;
        }

        return beats;
    }

    public void degreesToTones(ArrayList<ArrayList<Pair<Degree, Integer>>> inputVoices) {

        Validator validator = new Validator(inputVoices);
        ArrayList<ArrayList<Pair<Degree, Integer>>> valid = validator.validate();
        ArrayList<ArrayList<Pair<String, Integer>>> list = new ArrayList<ArrayList<Pair<String, Integer>>>();

        for (ArrayList<Pair<Degree, Integer>> voice : valid) {

            ArrayList<Pair<String, Integer>> newVoice = new ArrayList<>();

            for (Pair<Degree, Integer> p : voice) {
                String tone;

                if (p.first().toInt() == 7)
                    tone = p.first().getNonScaleTone();
                else
                    tone = BlackMagicka.pickIthNote(keyTonic, p.first().toInt(), sharp);

                newVoice.add(new Pair(tone, p.second()));
            }

            list.add(newVoice);
        }

        voices = list;
    }

    public ArrayList<ArrayList<Beat>> kickStart(ArrayList<ArrayList<Pair<Degree, Integer>>> inputVoices) {
        degreesToTones(inputVoices);
        return buildVoices();
    }
}