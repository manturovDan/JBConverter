package man.dan.converter.unittest;

import man.dan.converter.lexer.Lexer;
import man.dan.converter.parser.Parser;
import man.dan.converter.parser.SyntaxError;
import man.dan.converter.parser.TypeError;
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

    public void waitTypeAnl(String expr) {
        Lexer lex = new Lexer(expr);
        try {
            Parser parser = new Parser(lex);
            LinkedList<Call> callChain = parser.analysis();
            Assert.fail();
        } catch (TypeError s) {
            Assert.assertTrue(true);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    //Syntax checks

    @Test
    public void subjErr0() {
        String expr = "   ";
        waitSyntaxAnl(expr);

        String expr2 = "";
        waitSyntaxAnl(expr2);
    }

    @Test
    public void subjErr1() {
        String expr = " mapppp{element+15*3-(element+4)*10-5}";
        waitSyntaxParse(expr);
    }

    @Test
    public void subjErr2() {
        String expr = " f{element+15*3-(element+4)*10-5> 0}";
        waitSyntaxParse(expr);
    }

    @Test
    public void subjErr3() {
        String expr = "{element+15*3-(element+4)*10-5>10}";
        waitSyntaxParse(expr);
    }

    @Test
    public void subjErr4() {
        String expr = "filter{element+15*3-(element+4)*10-5>10";
        waitSyntaxAnl(expr);
    }

    @Test
    public void trickErr1() {
        String expr = "filter{element+15*3-map{(element+4)}*10-5>10} ";
        waitSyntaxAnl(expr);
    }

    @Test
    public void trickErr2() {
        String expr = "filter{element+15*3-{(element+4)}*10-5>10 } ";
        waitSyntaxAnl(expr);
    }

    @Test
    public void chainErr1() {
        String expr = "filter{element+15*3-(element+4)*10-5>10}>>>>map{element}";
        waitSyntaxAnl(expr);
    }

    @Test
    public void chainErr2() {
        String expr = "filter{element+15*3-(element+4)*10-5>10}%>%%>%map{element}";
        waitSyntaxAnl(expr);
    }

    @Test
    public void chainErr3() {
        String expr = "filter{element+15*3-(element+4)*10-5>10}%>%map{element}%>%map{}";
        waitSyntaxAnl(expr);
    }

    @Test
    public void chainErr4() {
        String expr = "filter{}%>%filter{element+15*3-(element+4)*10-5>10}%>%map{element}%>%map{element}";
        waitSyntaxAnl(expr);
    }

    @Test
    public void expressionErr1() {
        String expr = "filter{1=0}%>%filter{element+15*3-(element+4))*10-5>10}%>%map{element}%>%map{element}";
        waitSyntaxAnl(expr);
    }

    @Test
    public void expressionErr2() {
        String expr = "filter{1=0}%>%filter{element+15*3-(element+4)*10-5>10}%>%map{element-}%>%map{element}";
        waitSyntaxAnl(expr);
    }

    @Test
    public void expressionErr3() {
        String expr = "filter{1==0}%>%filter{element+15*3-(element+4)*10-5>10}%>%map{element}%>%map{element}";
        waitSyntaxAnl(expr);
    }

    @Test
    public void expressionErr4() {
        String expr = "filter{1=0}%>%filter{(element+15*3-(element+4)*10-5>=10}%>%map{element}%>%map{element}";
        waitSyntaxAnl(expr);
    }

    @Test
    public void expressionErr5() {
        String expr = "filter{element+--15*3>10}";
        waitSyntaxAnl(expr);
    }

    @Test
    public void expressionErr6() {
        String expr = "map{elemnt+50}";
        waitSyntaxAnl(expr);
    }

    @Test
    public void expressionErr7() {
        String expr = "map{(element+(50 - 4*)-10)}";
        waitSyntaxAnl(expr);
    }

    @Test
    public void expressionErr8() {
        String expr = "filter{element>5000000000}";
        waitSyntaxAnl(expr);
    }

    @Test
    public void expressionErr9() {
        String expr = "filter{element<>-99990}";
        waitSyntaxAnl(expr);
    }

    @Test
    public void expressionErr10() {
        String expr = "filter{element<-5000000000}";
        waitSyntaxAnl(expr);
    }

    @Test
    public void expressionErr11() {
        String expr = "filter{element+-21>--599}";
        waitSyntaxAnl(expr);
    }

    @Test
    public void expressionErr12() {
        String expr = "map{element+-21*-599}%>%filter{element+21>-599}%>%";
        waitSyntaxAnl(expr);
    }

    @Test
    public void expressionErr13() {
        String expr = "%>%";
        waitSyntaxAnl(expr);
    }

    @Test
    public void expressionErr14() {
        String expr = "%>%map{element+-21*-599}";
        waitSyntaxAnl(expr);
    }

    @Test
    public void expressionErr15() {
        String expr = "map{element+-21*-599}%>%";
        waitSyntaxAnl(expr);
    }

    //No spaces

    @Test
    public void spaceErr1() {
        String expr = "  map{element+-21*-599}";
        waitSyntaxAnl(expr);
    }

    @Test
    public void spaceErr2() {
        String expr = "filter{(element>10)} %>% filter{(element<20)}";
        waitSyntaxAnl(expr);
    }


    @Test
    public void spaceErr3() {
        String expr = "filter{(element>10)}%>%filter{(element<20)} ";
        waitSyntaxAnl(expr);
    }


    @Test
    public void spaceErr4() {
        String expr = "map{ element*5 }%>%map{ element * element+element}%>%map{(element+1)* element }";
        waitSyntaxAnl(expr);
    }

    @Test
    public void spaceErr5() {
        String expr = "map{element*5}%>%map{element*element+element}%>%map {(element+1)*element} ";
        waitSyntaxAnl(expr);
    }

    //Types checks

    @Test
    public void typeErr1() {
        String expr = "filter{element+-21>-599}";
        waitTypeAnl(expr);
    }
}
