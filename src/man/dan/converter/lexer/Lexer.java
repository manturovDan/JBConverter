package man.dan.converter.lexer;

import java.util.HashMap;
import java.lang.*;

import static java.lang.Math.abs;

public class Lexer {
    protected HashMap<String, Word> words = new HashMap<>();
    protected String parsed;
    int curCh = -1;
    char peek = ' ';

    protected void reserve(Word w) {
        words.put(w.lexeme, w);
    }

    void readCh() {
        if (curCh == parsed.length() - 1)
            peek = '\n';
        else
            peek = parsed.charAt(++curCh);
    }

    void cancelStep() {
        curCh--;
    }

    boolean readCh(char c) {
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

    public Token scan() throws Exception {
        readCh();

        while (peek == ' ' || peek == '\t')
            readCh();

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
                return Word.minus;
            case '*':
                return Word.mul;
            case '(':
                return Word.op_bracket;
            case ')':
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
            int val = 0;
            int add;

            do {
                if (val > Integer.MAX_VALUE / 10)
                    throw new Exception("Syntax error");

                val *= 10;
                add = Character.digit(peek, 10);
                Num.checkOverflow(val, add);
                val += add;

                readCh();
            } while (Character.isDigit(peek));
            cancelStep();

            return new Num(val);
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

            if (w != null)
                return w;

            throw new Exception("Syntax Error");
        }

        throw new Exception("Syntax Error");
    }
}
