package man.dan.converter.lexer;

public class Word extends Token {
    protected String lexeme;

    protected Word(String s, int tag) {
        super(tag);
        lexeme = s;
    }

    public static final Word
        and = new Word( "&", Tagv.AND),
        or = new Word("|", Tagv.OR),
        equal = new Word("=", Tagv.EQUAL),
        greater = new Word(">", Tagv.GREATER),
        less = new Word("<", Tagv.LESS),
        minus = new Word("-", Tagv.MINUS),
        plus = new Word("+", Tagv.PLUS),
        mul = new Word("*", Tagv.MUL),
        conveyor = new Word("%>%", Tagv.CONVEYOR),
        element = new Word("element", Tagv.ELEMENT),
        map = new Word("map{", Tagv.MAP),
        filter = new Word("filter{", Tagv.FILTER),
        cl_brace = new Word("}", Tagv.CL_BRACE),
        op_bracket = new Word("(", Tagv.OP_BRACKET),
        cl_bracket = new Word(")", Tagv.CL_BRACKET),
        EOS = new Word("EOS", Tagv.EOS);
    //and so on

    public String toString() {
        return lexeme;
    }
}
