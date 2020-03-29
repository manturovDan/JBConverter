package man.dan.converter.lexer;

public class Word extends Token {
    protected String lexeme;

    protected Word(String s, int tag) {
        super(tag);
        lexeme = s;
    }

    public static final Word
        and = new Word( "&", Tag.AND),
        or = new Word("|", Tag.OR),
        equal = new Word("=", Tag.EQUAL),
        greater = new Word(">", Tag.GREATER),
        less = new Word("<", Tag.LESS),
        minus = new Word("-", Tag.MINUS),
        plus = new Word("+", Tag.PLUS),
        mul = new Word("*", Tag.MUL);
    //and so on

    public String toString() {
        return lexeme;
    }
}
