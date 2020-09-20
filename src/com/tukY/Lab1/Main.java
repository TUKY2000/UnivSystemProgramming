package com.tukY.Lab1;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Main {

    static void writeInFile(String filename, List<String> words){
        try(FileWriter writer = new FileWriter(filename)){
            writer.write("Words with biggest number of unique characters: \n");
            for (int i = 0, listLen = words.size() - 1; i < listLen; i++){
                writer.write(words.get(i).concat(", "));
            }
            writer.write(words.get(words.size() - 1).concat("."));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        writeInFile(args[1], Lab.findLongest(args[0], 30));
    }
}
