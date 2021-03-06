package man.dan.converter.representation;

import man.dan.converter.lexer.Word;
import man.dan.converter.parser.Parser;
import man.dan.converter.parser.TypeError;

public class Multiple extends Operator implements Numeric, GetsNumeric {
    public Multiple(Node l, Node r) throws TypeError {
        super(l, r);
    }

    @Override
    protected Multiple clone() throws CloneNotSupportedException {
        return (Multiple)super.clone();
    }

    @Override
    public String toString() {
        return "*";
    }
}
