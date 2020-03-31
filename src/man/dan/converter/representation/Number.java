package man.dan.converter.representation;

import man.dan.converter.lexer.Num;
import man.dan.converter.lexer.Token;

public class Number extends Operand {
    protected int val;

    public Number(int v) {
        val = v;
    }

    public Number(Token n) {
        val = ((Num)n).value;
    }
}
