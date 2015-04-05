package virtuouso;

import java.util.List;

/**
 * Created by ben on 3/29/2015.
 */
public class VirtuosoAgent {

    public static void main(String args[]) {
        System.out.println(BlackMagicka.pickDominant("A"));
    }

    private enum WeightType{Chord, Beat, PassingTone, Root, Inversion}
    //this should be our generic hook for different weight schemes, different weighting for choosing likely chord progression than for picking phrase notes
    int heuristicCompare(WeightType type) { //needs more parameters of course, just a sketch of using different comparisons for choosing notes
        switch (type) {
            case Chord:
                break;
            case Beat:
                break;
            case PassingTone:
                break;
            case Root:
                break;
            case Inversion:
                break;
        }

        return -1;
    }

//    public StateNode minCost() {
//        StateNode buff = fringe.removeFirst();
//
//        for (StateNode node : fringe) {
//            if ((node.compareTo(buff)) < 0) {
//                // if (node.heuristicCompare(buff) < 0) {
//                buff = node;
//            }
//        }
//
//        // fringe.remove(buff);
//        return buff;
//    }
//
//    public StateNode DLS(int limit) {
//
//        while (!fringe.isEmpty()) {
//            ArrayDeque<StateNode> successors = generateStateSet(minCost(), limit);
//
//            if (successors.isEmpty() || successors.getLast().depth > limit) //failed, go deeper
//                return null;
//
//            if (goalPath(successors))
//                return successors.getLast();
//
//            //for more blahblah, check if visited...
//            for (StateNode node : successors)
//                fringe.addLast(node);
//        }
//
//        return null;
//    }

    public static void AStarSearch(int maxDepth) {
        for (int i = 0; i < maxDepth; i++) {
//            StateNode result = DLS(i);
            // System.out.println("limit hit -------------------");
        }
    }

//    viterbi path
    public void viterbiPath() {

    }

    void gibbsSampling() {

    }




}
