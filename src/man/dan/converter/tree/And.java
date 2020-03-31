package man.dan.converter.tree;

import man.dan.converter.lexer.Word;
import man.dan.converter.parser.Parser;

public class And extends Operator implements Logic, GetsLogic {

    @Override
    protected int getPriority() {
        return Parser.getPriority(Word.and);
    }
}
