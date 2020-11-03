package com.tukY.Lab3;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Lab3 {

    /**
     * Write tokens to file in format: value - token_type
     * @param filepath path to file to write tokens
     * @param tokens tokens ot write
     * @param ignore token's types won't be written
     */
    static void writeTokens(String filepath, List<Token> tokens, List<TokenType> ignore){

        try (FileWriter writer = new FileWriter(filepath)){
            for (Token token : tokens)
                if (!ignore.contains(token.getType()))
                    writer.write(token.toString() + '\n');

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lexical analysis for Java programming language
     * @param args path to file will be analyzed by JavaLexer class
     */
    public static void main(String[] args) {
        try {
            writeTokens(args[0].concat(".tokens")
                    , new JavaLexer(args[0]).getAll()
                    , List.of(TokenType.COMMENT));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
