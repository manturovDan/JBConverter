package man.dan.converter.unittest;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import javax.script.ScriptEngineManager;import javax.script.ScriptEngine;
import javax.script.ScriptException;

public class OnFlightTest {
    @Test
    public void jsTest() throws ScriptException {
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");
        String foo = "40+2";
        Assert.assertEquals(engine.eval(foo), 42);
    }
}
