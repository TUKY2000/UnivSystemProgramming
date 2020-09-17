package com.tukY.Lab1;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Lab {

    String delims_regex;


    public Lab() {
        delims_regex = "([^a-zA-Z\\d])+";
    }

    static public Set<String> findLongest(String filepath, int wordLenLim){

        Set<String> longestWords = new HashSet<>();

        try (FileReader reader = new FileReader(filepath)){
            int MaxNOUniqueChar = 0;
            int WordNOUniqueChar = 0;

            char[] buf = new char[1024];
            List<String> words = null;

            while (reader.read(buf) != 0){
                words = tokenize(new String(buf));
                for(String word : words){
                    if(word.length() > MaxNOUniqueChar) {

                        if(word.length() > wordLenLim)
                            word = padWord(word, wordLenLim);

                        WordNOUniqueChar = noUniqueChar(word);
                        if(WordNOUniqueChar < MaxNOUniqueChar)
                            continue;
                        if (WordNOUniqueChar == MaxNOUniqueChar)
                            longestWords.add(word);
                        else{
                            longestWords.clear();
                            longestWords.add(word);
                            MaxNOUniqueChar = WordNOUniqueChar;
                        }
                    }
                }
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return longestWords;
    }

    List<String> tokenize(String str){
        return Arrays.asList(str.split(delims_regex));
    }

    String padWord(String word, int maxlen){
        return word.substring(0, maxlen);
    }

    int noUniqueChar(String word){
        int result = 0;
        for (int i = 0; i < word.length(); i++){
            result++;
            for(int j = 0; j < i; j++){
                if (word.charAt(i) == word.charAt(j)){
                    result--;
                    j = i;      // end of loop
                }
            }
        }
        return result;
    }
}
