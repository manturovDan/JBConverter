package man.dan.converter.unittest;

import man.dan.converter.lexer.Lexer;
import man.dan.converter.parser.Parser;
import man.dan.converter.parser.SyntaxError;
import man.dan.converter.representation.Call;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

public class ErrorRecognizeTest {
    public void waitSyntaxParse(String expr) {
        Lexer lex = new Lexer(expr);
        try {
            new Parser(lex);
            Assert.fail();
        } catch (SyntaxError s) {
            Assert.assertTrue(true);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    public void waitSyntaxAnl(String expr) {
        Lexer lex = new Lexer(expr);
        try {
            Parser parser = new Parser(lex);
            LinkedList<Call> callChain = parser.analysis();
            Assert.fail();
        } catch (SyntaxError s) {
            Assert.assertTrue(true);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void subjErr1() {
        String expr = " mapppp{ element+ 15*3- (element+  4)*10 - 5}";
        waitSyntaxParse(expr);
    }

    @Test
    public void subjErr2() {
        String expr = " f{ element+ 15*3- (element+  4)*10 - 5 > 10}";
        waitSyntaxParse(expr);
    }

    @Test
    public void subjErr3() {
        String expr = " { element+ 15*3- (element+  4)*10 - 5 > 10}";
        waitSyntaxParse(expr);
    }

    @Test
    public void subjErr4() {
        String expr = " filter{ element+ 15*3- (element+  4)*10 - 5 > 10";
        waitSyntaxAnl(expr);
    }

    @Test
    public void trickErr1() {
        String expr = " filter{ element+ 15*3- map {(element+  4)}*10 - 5 > 10 } ";
        waitSyntaxAnl(expr);
    }
}
