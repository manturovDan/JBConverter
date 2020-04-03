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

    public void noErr(String expr) throws Exception {
        Lexer lex = new Lexer(expr);
        Parser parser = new Parser(lex);
        LinkedList<Call> callChain = parser.analysis();
        Merger merger = new Merger(callChain);
        merger.transform();
        Assert.assertTrue(true);
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
        String expr = " mapppp{(((element+(15*3))-((element+4)*10))-5)}";
        waitSyntaxParse(expr);
    }

    @Test
    public void subjErr2() {
        String expr = " f{(((element>-5)&(3<element))|(3=-6))}";
        waitSyntaxParse(expr);
    }

    @Test
    public void subjErr3() {
        String expr = "{(((element>-5)&(3<element))|(3=-6))}";
        waitSyntaxParse(expr);
    }

    @Test
    public void subjErr4() {
        String expr = "filter{(((element>-5)&(3<element))|(3=-6))";
        waitSyntaxAnl(expr);
    }

    @Test
    public void bracketsCorrect() throws Exception {
        String expr = "filter{(((element>-5)&(3<element))|(3=-6))}";
        String expr1 = "map{((((10000*element)*20)+15)*0)}";
        String expr2 = "filter{((((5+element)+13)<4)&(5<(element*(element+10))))}";
        String expr3 = "filter{(((5+3)<8)|(1=0))}";
        String expr4 = "filter{((((5+3)<8)|(1=0))&(element>-9))}";
        String expr5 = "map{(((element+188)*38)-2)}";
        String expr6 = "map{(3*1)}";

        noErr(expr);
        noErr(expr1);
        noErr(expr2);
        noErr(expr3);
        noErr(expr4);
        noErr(expr5);
        noErr(expr6);
        noErr("map{element}");
        noErr("map{1}");
    }

    @Test
    public void bracketsErr1() throws Exception {
        String expr = "filter{(((element>-5)&(3<element>1))|(3=-6))}";
        waitSyntaxAnl(expr);
    }

    @Test
    public void bracketsErr2() throws Exception {
        String expr = "filter{(((element>-5)&3<element)|(3=-6))}";
        waitSyntaxAnl(expr);
    }

    @Test
    public void bracketsErr3() throws Exception {
        String expr = "filter{(((element>-5)&(3<element))|((3)=-6))}";
        waitSyntaxAnl(expr);
    }

    @Test
    public void bracketsErr4() throws Exception {
        String expr = "filter{(((element>-5)&(3<(element)))|(3=-6))}";
        waitSyntaxAnl(expr);
    }

    @Test
    public void bracketsErr5() throws Exception {
        String expr = "filter{(((element>-5)&(3<element))|(3=(-)6))}";
        waitSyntaxAnl(expr);
    }

    @Test
    public void bracketsErr6() throws Exception {
        String expr = "filter{(((element(>-5))&(3<element))|(3=-6))}";
        waitSyntaxAnl(expr);
    }

    @Test
    public void bracketsErr7() throws Exception {
        String expr = "filter{(((element>-5)&(3<element))|(3=-6)}";
        waitSyntaxAnl(expr);
    }

    @Test
    public void bracketsErr8() throws Exception {
        String expr = "filter{((((5+element)+13)<4)&(5<(element*((element+10)))))}";
        waitSyntaxAnl(expr);
    }

    @Test
    public void bracketsErr9() throws Exception {
        String expr = "map{(((((10000*element)*20)+15)*0))}";
        waitSyntaxAnl(expr);
    }

    @Test
    public void bracketsErr10() throws Exception {
        String expr = "map{((((((10000*element)))*20)+15)*0)}";
        waitSyntaxAnl(expr);
    }

    @Test
    public void trickErr1() {
        String expr = "filter{(((element>-5)&(3<element))}|(3=-6))}";
        waitSyntaxAnl(expr);
    }

    @Test
    public void trickErr2() {
        String expr = "map{{(((element+(15*3))-((element+4)*10))-5)}";
        waitSyntaxAnl(expr);
    }

    @Test
    public void chainErr1() {
        String expr = "map{(((element+(15*3))-((element+4)*10))-5)}>>>>map{element}";
        waitSyntaxAnl(expr);
    }

    @Test
    public void chainErr2() {
        String expr = "filter{(((element+(15*3))>((element+4)*10))&(1=0))}%>%%>%map{element}";
        waitSyntaxAnl(expr);
    }

    @Test
    public void chainErr3() {
        String expr = "filter{(((element+(15*3))>((element+4)*10))&(1=0))}%>%%>%map{}";
        waitSyntaxAnl(expr);
    }

    @Test
    public void chainErr4() {
        String expr = "filter{}%>%filter{(((element+(15*3))>((element+4)*10))&(1=0))}%>%%>%map{element}";
        waitSyntaxAnl(expr);
    }

    @Test
    public void expressionErr1() {
        String expr = "filter{((((5+element)+13)<4)&(5<(ele*ment*(element+10))))}";
        waitSyntaxAnl(expr);
    }

    @Test
    public void expressionErr2() throws Exception {
        noErr("filter{(1=0)}%>%filter{((((5+element)+13)<4)&(5<(element*(element+10))))}%>%map{(3*1)}%>%map{element}");
        String expr1 = "filter{(1=0)}%>%filter{((((5+element)+13)<4)&(5<(element*(element+10))))}%>%map{(3*1)-}%>%map{element}";
        waitSyntaxAnl(expr1);

        String expr2 = "filter{(1=0)}%>%filter{((((5+element)+13)<4)&(5<(element*(element+10))))}%>%map{((3*1)-)}%>%map{element}";
        waitSyntaxAnl(expr2);

        String expr3 = "filter{(1=0)}%>%filter{((((5+element)+13)<4)0&(5<(element*(element+10))))}%>%map{(3*1)}%>%map{element}";
        waitSyntaxAnl(expr3);

        String expr4 = "filter{(1=0)}%>%filter{((((5+element)+13)<4)(1=1)&(5<(element*(element+10))))}%>%map{(3*1)}%>%map{element}";
        waitSyntaxAnl(expr4);
    }

    @Test
    public void expressionErr3() {
        String expr3 = "filter{(1=0)}%>%filter{((((5+element)+13)<4)0&(5<(element*(element+10))))}%>%map{(3*1)}%>%map{element}";
        waitSyntaxAnl(expr3);

        String expr4 = "filter{(1=0)}%>%filter{((((5+element)+13)<4)(1=1)&(5<(element*(element+10))))}%>%map{(3*1)}%>%map{element}";
        waitSyntaxAnl(expr4);
    }

    @Test
    public void expressionErr4() {
        String expr = "filter{(1=0)}%>%filter{(((element+(15*3))>=((element+4)*10))-5)}%>%map{((((10000*element)*20)+15)*0)}%>%map{element}";
        waitSyntaxAnl(expr);
    }

    @Test
    public void expressionErr5() {
        String expr = "filter{(((element-(--15*3))>((element+4)*10))-5)}";
        waitSyntaxAnl(expr);
    }

    @Test
    public void expressionErr6() {
        String expr = "map{element+50}";
        waitSyntaxAnl(expr);

        String expr1 = "map{(eleme+50)}";
        waitSyntaxAnl(expr1);
    }

    @Test
    public void expressionErr7() {
        String expr = "map{((element+(50-4*))-10)}";
        waitSyntaxAnl(expr);

        String expr1 = "map{((element+(50-4))-10)*}";
        waitSyntaxAnl(expr1);
    }

    @Test
    public void expressionErr8() {
        String expr = "filter{(element>5000000000)}";
        waitSyntaxAnl(expr);
    }

    @Test
    public void expressionErr9() {
        String expr = "filter{(element<>-99990)}";
        waitSyntaxAnl(expr);
    }

    @Test
    public void expressionErr10() {
        String expr = "filter{(element<-5000000000)}";
        waitSyntaxAnl(expr);
    }

    @Test
    public void expressionErr11() {
        String expr = "filter{((element+-21)>-599)()}";
        waitSyntaxAnl(expr);

        String expr1 = "filter{((element+-21)>(-599+()))}";
        waitSyntaxAnl(expr1);
    }

    @Test
    public void expressionErr12() {
        String expr = "map{(((element+188)*38)-2)}%>%filter{(((5+3)<8)|(1=0))}%>%";
        waitSyntaxAnl(expr);
    }

    @Test
    public void expressionErr13() {
        String expr = "%>%";
        waitSyntaxAnl(expr);
    }

    @Test
    public void expressionErr14() {
        String expr = "%>%map{(3*1)}";
        waitSyntaxAnl(expr);
    }

    @Test
    public void expressionErr15() throws Exception {
        String expr = "map{0}%>%";
        waitSyntaxAnl(expr);

        noErr("map{0}");
    }

    //No spaces

    @Test
    public void spaceErr1() {
        String expr = "  map{(0)}";
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
        String expr = "map{ (element*5) }%>%map{ (element * (element+element))}%>%map{((element+1)* element )}";
        waitSyntaxAnl(expr);
    }

    @Test
    public void spaceErr5() {
        String expr = "map{(element*5)}%>%map{ 4 }%>%map {((element+1)*element)}";
        waitSyntaxAnl(expr);
    }

    //Types checks

    @Test
    public void typeErr1() {
        String expr = "filter{0}";
        waitTypeAnl(expr);

        String expr2 = "filter{0}%>%map{0}";
        waitTypeAnl(expr2);
    }

    @Test
    public void typeErr2() {
        String expr = "map{(1=2)}";
        waitTypeAnl(expr);

        String expr2 = "filter{(1=2)}%>%map{(element>10)}";
        waitTypeAnl(expr2);
    }

    @Test
    public void typeErr3() {
        String expr = "map{(1=2)}%>%filter{(a)}";
        waitSyntaxAnl(expr);

        String expr2 = "filter{(a)}%>%map{(1=2)}";
        waitSyntaxAnl(expr2);
    }

    @Test
    public void typeErrAnd() {
        waitTypeAnl("filter{((((5+element)+13)<4)&(5*(element*(element+10))))}");
        waitTypeAnl("filter{((((5+element)+13)+4)&(5<(element*(element+10))))}");
        waitSyntaxAnl("filter{((((5+element)+13)+4)&(5<(element*(element+10))))}%>%map{1)}");
        waitTypeAnl("filter{((((5+element)+13)+4)&(5<(element*(element+10))))}%>%map{1}");
    }

    @Test
    public void typeCorrect() throws Exception {
        String expr = "filter{((element+-21)>-599)}%>%filter{(1<2)}%>%filter{((element<-21)&(1=0))}";
        noErr(expr);
    }

    @Test
    public void typeErrOr0() {
        String expr = "filter{((element+-21)>-599)}%>%filter{(1<2)}%>%filter{((element<-21)|(1+0))}";
        waitTypeAnl(expr);
    }

    @Test
    public void typeErrOr() {
        String expr = "filter{((element+-21)|-599)}%>%filter{(1<2)}%>%filter{((element<-21)&(1=0))}";
        waitTypeAnl(expr);
    }

    @Test
    public void typeEqErr() {
        String expr = "filter{(((5+3)<8)|(1=(element>2)))}";
        waitTypeAnl(expr);
    }

    @Test
    public void typeGrErr() {
        String expr = "filter{(((5=3)>element)|(1=0))}";
        waitTypeAnl(expr);
    }

    @Test
    public void typeLsErr() {
        String expr = "filter{(((5=3)<element)|(1=0))}";
        waitTypeAnl(expr);
    }

    @Test
    public void typePlusErr() {
        String expr = "map{(((element+188)*38)+(2=2))}";
        waitTypeAnl(expr);
    }

    @Test
    public void typeMinusErr() {
        String expr = "map{(((element+188)*38)-(2=2))}";
        waitTypeAnl(expr);
    }

    @Test
    public void typeMulErr() {
        String expr = "map{(((element+188)*38)*(2=2))}";
        waitTypeAnl(expr);
    }

}
