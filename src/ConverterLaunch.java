import man.dan.converter.lexer.Lexer;
import man.dan.converter.parser.Parser;

public class ConverterLaunch {
    public static void main(String[] args) throws Exception {
        Lexer lex = new Lexer(args[0]);
        Parser parser = new Parser(lex);
        parser.expression();
    }
}
