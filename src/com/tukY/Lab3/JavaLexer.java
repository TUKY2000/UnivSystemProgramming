package com.tukY.Lab3;

import java.util.List;

public class JavaLexer {

    //region    Fields
    private String text;
    private int position;
    private int line;
    private int column;
    //endregion

    public JavaLexer(String text) {
        this.text = text;
    }

    //region    Methods
    public void readText(String text) {
        this.text = text;
    }

    public List<Token> getAll() {
        return null;
    }
    public Token next() {
        return null;
    }

    Token getIdentifier() {
        return null;
    }
    Token getNumber() {
        return null;
    }
    Token getSymbolic() {
        return null;
    }
    Token getComment() {
        return null;
    }
    Token getDirective() {
        return null;
    }
    Token getOperator() {
        return null;
    }
    Token getKeyword() {
        return null;
    }
    Token getPunctuation() {
        return null;
    }

    //endregion
}

