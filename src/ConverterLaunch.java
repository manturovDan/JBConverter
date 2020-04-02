import man.dan.converter.lexer.Lexer;
import man.dan.converter.parser.Parser;
import man.dan.converter.parser.SyntaxError;
import man.dan.converter.parser.TypeError;
import man.dan.converter.representation.Call;
import man.dan.converter.transformer.Merger;

import java.util.LinkedList;

public class ConverterLaunch  {
    public static void main(String[] args) throws Exception {
        System.out.println(Convert.conv(args[0]));
    }
}
