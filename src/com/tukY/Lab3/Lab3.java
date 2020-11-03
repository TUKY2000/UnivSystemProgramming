package com.tukY.Lab3;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class Lab3 {

    /**
     * Write tokens to file in format <value> - <token_type>
     * @param filepath path to file to write tokens
     * @param tokens tokens ot write
     * @param ignore token's types won't be written
     */
    static void writeTokens(String filepath, List<Token> tokens, List<TokenType> ignore){

    }

    /**
     * Lexical analysis for Java programming language
     * @param args path to file will be analyzed by JavaLexer class
     */
    public static void main(String[] args) {
        try {
            writeTokens(args[0].substring(0, args[0].indexOf('.')) + ".tokens"
                    , new JavaLexer(args[0]).getAll()
                    , List.of(TokenType.COMMENT));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
