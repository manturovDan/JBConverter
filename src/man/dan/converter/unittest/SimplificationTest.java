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
}
