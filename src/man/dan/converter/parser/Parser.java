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

    public void analysis() throws Exception {
        for (;;) {
            if (look == Word.filter) {
                System.out.println("FILTER");
                expression();
            }
            else if (look == Word.map) {
                System.out.println("MAP");
                expression();
            }

            move();

            if (look == Word.EOS)
                break;
            else if (look != Word.conveyor)
                throw new Exception("Syntax Error");

            System.out.println("PIPE");
            move();
        }
    }

    public void expression() throws Exception {
        /*
        Priority:
        1) - unary
        2) *
        3) + - binary
        4) > <
        5) =
        6) &
        7) |
         */
        move();
        for (; look != Word.cl_brace; move()) {
            System.out.println(look);


        }
    }
}
