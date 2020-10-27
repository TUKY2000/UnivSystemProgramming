package com.tukY.Lab3;

public class Token {

    //region    Fields
    private final String name;
    private final String value;
    private final TokenType type;
    private final int line;
    private final int column;
    //endregion


    //region    Constructors
    public Token(int line, int col) {
        this(TokenType.TYPELESS, line, col);
    }

    public Token(TokenType type, int line, int col) {
        this("", "", type, line, col);
    }

    public Token(String name, String value, TokenType type, int line, int col) {
        this.name = name;
        this.value = value;
        this.type = type;
        this.line = line;
        this.column = col;
    }
    //endregion

    //region    Methods
    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public TokenType getType() {
        return type;
    }

    public int getRow() {
        return line;
    }

    public int getColumn() {
        return column;
    }
    //endregion
}
