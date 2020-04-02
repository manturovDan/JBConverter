import man.dan.converter.lexer.Lexer;
import man.dan.converter.parser.Parser;
import man.dan.converter.parser.SyntaxError;
import man.dan.converter.parser.TypeError;
import man.dan.converter.representation.Call;
import man.dan.converter.transformer.Merger;

import java.util.LinkedList;

public class Convert {
    public static String conv(String input) throws Exception {
        try {
            Lexer lex = new Lexer(input);
            Parser parser = new Parser(lex);
            LinkedList<Call> callChain = parser.analysis();
            Merger merger = new Merger(callChain);
            merger.transform();
            return "";
        } catch (SyntaxError s) {
            return "Syntax Error";
        } catch (TypeError t) {
            return "Type Error";
        }
    }
}
