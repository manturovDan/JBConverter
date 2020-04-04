package man.dan.converter.unittest;

import man.dan.converter.lexer.Lexer;
import man.dan.converter.parser.Parser;
import man.dan.converter.representation.Call;
import man.dan.converter.simplification.Simplificator;
import man.dan.converter.transformer.MakeString;
import man.dan.converter.transformer.Merger;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

public class SimplificationTest {
    public static String allStepsSimpl(String input) throws Exception {
        Lexer lex = new Lexer(input);
        Parser parser = new Parser(lex);
        LinkedList<Call> callChain = parser.analysis();
        Simplificator simplificator = new Simplificator(callChain);
        Merger merger = new Merger(callChain);
        merger.transform();
        simplificator.simpl();
        return MakeString.view(callChain);
    }

    @Test
    public void plusLSimplTest() throws Exception {
        Assert.assertEquals(allStepsSimpl("filter{(element>10)}%>%map{((element+10)*(20+15))}"),
                "filter{(element>10)}%>%map{((element+10)*35)}");

    }

    @Test
    public void plusRSimplTest() throws Exception {
        Assert.assertEquals(allStepsSimpl("filter{(element>10)}%>%map{((20+15)-(element+10))}"),
                "filter{(element>10)}%>%map{(35-(element+10))}");

    }

    @Test
    public void plusOvfRSimplTest() throws Exception {
        Assert.assertEquals(allStepsSimpl("filter{(element>10)}%>%map{((2000000000+1000000000)-(element+10))}"),
                "filter{(element>10)}%>%map{((2000000000+1000000000)-(element+10))}");

    }

    @Test
    public void SimplTestDeep() throws Exception {
        Assert.assertEquals(allStepsSimpl("filter{(element>10)}%>%map{(((20+15)-(element+10))*(((1*(2-18))*10)+((7*8)-3)))}"),
                "filter{(element>10)}%>%map{((35-(element+10))*-107)}");

    }

    @Test
    public void minusLSimplTest() throws Exception {
        Assert.assertEquals(allStepsSimpl("filter{(element>10)}%>%map{((-5-15)-(element+10))}"),
                "filter{(element>10)}%>%map{(-20-(element+10))}");

    }

    @Test
    public void minusRSimplTest() throws Exception {
        Assert.assertEquals(allStepsSimpl("filter{(element>10)}%>%map{((element+10)*(-5-15))}"),
                "filter{(element>10)}%>%map{((element+10)*-20)}");

    }

    @Test
    public void minusOvfSimplTest() throws Exception {
        Assert.assertEquals(allStepsSimpl("filter{(element>10)}%>%filter{(((3+4)-(((-5-1)+element)+4))=1)}%>%filter{((9-((9+element)+(2+2)))=(-2147483648-1))}"),
                "filter{(((element>10)&((7-((-6+element)+4))=1))&((9-((9+element)+4))=(-2147483648-1)))}%>%map{element}");

    }

    @Test
    public void mulNumSimplTest() throws Exception {
        Assert.assertEquals(allStepsSimpl("filter{((10*0)<element)}%>%map{(element-(3*40))}%>%filter{((9-((9+element)+(2*2)))=(-2147483648*-1))}%>%filter{(element>0)}%>%map{(element*-1)}"),
                "filter{(((0<element)&((9-((9+(element-120))+4)))=(-2147483648*-1))&(((element-120)>0))))}%>%map{((element-120)*-1)}");
    }
}
