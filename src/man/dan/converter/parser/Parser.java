package man.dan.converter.parser;

import man.dan.converter.lexer.Lexer;
import man.dan.converter.lexer.Num;
import man.dan.converter.lexer.Token;
import man.dan.converter.lexer.Word;
import man.dan.converter.tree.Element;
import man.dan.converter.tree.Number;
import man.dan.converter.tree.Operand;

import java.util.*;

public class Parser {
    protected Lexer lexer;
    protected Token look;

    protected ArrayDeque<Operand> operands = new ArrayDeque<>();

    protected static HashMap<Token, Integer> allOperators = new HashMap<Token, Integer>() {{
        put(Word.mul, 1);
        put(Word.plus, 2);
        put(Word.minus, 2);
        put(Word.greater, 3);
        put(Word.less, 3);
        put(Word.equal, 4);
        put(Word.and, 5);
        put(Word.or, 6);
    }};

    public static int getPriority(Word w) {
        return allOperators.get(w);
    }

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
        0) - unary
        1) *
        2) + - binary
        3) > <
        4) =
        5) &
        6) |
         */
        move();
        operands.clear();
        for (; look != Word.cl_brace; move()) {
            System.out.println(look);

            if (look instanceof Num) {
                operands.add(new Number(look));
            }
            else if (look == Word.element) {
                operands.add(new Element());
            }
            else if (allOperators.containsKey(look)) {

            }
        }
    }
}
