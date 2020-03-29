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
        mul = new Word("*", Tag.MUL),
        conveyor = new Word("%>%", Tag.CONVEYOR),
        element = new Word("element", Tag.ELEMENT),
        map = new Word("map{", Tag.MAP),
        filter = new Word("filter{", Tag.FILTER),
        cl_brace = new Word("}", Tag.CL_BRACE),
        op_bracket = new Word("(", Tag.OP_BRACKET),
        cl_bracket = new Word(")", Tag.CL_BRACKET);
    //and so on

    public String toString() {
        return lexeme;
    }
}
