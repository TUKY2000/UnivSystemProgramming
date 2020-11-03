package com.tukY.Lab3;

public class Token {

    //region    Fields
    private final String value;
    private final TokenType type;
    //endregion


    //region    Constructors
    public Token() {
        this(TokenType.TYPELESS);
    }

    public Token(TokenType type) {
        this("", type);
    }

    public Token(String value, TokenType type) {
        this.value = value;
        this.type = type;
    }
    //endregion

    //region    Methods
    public String getValue() {
        return value;
    }

    public TokenType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "<" + value
                + ">\t-\t<"
                + type + '>';
    }

    //endregion
}
