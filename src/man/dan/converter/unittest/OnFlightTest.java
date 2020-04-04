package man.dan.converter.unittest;
import man.dan.converter.parser.Parser;
import man.dan.converter.transformer.Merger;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class JSEng {
    public ScriptEngineManager mgr = new ScriptEngineManager();
    public ScriptEngine engine = mgr.getEngineByName("JavaScript");
}

public class OnFlightTest {
    @Test
    public void jsTest() throws ScriptException {
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");
        String foo = "40+2";
        Assert.assertEquals(engine.eval(foo), 42);
    }

    public boolean filter(String expr, long num, JSEng eng) throws ScriptException {
        expr = expr.replaceAll("element", String.valueOf(num));
        String res = eng.engine.eval(expr).toString();
        //System.out.println("expr:" + expr);
        return res.equals("true") || res.equals("1");
    }

    public long map(String expr, long num, JSEng eng) throws ScriptException {
        expr = expr.replaceAll("element", String.valueOf(num));
        String res = eng.engine.eval(expr).toString();
        if (res.equals("-0.0") || res.equals("-0"))
            return 0;
        return (long)(Double.parseDouble(res));
    }

    public ArrayList<Long> flight(String expr, int from, int to, boolean resulted, JSEng eng) throws ScriptException {
        ArrayList<Long> res = new ArrayList<>();

        expr = expr.replaceAll("=", "==");
        expr = expr.replaceAll("--", "+");

        Pattern patn = Pattern.compile("(filter|map)\\{((?:[0-9\\(\\)element\\*\\<>+\\-\\=\\&\\|])+)\\}");
        Matcher match;
        next: for (int i = from; i <= to; ++i) {
            long num = i;
            match = patn.matcher(expr);

            int step = 0;
            while (match.find()) {
                if (resulted && step == 2)
                    Assert.fail();
                //System.out.println(match.group(1) + " : " + match.group(2));
                if (match.group(1).equals("filter")) {
                    if (resulted && step != 0)
                        Assert.fail();

                    if(!filter(match.group(2), num, eng)) {
                        continue next;
                    }
                }

                if (match.group(1).equals("map")) {
                    if (resulted && step != 1)
                        Assert.fail();
                    num = map(match.group(2), num, eng);
                }

                ++step;
            }
            res.add(num);
        }

        //System.out.println(res);
        return res;
    }

    @Test
    public void sandTest() throws Exception {
        JSEng eng = new JSEng();
        String expr = "filter{(element>10)}%>%map{(element+5)}%>%filter{(1=1)}";
        Assert.assertEquals(flight(expr, 8, 12, false, eng), flight(expr, 8, 12, false, eng));

        Assert.assertNotEquals(flight("filter{((((5+3)<8)|(1=0))&(element>-9))}", 8, 12, false, eng), flight(expr, 8, 12, false, eng));
    }

    @Test
    public void FTSimple() throws Exception {
        JSEng eng = new JSEng();
        String expr = "filter{(element>10)}%>%map{(element+5)}%>%filter{(1=1)}";
        Assert.assertEquals(flight(SimplificationTest.allStepsSimpl(expr), 8, 12, true, eng), flight(expr, 8, 12, false, eng));
    }

    @Test
    public void Pl1Test() throws Exception {
        testForOneFMCountCount(1, 1);
    }

    @Test
    public void Pl2Test() throws Exception {
        testForOneFMCountCount(2, 0.1);
    }

    @Test
    public void Pl3Test() throws Exception {
        testForOneFMCountCount(3, 0.005);
    }

    @Test
    public void Pl4Test() throws Exception {
        testForOneFMCountCount(4, 0.0005);
    }

    @Test
    public void Pl5Test() throws Exception {
        testForOneFMCountCount(5, 0.00001);
    }

    public void testForOneFMCountCount(int k, double probability) throws Exception {
        JSEng eng = new JSEng();

        StringBuilder bldTest = new StringBuilder();
        for (int i = (int)(Math.pow(16, k)); i < 2*(int)(Math.pow(16, k)); ++i) {
            if (Math.random() >= probability)
                continue;
            bldTest.setLength(0);
            int num = i;
            while (num > 1) {
                bldTest.append(ConvTestLst.lst.get(num % 16)).append("%>%");
                num /= 16;
            }

            bldTest.setLength(bldTest.length() - 3);
            //System.out.println(bldTest);
            String str = bldTest.toString();

            Assert.assertEquals(flight(SimplificationTest.allStepsSimpl(str), -200, 200, true, eng), flight(str, -200, 200, false, eng));
        }
    }
}
