package man.dan.converter.unittest;

import man.dan.converter.lexer.Lexer;
import man.dan.converter.parser.Parser;
import man.dan.converter.parser.SyntaxError;
import man.dan.converter.parser.TypeError;
import man.dan.converter.representation.Call;
import man.dan.converter.transformer.MakeString;
import man.dan.converter.transformer.Merger;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class MergerTest {
    String allSteps(String input) throws Exception {
        Lexer lex = new Lexer(input);
        Parser parser = new Parser(lex);
        LinkedList<Call> callChain = parser.analysis();
        Merger merger = new Merger(callChain);
        merger.transform();
        return MakeString.view(callChain);
    }

    @Test
    public void example1Test() throws Exception {
        Assert.assertEquals(allSteps("filter{(element>10)}%>%filter{(element>10)}"),
                "filter{((element>10)&(element>10))}%>%map{element}");
    }

    @Test
    public void example2Test() throws Exception {
        Assert.assertEquals(allSteps("map{(element+10)}%>%filter{(element>10)}%>%map{(element*element)}"),
                "filter{((element+10)>10)}%>%map{((element+10)*(element+10))}");
    }

    @Test
    public void example3Test() throws Exception {
        Assert.assertEquals(allSteps("filter{(element>10)}%>%filter{(element<10)}%>%map{(element*element)}"),
                "filter{((element>10)&(element<10))}%>%map{(element*element)}");
    }

    @Test
    public void exampleMy1() throws Exception {
        Assert.assertEquals(allSteps("filter{((element+(15*3))=(((element+4)*10)-5))}%>%filter{(((5+3)<8)|(1=0))}%>%filter{((((5+3)<8)|(1=0))&(element>-9))}"),
                "filter{((((element+(15*3))=(((element+4)*10)-5))&(((5+3)<8)|(1=0)))&((((5+3)<8)|(1=0))&(element>-9)))}%>%map{element}");

        Assert.assertEquals(allSteps("filter{((element+(15*3))=(((element+4)*10)-5))}%>%filter{(((5+3)<8)|(1=0))}%>%filter{((((5+3)<8)|(1=0))&(element>-9))}%>%map{(element+1)}"),
                "filter{((((element+(15*3))=(((element+4)*10)-5))&(((5+3)<8)|(1=0)))&((((5+3)<8)|(1=0))&(element>-9)))}%>%map{(element+1)}");
    }

    @Test
    public void exampleMy2() throws Exception {
        //Assert.assertEquals(allSteps("map{((((10000*element)*20)+15)*0)}%>%map{(((element+188)*38)-2)}%>%map{(3*1)}%>%map{element}"),
        //        "filter{(1=1)}%>%map{(3*1)}");

        Assert.assertEquals(allSteps("map{(3*1)}%>%map{element}"),
                "filter{(1=1)}%>%map{(3*1)}");
    }
}