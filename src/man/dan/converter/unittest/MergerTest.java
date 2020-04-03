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
                "filter{(element>10)&(element>10)}%>%map{element}");
    }

    @Test
    public void example2Test() throws Exception {
        Assert.assertEquals(allSteps("map{(element+10)}%>%filter{(element>10)}%>%map{(element*element)}"),
                "filter{((element+10)>10)}%>%map{((element+10)*(element+10))}");
    }
}