package man.dan.converter.lexer;

import man.dan.converter.parser.SyntaxError;

public class Num extends Token {
    public final int value;

    protected Num(int val) {
        super(Tag.NUM);
        value = val;
    }

    public static boolean isOverflow(int left, int right) {
        return right > 0 ? Integer.MAX_VALUE - right < left : Integer.MIN_VALUE - right > left;
    }

    public static void checkOverflow(int left, int right) throws SyntaxError {
        if (isOverflow(left, right))
            throw new SyntaxError();
    }

    public String toString() {
        return String.valueOf(value);
    }
}
