package man.dan.converter.parser;

import man.dan.converter.lexer.Lexer;
import man.dan.converter.lexer.Num;
import man.dan.converter.lexer.Token;
import man.dan.converter.lexer.Word;
import man.dan.converter.tree.*;
import man.dan.converter.tree.Number;

import java.util.*;

public class Parser {
    protected Lexer lexer;
    protected Token look;

    protected ArrayDeque<Node> operands = new ArrayDeque<>();
    protected ArrayDeque<Word> operators = new ArrayDeque<>();

    protected static HashMap<Token, Integer> allOperators = new HashMap<>() {{
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

    protected void addNode(Token operator) throws Exception {
        Node right = operands.pop();
        Node left = operands.pop();

        if (operator == Word.mul) {
            operands.add(new Multiple(left, right));
        }
        else if (operator == Word.minus) {
            operands.add(new Minus(left, right));
        }
        else if (operator == Word.plus) {
            operands.add(new Plus(left, right));
        }
        else if (operator == Word.greater) {
            operands.add(new Greater(left, right));
        }
        else if (operator == Word.less) {
            operands.add(new Less(left, right));
        }
        else if (operator == Word.equal) {
            operands.add(new Equal(left, right));
        }
        else if (operator == Word.or) {
            operands.add(new Or(left, right));
        }
        else if (operator == Word.and) {
            operands.add(new And(left, right));
        }
        else
            throw new Exception("SYNTAX ERROR");
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

        Word popped;
        rec: for (; look != Word.cl_brace; move()) {
            System.out.println(look);

            if (look instanceof Num) {
                operands.add(new Number(look));
            }
            else if (look.equals(Word.element)) {
                operands.add(new Element());
            }
            else if (allOperators.containsKey(look)) {
                Word curOperator = (Word)look;
                Word iterOperator;

                while (!operators.isEmpty()) {
                    iterOperator = operators.getFirst();

                    if (allOperators.containsKey(iterOperator) && allOperators.get(curOperator) <= allOperators.get(iterOperator)) {
                        operators.pop();
                        addNode(iterOperator);
                    }
                    else
                        break;
                }
            }
            else if (look.equals(Word.op_bracket)) {
                operators.add((Word)look);
            }
            else if (look.equals(Word.cl_bracket)) {
                while (!operators.isEmpty()) {
                    popped = operators.pop();

                    if (popped.equals(Word.op_bracket)) {
                        continue rec;
                    }
                    else {
                        addNode(popped);
                    }
                }

                throw new Exception("SYNTAX ERROR");
            }
        }

        while(!operators.isEmpty()) {
            popped = operators.pop();
            if (look.equals(Word.op_bracket) || look.equals(Word.cl_bracket))
                throw new Exception("SYNTAX ERROR");
            addNode(popped);
        }
    }
}
