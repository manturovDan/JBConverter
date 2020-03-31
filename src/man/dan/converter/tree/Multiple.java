package man.dan.converter.tree;

import man.dan.converter.lexer.Num;
import man.dan.converter.lexer.Word;
import man.dan.converter.parser.Parser;

public class Multiple extends Operator implements Numeric, GetsNumeric {
    public Multiple(Node l, Node r) throws Exception {
        super(l, r);
    }

    @Override
    protected int getPriority() {
        return Parser.getPriority(Word.mul);
    }
}
