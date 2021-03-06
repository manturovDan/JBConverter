package man.dan.converter.representation;

import man.dan.converter.lexer.Word;
import man.dan.converter.parser.Parser;
import man.dan.converter.parser.TypeError;

public class Greater extends Operator implements Logic, GetsNumeric {
    public Greater(Node l, Node r) throws TypeError {
        super(l, r);
    }

    @Override
    protected Greater clone() throws CloneNotSupportedException {
        return (Greater)super.clone();
    }

    @Override
    public String toString() {
        return ">";
    }
}
