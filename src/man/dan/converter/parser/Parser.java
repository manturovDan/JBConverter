package man.dan.converter.parser;

import man.dan.converter.lexer.Lexer;
import man.dan.converter.lexer.Num;
import man.dan.converter.lexer.Token;
import man.dan.converter.lexer.Word;
import man.dan.converter.representation.*;
import man.dan.converter.representation.Number;

import java.util.*;

public class Parser {
    protected Lexer lexer;
    protected Token look;

    protected ArrayDeque<Node> operands = new ArrayDeque<>();
    protected ArrayDeque<Word> operators = new ArrayDeque<>();

    protected static HashSet<Token> allOperators = new HashSet<>() {{
        add(Word.mul);
        add(Word.plus);
        add(Word.minus);
        add(Word.greater);
        add(Word.less);
        add(Word.equal);
        add(Word.and);
        add(Word.or);
    }};

    public Parser(Lexer l) throws SyntaxError {
        lexer = l;
        move();
    }

    protected void move() throws SyntaxError {
        look = lexer.scan();
    }

    public LinkedList<Call> analysis() throws SyntaxError, TypeError {
        LinkedList<Call> callChain = new LinkedList<>();

        for (;;) {
            if (look == Word.filter) {
                System.out.println("FILTER");
                expression();
                try {
                callChain.add(new FilterCall((Logic) operands.element()));
                }
                catch (ClassCastException c) {
                    throw new TypeError();
                }
            }
            else if (look == Word.map) {
                System.out.println("MAP");
                expression();
                try {
                    callChain.add(new MapCall((Numeric) operands.element()));
                }
                catch (ClassCastException c) {
                    throw new TypeError();
                }
            }
            else
                throw new SyntaxError();

            move();

            if (look == Word.EOS)
                break;
            else if (look != Word.conveyor)
                throw new SyntaxError();

            System.out.println("PIPE");
            move();
        }

        if (callChain.size() == 0)
            throw  new SyntaxError();

        return callChain;
    }

    protected void addNode(Token operator) throws SyntaxError, TypeError {
        Node right;
        Node left;

        try {
            right = operands.removeLast();
            left = operands.removeLast();
        } catch (NoSuchElementException e) {
            throw new SyntaxError();
        }

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
            throw new SyntaxError();


        left.setParent(operands.getLast());
        right.setParent(operands.getLast());
    }

    public void expression() throws SyntaxError, TypeError {
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
            else if (allOperators.contains(look)) {
                Word curOperator = (Word)look;

                if (!(operators.isEmpty()) && allOperators.contains(operators.getLast()))
                    throw new SyntaxError();

                operators.add(curOperator);
            }
            else if (look.equals(Word.op_bracket)) {
                operators.add((Word)look);
            }
            else if (look.equals(Word.cl_bracket)) {
                popped = operators.removeLast();
                if (popped.equals(Word.op_bracket))
                    throw new SyntaxError();
                addNode(popped);
                if (!operators.removeLast().equals(Word.op_bracket))
                    throw new SyntaxError();

            }
            else
                throw new SyntaxError();
        }

        if (operands.isEmpty() || !operators.isEmpty())
            throw new SyntaxError();
    }
}
