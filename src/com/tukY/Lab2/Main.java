package com.tukY.Lab2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Main {

    final static String re_LETTERS = "[a-zA-Z]";

    public static List<String> generateWords(int len){
        if(len < 1)
            return new ArrayList<>();

        var pattern = Pattern.compile(re_LETTERS);
        var words = Arrays.asList(pattern.pattern().split(""));

        for (int i = 1; i < len; ++i){
            words.forEach(w ->{
                Stream.of(pattern.pattern())
                        .forEach(s -> words.add(w.concat(s)));
                words.remove(w);
            });
        }

        return words;
    }

    public static boolean isPossibleInFSM(FiniteStateMachine FSM, Iterable<String> words){
        for (var w : words)
            if (!FSM.isPossible(w))
                return false;

        return true;
    }

    /*
     *  Lab 9 : find out if ALL words of len k (1) are possible in Finite State Machine (0)
     *
     *  Input Args:
     *      0   :   file describes FSM
     *      1   :   len of words
     *
     */
    public static void main(String[] args) {
        System.out.print(
                isPossibleInFSM(new FiniteStateMachine(args[0])
                , generateWords(Integer.getInteger(args[1])))
        );
    }
}