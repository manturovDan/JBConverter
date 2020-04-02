package man.dan.converter.unittest;

import man.dan.converter.lexer.Lexer;
import man.dan.converter.parser.Parser;
import man.dan.converter.parser.SyntaxError;
import man.dan.converter.representation.Call;
import man.dan.converter.transformer.Merger;
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

    public void waitSyntaxMerge(String expr) {
        Lexer lex = new Lexer(expr);
        try {
            Parser parser = new Parser(lex);
            LinkedList<Call> callChain = parser.analysis();
            Assert.fail();
            Merger merger = new Merger(callChain);
            merger.transform();
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

    @Test
    public void trickErr2() {
        String expr = " filter{ element+ 15*3- {(element+  4)}*10 - 5 > 10 } ";
        waitSyntaxAnl(expr);
    }

    @Test
    public void chainErr1() {
        String expr = " filter{ element+ 15*3- (element+  4)*10 - 5 > 10 } >>>> map {element} ";
        waitSyntaxAnl(expr);
    }

    @Test
    public void chainErr2() {
        String expr = " filter{ element+ 15*3- (element+  4)*10 - 5 > 10 } %>% %>% map {element} ";
        waitSyntaxAnl(expr);
    }

    @Test
    public void chainErr3() {
        String expr = " filter{ element+ 15*3- (element+  4)*10 - 5 > 10 } %>% map {element} %>% map{} ";
        waitSyntaxAnl(expr);
    }

    @Test
    public void chainErr4() {
        String expr = "filter{} %>%  filter{ element+ 15*3- (element+  4)*10 - 5 > 10 } %>% map {element} %>% map{element} ";
        waitSyntaxAnl(expr);
    }
}
