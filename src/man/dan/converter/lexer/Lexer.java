package man.dan.converter.lexer;

import java.util.HashMap;
import java.lang.*;

import static java.lang.Math.abs;

public class Lexer {
    protected HashMap<String, Word> words = new HashMap<>();
    protected String parsed;
    int curCh = 0;
    char peek = ' ';

    protected void reserve(Word w) {
        words.put(w.lexeme, w);
    }

    void readCh() {
        peek = parsed.charAt(++curCh);
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
        //reserve(new Word());
    }

    public Token scan() throws Exception {
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

            return new Num(val);
        }

        if (Character.isLetter(peek)) {
            StringBuilder str = new StringBuilder();

            do {
                str.append(peek);
                readCh();
            } while (Character.isLetter(peek));

            String s = str.toString();

            Word w = words.get(s);
            if (w != null)
                return w;

            throw new Exception("Syntax Error");
        }

        Token tok = new Token(peek);
        peek = ' ';
        return tok; //MAYBE DELETE
    }
}
