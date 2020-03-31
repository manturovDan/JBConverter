package man.dan.converter.tree;

import man.dan.converter.lexer.Word;
import man.dan.converter.parser.Parser;

public class Greater extends Operator implements Logic, GetsNumeric {

    @Override
    protected int getPriority() {
        return Parser.getPriority(Word.greater);
    }
}
