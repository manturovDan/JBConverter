package man.dan.converter.tree;

import man.dan.converter.lexer.Word;
import man.dan.converter.parser.Parser;

public class Plus extends Operator implements Numeric, GetsNumeric {

    @Override
    protected int getPriority() {
        return Parser.getPriority(Word.plus);
    }
}
