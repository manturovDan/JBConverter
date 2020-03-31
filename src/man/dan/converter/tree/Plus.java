package man.dan.converter.tree;

import man.dan.converter.lexer.Word;
import man.dan.converter.parser.Parser;

public class Plus extends Operator implements Numeric, GetsNumeric {
    public Plus(Node l, Node r) throws Exception {
        super(l, r);
    }

    @Override
    protected int getPriority() {
        return Parser.getPriority(Word.plus);
    }
}
