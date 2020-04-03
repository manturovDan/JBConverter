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

    public boolean filter(String expr, int num) throws ScriptException {
        expr = expr.replaceAll("element", String.valueOf(num));
        String res = JSEng.engine.eval(expr).toString();
        //System.out.println("expr:" + expr);
        return res.equals("true") || res.equals("1");
    }

    public int map(String expr, int num) throws ScriptException {
        expr = expr.replaceAll("element", String.valueOf(num));
        String res = JSEng.engine.eval(expr).toString();
        return Integer.parseInt(res);
    }

    public ArrayList<Integer> flight(String expr, int from, int to) throws ScriptException {
        ArrayList<Integer> res = new ArrayList<>();

        expr = expr.replaceAll("=", "==");

        Pattern patn = Pattern.compile("(filter|map)\\{((?:[0-9\\(\\)element\\*\\<>+\\-\\=\\&\\|])+)\\}");
        Matcher match;
        next: for (int i = from; i <= to; ++i) {
            int num = i;
            match = patn.matcher(expr);

            while (match.find()) {
                //System.out.println(match.group(1) + " : " + match.group(2));
                if (match.group(1).equals("filter")) {
                    if(!filter(match.group(2), num)) {
                        continue next;
                    }
                }

                if (match.group(1).equals("map")) {
                    num = map(match.group(2), num);
                }
            }
            res.add(num);
        }

        System.out.println(res);
        return res;
    }

    @Test
    public void FTSimple() throws Exception {
        String expr = "filter{(element>10)}%>%map{(element+5)}%>%filter{(1=1)}";
        Assert.assertEquals(flight(MergerTest.allSteps(expr), 8, 12), flight(expr, 8, 12));
    }
}
