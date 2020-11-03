package com.tukY.Lab3;

import com.tukY.Lab2.FiniteStateMachine;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class JavaLexer {

    //region    Fields
    private String filepath;
    private int line;
    private int column;

    private BufferedReader reader;

    //region    FSMs
    private final static FiniteStateMachine FSM_IDENTIFIER  = new FiniteStateMachine("identifier.fsm");
    private final static FiniteStateMachine FSM_NUMBER      = new FiniteStateMachine("number.fsm");
    private final static FiniteStateMachine FSM_SYMBOLIC    = new FiniteStateMachine("symbolic.fsm");
    private final static FiniteStateMachine FSM_COMMENT     = new FiniteStateMachine("comment.fsm");
    private final static FiniteStateMachine FSM_OPERATOR    = new FiniteStateMachine("operator.fsm");
    private final static FiniteStateMachine FSM_KEYWORD     = new FiniteStateMachine("keyword.fsm");
    private final static FiniteStateMachine FSM_PUNCTUATION = new FiniteStateMachine("punctuation.fsm");
    //endregion
    //endregion

    public JavaLexer(String filepath) throws FileNotFoundException {
        this.filepath = filepath;
        this.line = 0;
        this.column = 0;

        reader = new BufferedReader(new FileReader(filepath));
    }

    //region    Methods
    public void close() throws IOException {
        reader.close();
    }

    public List<Token> getAll() throws IOException {
        List<Token> tokens = new ArrayList<>();
        Token token;
        while ((token = next()).getType() != TokenType.END){
            tokens.add(token);
        }
        return tokens;
    }

    public Token next() throws IOException {
        char current;
        int code;

        //  find token's start symbol
        while (true) {
            //  EOF
            if ((code = reader.read()) == -1)
                return new Token(TokenType.END, line, column);

            //  skip whitespaces
            if ((current = (char) code) == '\n') {
                line++;
                column = 0;
            } else if (current == ' '){
                column++;
            } else if (current == '\t'){
                column += 4;
            } else break;
        }

        if (FSM_IDENTIFIER.isPossible(current)){
            Token token = getKeyword(current);
            return token == null ? token : getIdentifier(current);

        } else if (FSM_NUMBER.isPossible(current)) {
            return getNumber(current);

        } else if (FSM_PUNCTUATION.isPossible(current)){
            return getPunctuation(current);

        } else if (current == '/') {
            Token token = getComment(current);
            return token == null ? token : getOperator(current);

        } else if (FSM_OPERATOR.isPossible(current)){
            return getOperator(current);

        } else if (FSM_SYMBOLIC.isPossible(current)){
            return getSymbolic(current);
        }

        return null;
    }
    //endregion


    //region Recognisers
    Token getIdentifier(char start) throws IOException {

        char next = start;
        StringBuilder lexeme = new StringBuilder();

        do {
            lexeme.append(next);
            FSM_IDENTIFIER.exec(next);
            column++;

        } while (FSM_IDENTIFIER.isPossible(next = (char) reader.read()));

        FSM_IDENTIFIER.reset();
        return new Token(lexeme.toString(), TokenType.IDENTIFIER, line, column);
    }

    Token getKeyword(char start) throws IOException {

        char next = start;
        StringBuilder lexeme = new StringBuilder();

        reader.mark(10);
        do {
            lexeme.append(next);
            FSM_KEYWORD.exec(next);
            column++;

        } while (FSM_KEYWORD.isPossible(next = (char) reader.read()));

        Token keyword = null;
        if (FSM_KEYWORD.getCurrentState().isFinal())
            keyword = new Token(lexeme.toString(), TokenType.KEYWORD, line, column);
        else reader.reset();

        FSM_KEYWORD.reset();
        return keyword;
    }

    Token getNumber(char start) throws IOException {

        char next = start;
        StringBuilder lexeme = new StringBuilder();

        reader.mark(10);
        do {
            lexeme.append(next);
            FSM_NUMBER.exec(next);
            column++;

        } while (FSM_NUMBER.isPossible(next = (char) reader.read()));

        Token number = null;
        if (FSM_NUMBER.getCurrentState().isFinal())
            number = new Token(lexeme.toString(), TokenType.NUMBER, line, column);
        else reader.reset();

        FSM_NUMBER.reset();
        return number;
    }

    Token getSymbolic(char start) throws IOException {
        char next = start;
        StringBuilder lexeme = new StringBuilder();

        reader.mark(10);
        do {
            lexeme.append(next);
            FSM_SYMBOLIC.exec(next);
            column++;

        } while (FSM_SYMBOLIC.isPossible(next = (char) reader.read()));

        Token symbolic = null;
        if (FSM_SYMBOLIC.getCurrentState().isFinal())
            symbolic = new Token(lexeme.toString(), TokenType.SYMBOLIC, line, column);
        else reader.reset();

        FSM_SYMBOLIC.reset();
        return symbolic;
    }

    Token getComment(char start) throws IOException {
        char next = start;
        StringBuilder lexeme = new StringBuilder();

        reader.mark(10);
        do {
            lexeme.append(next);
            FSM_COMMENT.exec(next);
            column++;

        } while (FSM_COMMENT.isPossible(next = (char) reader.read()));

        Token comment = null;
        if (FSM_COMMENT.getCurrentState().isFinal())
            comment = new Token(lexeme.toString(), TokenType.COMMENT, line, column);
        else reader.reset();

        FSM_COMMENT.reset();
        return comment;
    }

    Token getOperator(char start) throws IOException {
        char next = start;
        StringBuilder lexeme = new StringBuilder();

        reader.mark(10);
        do {
            lexeme.append(next);
            FSM_OPERATOR.exec(next);
            column++;

        } while (FSM_OPERATOR.isPossible(next = (char) reader.read()));

        Token operator = null;
        if (FSM_OPERATOR.getCurrentState().isFinal())
            operator = new Token(lexeme.toString(), TokenType.OPERATOR, line, column);
        else reader.reset();

        FSM_OPERATOR.reset();
        return operator;
    }

    Token getPunctuation(char start) throws IOException {
        char next = start;
        StringBuilder lexeme = new StringBuilder();

        reader.mark(10);
        do {
            lexeme.append(next);
            FSM_PUNCTUATION.exec(next);
            column++;

        } while (FSM_PUNCTUATION.isPossible(next = (char) reader.read()));

        Token punctuation = null;
        if (FSM_PUNCTUATION.getCurrentState().isFinal())
            punctuation = new Token(lexeme.toString(), TokenType.PUNCTUATION, line, column);
        else reader.reset();

        FSM_PUNCTUATION.reset();
        return punctuation;
    }

    //endregion
}

