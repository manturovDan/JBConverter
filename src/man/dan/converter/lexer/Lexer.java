package man.dan.converter.lexer;

import man.dan.converter.parser.SyntaxError;

import java.util.HashMap;
import java.lang.*;

import static java.lang.Math.abs;

public class Lexer {
    protected HashMap<String, Word> words = new HashMap<>();
    protected String parsed;
    int curCh = -1;
    char peek = ' ';
    protected boolean waitBinMinus = false;

    protected void reserve(Word w) {
        words.put(w.lexeme, w);
    }

    void readCh() throws SyntaxError {
        if (curCh == parsed.length() - 1) {
            peek = '\n';
            curCh++;
        }
        else if (curCh == parsed.length())
            throw new SyntaxError();
        else
            peek = parsed.charAt(++curCh);
    }

    void cancelStep() {
        curCh--;
    }

    boolean readCh(char c) throws SyntaxError {
        readCh();
        if (peek != c)
            return false;
        peek = ' ';
        return true;
    }

    public Lexer(String s) {
        parsed = s;
        reserve(Word.filter);
        reserve(Word.map);
        reserve(Word.cl_brace);
        reserve(Word.element);
    }

    public Token scan() throws SyntaxError {
        readCh();

        boolean waitBinMinusCurrent = waitBinMinus;
        waitBinMinus = false;
        int sign = 1;

        switch (peek) {
            case '&':
                return Word.and;
            case '|':
                return Word.or;
            case '=':
                return Word.equal;
            case '>':
                return Word.greater;
            case '<':
                return Word.less;
            case '+':
                return Word.plus;
            case '-':
                if (waitBinMinusCurrent)
                    return Word.minus;
                else { //unary minus
                    sign = -1;
                    readCh();
                    break;
                }
            case '*':
                return Word.mul;
            case '(':
                return Word.op_bracket;
            case ')':
                waitBinMinus = true;
                return Word.cl_bracket;
            case '}':
                return Word.cl_brace;
            case '\n':
                return Word.EOS;
            case '%':
                if (readCh('>') && readCh('%'))
                    return Word.conveyor;
        }

        if(Character.isDigit(peek)) {
            long val = 0;
            int add;

            do {
                if (val > Integer.MAX_VALUE)
                    throw new SyntaxError();

                val *= 10;
                add = Character.digit(peek, 10);

                val += add;

                readCh();
            } while (Character.isDigit(peek));
            cancelStep();
            val *= sign;
            if (val > Integer.MAX_VALUE || val < Integer.MIN_VALUE)
                throw new SyntaxError();

            waitBinMinus = true;

            return new Num((int)val);
        }

        if (Character.isLetter(peek)) {
            StringBuilder str = new StringBuilder();

            do {
                str.append(peek);
                readCh();
            } while (Character.isLetter(peek));

            if (peek == '{') {
                str.append(peek);
            } else
                cancelStep();

            String s = str.toString();
            Word w = words.get(s);

            if (w == Word.element)
                waitBinMinus = true;

            if (w != null)
                return w;

            throw new SyntaxError();
        }

        throw new SyntaxError();
    }
}
