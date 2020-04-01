package man.dan.converter.representation;

import man.dan.converter.lexer.Word;
import man.dan.converter.parser.Parser;
import man.dan.converter.parser.TypeError;

public class And extends Operator implements Logic, GetsLogic {
    public And(Node l, Node r) throws TypeError {
        super(l, r);
    }

    @Override
    protected int getPriority() {
        return Parser.getPriority(Word.and);
    }
}
