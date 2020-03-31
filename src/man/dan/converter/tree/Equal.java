package man.dan.converter.tree;

import man.dan.converter.lexer.Word;
import man.dan.converter.parser.Parser;

public class Equal extends Operator implements Logic, GetsNumeric {
    public Equal(Node l, Node r) throws Exception {
        super(l, r);
    }

    @Override
    protected int getPriority() {
        return Parser.getPriority(Word.equal);
    }
}
