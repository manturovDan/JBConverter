package man.dan.converter.tree;

import man.dan.converter.lexer.Word;
import man.dan.converter.parser.Parser;

public class Or extends Operator implements Logic, GetsLogic {
    public Or(Node l, Node r) throws Exception {
        super(l, r);
    }

    @Override
    protected int getPriority() {
        return Parser.getPriority(Word.or);
    }
}
