import man.dan.converter.lexer.Lexer;
import man.dan.converter.parser.Parser;
import man.dan.converter.parser.SyntaxError;
import man.dan.converter.parser.TypeError;

public class ConverterLaunch {
    public static void main(String[] args) throws SyntaxError, TypeError {
        Lexer lex = new Lexer(args[0]);
        Parser parser = new Parser(lex);
        parser.analysis();
    }
}
