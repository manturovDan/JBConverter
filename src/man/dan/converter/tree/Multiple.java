package man.dan.converter.tree;

import man.dan.converter.lexer.Word;
import man.dan.converter.parser.Parser;

public class Multiple extends Operator implements Numeric, GetsNumeric {
    protected static int priority = 1;

    @Override
    protected int getPriority() {
        return Parser.getPriority(Word.mul);
    }
}
