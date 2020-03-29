package man.dan.converter.lexer;

public class Token {
    public final int tag;

    protected Token(int t) {
        tag = t;
    }

    public String toString() {
        return String.valueOf(tag);
    }
}
