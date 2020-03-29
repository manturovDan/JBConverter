package man.dan.converter.lexer;

public class Num extends Token {
    public final int value;

    protected Num(int val) {
        super(Tag.NUM);
        value = val;
    }

    public static boolean isOverflow(int left, int right) {
        return right > 0 ? Integer.MAX_VALUE - right < left : Integer.MIN_VALUE - right > left;
    }

    public static void checkOverflow(int left, int right) throws Exception {
        if (isOverflow(left, right))
            throw new Exception("Syntax error");
    }

    public String toString() {
        return String.valueOf(value);
    }
}
