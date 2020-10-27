package com.tukY.Lab3;

import java.util.ArrayList;
import java.util.List;

public class JavaLexer {

    //region    Fields
    private String text;
    private int position;
    private int line;
    private int column;
    //endregion


    public JavaLexer() {
        this("");
    }

    public JavaLexer(String text) {
        this.text = text;
        this.position = 0;
        this.line = 0;
        this.column = 0;
    }

    //region    Methods
    public void readText(String text) {
        this.text = text;
    }

    public List<Token> getAll() {
        List<Token> tokens = new ArrayList<>();
        Token token;
        while ((token = next()).getType() != TokenType.END){
            tokens.add(token);
        }
        return tokens;
    }
    public Token next() {
        if (position > text.length())
            return new Token(TokenType.END, line, column);

        char current = text.charAt(position);

        if (Character.isLetter(current) || current == '_'){
            return getIdentifier(current);      //  but can be IDENTIFIER

            // Maybe I need read and then check if it is KEYWORD

        } else if (Character.isDigit(current)) {
            return getNumber(current);
        } else if ("{}()[],;".indexOf(current) >= 0){
            return getPunctuation(current);
        } else if ("+-*/<>!=".indexOf(current) >= 0){
            return getOperator(current);        //  but can be COMMENT

            // Maybe I need read and then check if it is COMMENT

        } else if (current == '\'' || current == '\"'){
            return getSymbolic(current);
        } else if (current == '#' || current == '@'){
            return getDirective(current);
        }

        return null;
    }

    Token getIdentifier(char current) {
        return null;
    }
    Token getNumber(char current) {
        return null;
    }
    Token getSymbolic(char current) {
        return null;
    }
    Token getComment(char current) {
        return null;
    }
    Token getDirective(char current) {
        return null;
    }
    Token getOperator(char current) {
        return null;
    }
    Token getKeyword(char current) {
        return null;
    }
    Token getPunctuation(char current) {
        return null;
    }

    //endregion
}

