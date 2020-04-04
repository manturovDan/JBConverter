package man.dan.converter.lexer;

import man.dan.converter.parser.SyntaxError;

public class Num extends Token {
    public final int value;

    protected Num(int val) {
        super(Tag.NUM);
        value = val;
    }

    public String toString() {
        return String.valueOf(value);
    }
}
