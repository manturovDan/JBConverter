package man.dan.converter.parser;

import man.dan.converter.lexer.Lexer;
import man.dan.converter.lexer.Token;
import man.dan.converter.lexer.Word;

public class Parser {
    protected Lexer lexer;
    protected Token look;

    public Parser(Lexer l) throws Exception {
        lexer = l;
        move();
    }

    protected void move() throws Exception {
        look = lexer.scan();
    }

    public void analysis() {

    }

    public void expression() throws Exception {
        while (look != Word.EOS) {
            System.out.println(look);
            move();
        }
    }
}
