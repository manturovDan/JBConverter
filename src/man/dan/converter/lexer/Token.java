package man.dan.converter.lexer;

public class Token {
    public final Tag tag;

    protected Token(Tag t) {
        tag = t;
    }

    public String toString() {
        return tag.toString();
    }
}
