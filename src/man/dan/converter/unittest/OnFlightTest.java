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
    public static ScriptEngineManager mgr = new ScriptEngineManager();
    public static ScriptEngine engine = mgr.getEngineByName("JavaScript");
}

public class OnFlightTest {

    @BeforeAll
    public static void jsInit() {
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");
    }
    @Test
    public void jsTest() throws ScriptException {
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");
        String foo = "40+2";
        Assert.assertEquals(engine.eval(foo), 42);
    }

    public boolean filter(String expr, long num) throws ScriptException {
        expr = expr.replaceAll("element", String.valueOf(num));
        String res = JSEng.engine.eval(expr).toString();
        //System.out.println("expr:" + expr);
        return res.equals("true") || res.equals("1");
    }

    public long map(String expr, long num) throws ScriptException {
        expr = expr.replaceAll("element", String.valueOf(num));
        String res = JSEng.engine.eval(expr).toString();
        if (res.equals("-0.0") || res.equals("-0"))
            return 0;
        return (long)(Double.parseDouble(res));
    }

    public ArrayList<Long> flight(String expr, int from, int to, boolean resulted) throws ScriptException {
        ArrayList<Long> res = new ArrayList<>();

        expr = expr.replaceAll("=", "==");

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

                    if(!filter(match.group(2), num)) {
                        continue next;
                    }
                }

                if (match.group(1).equals("map")) {
                    if (resulted && step != 1)
                        Assert.fail();
                    num = map(match.group(2), num);
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
        String expr = "filter{(element>10)}%>%map{(element+5)}%>%filter{(1=1)}";
        Assert.assertEquals(flight(expr, 8, 12, false), flight(expr, 8, 12, false));

        Assert.assertNotEquals(flight("filter{((((5+3)<8)|(1=0))&(element>-9))}", 8, 12, false), flight(expr, 8, 12, false));
    }

    @Test
    public void FTSimple() throws Exception {
        String expr = "filter{(element>10)}%>%map{(element+5)}%>%filter{(1=1)}";
        Assert.assertEquals(flight(MergerTest.allSteps(expr), 8, 12, true), flight(expr, 8, 12, false));
    }

    @Test
    public void AllPlacementsTest() throws Exception {
        int kMax = 3;
        StringBuilder bldTest = new StringBuilder();
        for (int k = 1; k <= kMax; ++k) {
            for (int i = (int)(Math.pow(16, k)); i < 2*(int)(Math.pow(16, k)); ++i) {
                bldTest.setLength(0);
                int num = i;
                while (num > 1) {
                    bldTest.append(ConvTestLst.lst.get(num % 16)).append("%>%");
                    num /= 16;
                }

                bldTest.setLength(bldTest.length() - 3);
                System.out.println(bldTest);
                String str = bldTest.toString();

                Assert.assertEquals(flight(MergerTest.allSteps(str), -200, 200, true), flight(str, -200, 200, false));
            }
        System.out.println();
        }
    }
}
