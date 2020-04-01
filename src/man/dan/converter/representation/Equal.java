package man.dan.converter.representation;

import man.dan.converter.lexer.Word;
import man.dan.converter.parser.Parser;
import man.dan.converter.parser.TypeError;

public class Equal extends Operator implements Logic, GetsNumeric {
    public Equal(Node l, Node r) throws TypeError {
        super(l, r);
    }

    @Override
    protected int getPriority() {
        return Parser.getPriority(Word.equal);
    }
}
