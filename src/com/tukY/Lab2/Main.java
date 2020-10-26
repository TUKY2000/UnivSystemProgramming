package com.tukY.Lab2;

import java.util.*;

public class Main {
    static List<String> generateWords(List<String> pwords, char[] alphabet, int len0, int len1){
        if (len0 == len1)
            return pwords;

        List<String> words = new ArrayList<>();

        for (var word : pwords){
            for (var c : alphabet){
                words.add(word + c);
            }
        }

        return generateWords(words, alphabet, len0, len1+1);
    }

    public static List<String> generateWords(String alphabet, int len){
        if(len < 1)
            return new ArrayList<>();

        return generateWords(List.of(alphabet.split("")), alphabet.toCharArray(), len, 1);
    }

    /**
     * @param FSM Finite State Machine
     * @param words Collection of strings to check
     * @return if all of words are possible at FSM
     */
    public static boolean isPossibleInFSM(FiniteStateMachine FSM, Iterable<String> words){
        for (var w : words)
            if (!FSM.isPossible(w))
                return false;

        return true;
    }

    /**
     *  Lab 9 : find out if ALL words of len k (1) are possible in Finite State Machine (0)
     *
     *  Input Args:
     *      0   :   file describes FSM
     *      1   :   len of words
     *
     */
    public static void main(String[] args) {
        FiniteStateMachine fsm = new FiniteStateMachine(args[0]);
        List<String> words = generateWords(fsm.getAlphabet(), Integer.parseInt(args[1]));

        boolean res = isPossibleInFSM(fsm, words);

        System.out.print(res);
    }
}