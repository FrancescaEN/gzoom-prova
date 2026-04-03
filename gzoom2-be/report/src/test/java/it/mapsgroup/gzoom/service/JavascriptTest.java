package it.mapsgroup.gzoom.service;

import org.junit.Test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * @author Andrea Fossi.
 */
public class JavascriptTest {
    @Test
    public void name() throws ScriptException {
        System.out.println("commentato");
//        ScriptEngineManager factory = new ScriptEngineManager();
//        ScriptEngine engine = factory.getEngineByName("JavaScript");
//        String func="function getFormatPattern(decimalScale,um){\n" +
//                "\n" +
//                "        var pattern = \"#,##0\";\n" +
//                "        if (decimalScale > 0) {\n" +
//                "                pattern = pattern + \".\";\n" +
//                "                \n" +
//                "                for(i=0; i<decimalScale; i++ ){\n" +
//                "                        pattern += \"0\";\n" +
//                "                }\n" +
//                "        }\n" +
//                "        if (\"%\" === um || (um != null && um.indexOf(\"Perc\") != -1)) {\n" +
//                "                pattern = pattern + \"%\";\n" +
//                "        }\n" +
//                "        return pattern;\n" +
//                "\n" +
//                "};" +
//                "print (getFormatPattern(2,\"perc\"));";
//        engine.eval(func);
        //engine.eval("print('Hello, World')");
    }
}
