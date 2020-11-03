package com.tukY.Lab3;

import com.tukY.Lab2.FiniteStateMachine;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class JavaLexer {

    //region    Fields
    private int code;

    private BufferedReader reader;

    //region    keywords
    private final static Set<String> keywords = Set.of(
              "abstract"    , "continue"    , "for"         , "package"     , "synchronized"
            , "assert"      , "default"     , "if"          , "private"     , "this"
            , "boolean"     , "do"          , "implements"  , "protected"   , "throw"
            , "break"       , "double"      , "import"      , "public"      , "throws"
            , "byte"        , "else"        , "instanceof"  , "return"      , "transient"
            , "case"        , "enum"        , "int"         , "short"       , "try"
            , "catch"       , "extends"     , "interface"   , "static"      , "void"
            , "char"        , "final"       , "long"        , "strictfp"    , "volatile"
            , "class"       , "finally"     , "native"      , "super"       , "while"
            , "const"       , "float"       , "new"         , "switch"      , "true"
            , "false"
    );
    //endregion

    //region    FSMs
    private final static FiniteStateMachine FSM_IDENTIFIER  = new FiniteStateMachine("res/Lab3/fsm/identifier.fsm");
    private final static FiniteStateMachine FSM_NUMBER      = new FiniteStateMachine("res/Lab3/fsm/number.fsm");
    private final static FiniteStateMachine FSM_OPERATOR    = new FiniteStateMachine("res/Lab3/fsm/operator.fsm");
    private final static FiniteStateMachine FSM_PUNCTUATION = new FiniteStateMachine("res/Lab3/fsm/punctuation.fsm");
    private final static FiniteStateMachine FSM_LITERAL     = new FiniteStateMachine("res/Lab3/fsm/literal.fsm")
                                                                .append(2, ' ', 2).append(3, ' ', 3);
    private final static FiniteStateMachine FSM_COMMENT     = new FiniteStateMachine("res/Lab3/fsm/comment.fsm")
                                                                .append(3, '\n', 6).append(4, '\n', 4)
                                                                .append(3, ' ', 3).append(4, ' ', 4)
                                                                .append(5, '\n', 4).append(5, ' ', 4)
                                                                .append(4, '\r', 4).append(3, '\r', 6)
                                                                .append(5, '\r', 4);
    //endregion
    //endregion


    public JavaLexer() {}

    public JavaLexer(String filepath) throws IOException {
        open(filepath);
        code = reader.read();
    }

    //region    Methods
    public void open(String filepath) throws FileNotFoundException {
        this.reader = new BufferedReader(new FileReader(filepath));
    }

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

        //  find token's start symbol && skip whitespaces
        while (Character.isWhitespace(code) || code == -1) {

            //  EOF
            if ((code = reader.read()) == -1)
                return new Token(TokenType.END);
        }

        if (FSM_IDENTIFIER.isPossible(current = (char) code)){
            return getIdentifierOrKeyword(current);

        } else if (FSM_NUMBER.isPossible(current)) {
            return getNumber(current);

        } else if (FSM_PUNCTUATION.isPossible(current)){
            return getPunctuation(current);

        } else if (current == '/') {
            Token token = getComment(current);
            return token == null ? getOperator(current) : token;

        } else if (FSM_OPERATOR.isPossible(current)){
            return getOperator(current);

        } else if (FSM_LITERAL.isPossible(current)){
            return getLiteral(current);
        }

        return new Token(String.valueOf(current), TokenType.TYPELESS);
    }
    //endregion


    //region Recognisers
    Token getIdentifierOrKeyword(char start) throws IOException {
        char next = start;
        StringBuilder lexeme = new StringBuilder();

        do {
            lexeme.append(next);
            FSM_IDENTIFIER.exec(next);

        } while (FSM_IDENTIFIER.isPossible(next = (char) (code = reader.read())));

        FSM_IDENTIFIER.reset();
        return keywords.contains(lexeme.toString()) ?
                new Token(lexeme.toString(), TokenType.KEYWORD) :
                new Token(lexeme.toString(), TokenType.IDENTIFIER);
    }

    Token getNumber(char start) throws IOException {

        char next = start;
        StringBuilder lexeme = new StringBuilder();

        reader.mark(64);
        do {
            lexeme.append(next);
            FSM_NUMBER.exec(next);

        } while (FSM_NUMBER.isPossible(next = (char) (code = reader.read())));

        Token number = null;
        if (FSM_NUMBER.getCurrentState().isFinal())
            number = new Token(lexeme.toString(), TokenType.NUMBER);
        else reader.reset();

        FSM_NUMBER.reset();
        return number;
    }

    Token getLiteral(char start) throws IOException {
        char next = start;
        StringBuilder lexeme = new StringBuilder();

        reader.mark(1024);
        do {
            lexeme.append(next);
            FSM_LITERAL.exec(next);

        } while (FSM_LITERAL.isPossible(next = (char) (code = reader.read())));

        Token symbolic = null;
        if (FSM_LITERAL.getCurrentState().isFinal())
            symbolic = new Token(lexeme.toString(), TokenType.LITERAL);
        else reader.reset();

        FSM_LITERAL.reset();
        return symbolic;
    }

    Token getComment(char start) throws IOException {
        char next = start;
        StringBuilder lexeme = new StringBuilder();

        reader.mark(1024);
        do {
            lexeme.append(next);
            FSM_COMMENT.exec(next);

        } while (FSM_COMMENT.isPossible(next = (char) (code = reader.read())));

        Token comment = null;
        if (FSM_COMMENT.getCurrentState().isFinal())
            comment = new Token(lexeme.toString(), TokenType.COMMENT);
        else reader.reset();

        FSM_COMMENT.reset();
        return comment;
    }

    Token getOperator(char start) throws IOException {
        char next = start;
        StringBuilder lexeme = new StringBuilder();

        reader.mark(5);
        do {
            lexeme.append(next);
            FSM_OPERATOR.exec(next);

        } while (FSM_OPERATOR.isPossible(next = (char) (code = reader.read())));

        Token operator = null;
        if (FSM_OPERATOR.getCurrentState().isFinal())
            operator = new Token(lexeme.toString(), TokenType.OPERATOR);
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

        } while (FSM_PUNCTUATION.isPossible(next = (char) (code = reader.read())));

        Token punctuation = null;
        if (FSM_PUNCTUATION.getCurrentState().isFinal())
            punctuation = new Token(lexeme.toString(), TokenType.PUNCTUATION);
        else reader.reset();

        FSM_PUNCTUATION.reset();
        return punctuation;
    }

    //endregion
}

