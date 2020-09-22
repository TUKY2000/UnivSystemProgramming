package com.tukY.Lab1;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public final class Lab {

    static String re_delims = "\\W+";
    static String re_notIdentifier = "\\s\\d\\w+";

    private Lab(){}

    static public List<String> findLongest(String filepath, int wordLenLim){

        try (FileReader reader = new FileReader(filepath)){

            //region Init Variables
            List<String> longestWords = new ArrayList<>();

            int maxUniques = 0;

            char[] buf = new char[4096];
            List<String> words = null;

            boolean flagLastSliced = false;
            String wordLastSliced = "";

            //endregion

            while (reader.read(buf) != -1){

                words = tokenize(clearedFromNotIdentifier(new String(buf)));

                // region Last Word was Sliced
                if(flagLastSliced){
                    flagLastSliced = false;
                    words.add(0, wordLastSliced.concat(words.remove(0)));
                }

                if(Character.isLetterOrDigit(buf[buf.length-1])) {
                    flagLastSliced = true;
                    wordLastSliced = words.remove(words.size() - 1);
                }
                // endregion

                for (int i = 0; i < words.size(); i++) {
                    maxUniques = toProcess(longestWords, words.get(i), maxUniques, wordLenLim);
                }
                Arrays.fill(buf, '\u0000');  // clear buffer array
            }

            //  if last word of text was not processed
            if(flagLastSliced)
                toProcess(longestWords, wordLastSliced, maxUniques, wordLenLim);

            return longestWords;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return new ArrayList<>();
    }

    static private int toProcess(List<String> longestWords, String word, int maxUniques, int maxLen){
        if (word.length() >= maxUniques && !longestWords.contains(word)) {

            if (word.length() > maxLen)
                word = padWord(word, maxUniques);

            int wordUniques = noUniqueChar(word);

            if (wordUniques == maxUniques && !longestWords.contains(word))
                longestWords.add(word);
            else if (wordUniques > maxUniques){
                longestWords.clear();
                longestWords.add(word);
                return wordUniques;
            }
        }
        return maxUniques;
    }

    private static String clearedFromNotIdentifier(String str){
        return str.replace(re_notIdentifier, "");
    }

    static private List<String> tokenize(String str){
        return new ArrayList<>(Arrays.asList(str.split(re_delims)));
    }

    static private String padWord(String word, int len){
        return word.substring(0, len);
    }

    static  private int noUniqueChar(String word){
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
